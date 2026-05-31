package com.coffeeshop.service;

import com.coffeeshop.dao.DAO;
import com.coffeeshop.model.Products;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Hệ thống gợi ý sản phẩm kết hợp Collaborative Filtering và Rule-based.
 *
 * ═══════════════════════════════════════════════════════════════
 * THUẬT TOÁN: Hybrid Recommendation System
 * ═══════════════════════════════════════════════════════════════
 *
 * 1. Collaborative Filtering (CF) - Trọng số 0.6:
 *    - Cosine Similarity giữa các cặp user
 *    - KNN (K=5): chọn K người dùng gần nhất
 *    - Dự đoán điểm cho sản phẩm chưa mua
 *
 * 2. Rule-based (RB) - Trọng số 0.4:
 *    - Cold Start: gợi ý Best Seller
 *    - Returning User: ưu tiên sản phẩm mua thường xuyên
 *
 * 3. Kết hợp: final score = 0.6 × CF + 0.4 × RB
 *
 * 4. Cross-selling: "Bạn có thể cũng thích"
 *    - Co-occurrence: sản phẩm thường mua cùng
 *    - Fallback: cùng danh mục
 *
 * ═══════════════════════════════════════════════════════════════
 */
public class RecommendationService {

    private static final double CF_WEIGHT = 0.6;
    private static final double RB_WEIGHT = 0.4;
    private static final int K_NEIGHBORS = 5;
    private static final int MAX_RECOMMENDATIONS = 5;
    private static final long MATRIX_TTL_MS = 30 * 60 * 1000; // 30 minutes

    // In-memory caches
    private volatile Map<UUID, Map<UUID, Double>> userProductMatrix;
    private volatile Map<UUID, Map<UUID, Double>> userSimilarityMatrix;
    private volatile Set<UUID> allProductIds;
    private volatile long matrixLastBuilt = 0;

    private static RecommendationService instance;

    public static synchronized RecommendationService getInstance() {
        if (instance == null) instance = new RecommendationService();
        return instance;
    }

    private RecommendationService() {}

    /**
     * Gợi ý sản phẩm cho user (CF + Rule-based).
     */
    public List<Products> getRecommendations(UUID userId) {
        ensureMatricesBuilt();

        if (userProductMatrix == null || userProductMatrix.isEmpty()) {
            return getBestSellers();
        }

        Map<UUID, Double> userRatings = userProductMatrix.get(userId);
        boolean isNewUser = (userRatings == null || userRatings.isEmpty());

        if (isNewUser) {
            return getBestSellers();
        }

        Map<UUID, Double> cfScores = computeCFScores(userId);
        Map<UUID, Double> rbScores = computeRuleBasedScores(userId);

        Map<UUID, Double> combinedScores = new HashMap<>();
        Set<UUID> candidates = new HashSet<>();
        candidates.addAll(cfScores.keySet());
        candidates.addAll(rbScores.keySet());

        for (UUID pid : candidates) {
            double cf = cfScores.getOrDefault(pid, 0.0);
            double rb = rbScores.getOrDefault(pid, 0.0);
            combinedScores.put(pid, CF_WEIGHT * cf + RB_WEIGHT * rb);
        }

        List<UUID> recommendedIds = combinedScores.entrySet().stream()
            .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
            .limit(MAX_RECOMMENDATIONS)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        if (recommendedIds.isEmpty()) return getBestSellers();

        return loadProductsByIds(recommendedIds);
    }

    /**
     * Cross-selling: "Bạn có thể cũng thích" khi xem 1 sản phẩm.
     */
    public List<Products> getCrossSelling(UUID productId) {
        ensureMatricesBuilt();

        List<UUID> usersWhoBought = new ArrayList<>();
        if (userProductMatrix != null) {
            for (Map.Entry<UUID, Map<UUID, Double>> entry : userProductMatrix.entrySet()) {
                if (entry.getValue().containsKey(productId)) {
                    usersWhoBought.add(entry.getKey());
                }
            }
        }

        if (usersWhoBought.size() < 2) {
            return getFallbackCrossSelling(productId);
        }

        Map<UUID, Double> coOccurrence = new HashMap<>();
        for (UUID uid : usersWhoBought) {
            Map<UUID, Double> ratings = userProductMatrix.get(uid);
            for (Map.Entry<UUID, Double> r : ratings.entrySet()) {
                if (!r.getKey().equals(productId)) {
                    coOccurrence.merge(r.getKey(), r.getValue(), Double::sum);
                }
            }
        }

        if (coOccurrence.isEmpty()) return getFallbackCrossSelling(productId);

        List<UUID> ids = coOccurrence.entrySet().stream()
            .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
            .limit(3)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        List<Products> results = loadProductsByIds(ids);

        if (results.size() < 3) {
            for (Products p : getFallbackCrossSelling(productId)) {
                if (!results.contains(p) && results.size() < 3) results.add(p);
            }
        }
        return results;
    }

    /**
     * Best sellers — fallback khi chưa đủ dữ liệu CF.
     */
    public List<Products> getBestSellers() {
        String sql = "SELECT oi.snapshot_product_name, SUM(oi.quantity) as total_qty " +
                     "FROM order_items oi JOIN orders o ON oi.order_id = o.id " +
                     "WHERE o.status != 'CANCELLED' " +
                     "GROUP BY oi.snapshot_product_name ORDER BY total_qty DESC LIMIT ?";
        List<Products> result = new ArrayList<>();
        try {
            Connection conn = DAO.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, MAX_RECOMMENDATIONS);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("snapshot_product_name");
                Products p = findProductByName(name);
                if (p != null && p.isAvailable() && !result.contains(p)) {
                    result.add(p);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }

        if (result.size() < MAX_RECOMMENDATIONS) {
            List<Products> all = loadAllProducts();
            Set<UUID> existing = result.stream().map(Products::getId).collect(Collectors.toSet());
            all.stream().filter(p -> p.isAvailable() && !existing.contains(p.getId()))
                .limit(MAX_RECOMMENDATIONS - result.size())
                .forEach(result::add);
        }
        return result;
    }

    // ═══════════════════════════════════════════════════════════
    // MATRIX BUILDING
    // ═══════════════════════════════════════════════════════════

    private synchronized void ensureMatricesBuilt() {
        if (System.currentTimeMillis() - matrixLastBuilt < MATRIX_TTL_MS && userProductMatrix != null) return;

        String sql = "SELECT o.user_id, oi.product_id, SUM(oi.quantity) as total_qty " +
                     "FROM order_items oi JOIN orders o ON oi.order_id = o.id " +
                     "WHERE o.status != 'CANCELLED' AND o.user_id IS NOT NULL AND oi.product_id IS NOT NULL " +
                     "GROUP BY o.user_id, oi.product_id";

        Map<UUID, Map<UUID, Double>> matrix = new HashMap<>();
        Set<UUID> productIds = new HashSet<>();

        try {
            Connection conn = DAO.getConnection();
            ResultSet rs = conn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                UUID uid = UUID.fromString(rs.getString("user_id"));
                UUID pid = UUID.fromString(rs.getString("product_id"));
                double qty = rs.getDouble("total_qty");
                productIds.add(pid);
                matrix.computeIfAbsent(uid, k -> new HashMap<>()).merge(pid, qty, Double::sum);
            }
        } catch (SQLException e) { e.printStackTrace(); }

        // Normalize
        for (Map<UUID, Double> vec : matrix.values()) {
            double max = vec.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
            if (max > 0) vec.replaceAll((k, v) -> v / max);
        }

        // Cosine similarity
        Map<UUID, Map<UUID, Double>> simMatrix = new HashMap<>();
        List<UUID> userIds = new ArrayList<>(matrix.keySet());
        for (int i = 0; i < userIds.size(); i++) {
            UUID u1 = userIds.get(i);
            Map<UUID, Double> v1 = matrix.get(u1);
            simMatrix.put(u1, new HashMap<>());
            for (int j = i + 1; j < userIds.size(); j++) {
                UUID u2 = userIds.get(j);
                Map<UUID, Double> v2 = matrix.get(u2);
                double sim = cosineSimilarity(v1, v2);
                simMatrix.get(u1).put(u2, sim);
                simMatrix.computeIfAbsent(u2, k -> new HashMap<>()).put(u1, sim);
            }
        }

        userProductMatrix = matrix;
        userSimilarityMatrix = simMatrix;
        allProductIds = productIds;
        matrixLastBuilt = System.currentTimeMillis();
    }

    private double cosineSimilarity(Map<UUID, Double> v1, Map<UUID, Double> v2) {
        Set<UUID> common = new HashSet<>(v1.keySet());
        common.retainAll(v2.keySet());
        if (common.isEmpty()) return 0.0;
        double dot = 0;
        for (UUID k : common) dot += v1.get(k) * v2.get(k);
        double m1 = Math.sqrt(v1.values().stream().mapToDouble(v -> v * v).sum());
        double m2 = Math.sqrt(v2.values().stream().mapToDouble(v -> v * v).sum());
        return (m1 == 0 || m2 == 0) ? 0.0 : dot / (m1 * m2);
    }

    // ═══════════════════════════════════════════════════════════
    // SCORING
    // ═══════════════════════════════════════════════════════════

    private Map<UUID, Double> computeCFScores(UUID userId) {
        Map<UUID, Double> scores = new HashMap<>();
        if (userSimilarityMatrix == null || !userSimilarityMatrix.containsKey(userId)) return scores;

        Map<UUID, Double> sims = userSimilarityMatrix.get(userId);
        Map<UUID, Double> ratings = userProductMatrix.getOrDefault(userId, Collections.emptyMap());

        List<Map.Entry<UUID, Double>> neighbors = sims.entrySet().stream()
            .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
            .limit(K_NEIGHBORS).collect(Collectors.toList());

        for (UUID pid : allProductIds) {
            if (ratings.containsKey(pid)) continue;
            double wSum = 0, sSum = 0;
            for (Map.Entry<UUID, Double> n : neighbors) {
                Map<UUID, Double> nRatings = userProductMatrix.get(n.getKey());
                if (nRatings != null && nRatings.containsKey(pid)) {
                    wSum += n.getValue() * nRatings.get(pid);
                    sSum += Math.abs(n.getValue());
                }
            }
            if (sSum > 0) scores.put(pid, wSum / sSum);
        }
        return scores;
    }

    private Map<UUID, Double> computeRuleBasedScores(UUID userId) {
        Map<UUID, Double> scores = new HashMap<>();
        Map<UUID, Double> ratings = userProductMatrix.getOrDefault(userId, Collections.emptyMap());

        if (ratings.isEmpty()) {
            // Cold start: best sellers
            String sql = "SELECT oi.product_id, SUM(oi.quantity) as cnt FROM order_items oi " +
                         "JOIN orders o ON oi.order_id = o.id WHERE o.status != 'CANCELLED' " +
                         "AND oi.product_id IS NOT NULL GROUP BY oi.product_id ORDER BY cnt DESC LIMIT 10";
            try {
                ResultSet rs = DAO.getConnection().prepareStatement(sql).executeQuery();
                double maxCnt = 1;
                List<Object[]> tmp = new ArrayList<>();
                while (rs.next()) {
                    UUID pid = UUID.fromString(rs.getString("product_id"));
                    double cnt = rs.getDouble("cnt");
                    tmp.add(new Object[]{pid, cnt});
                    if (cnt > maxCnt) maxCnt = cnt;
                }
                for (Object[] row : tmp) scores.put((UUID) row[0], (double) row[1] / maxCnt);
            } catch (SQLException e) { e.printStackTrace(); }
        } else {
            double maxFreq = ratings.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
            for (Map.Entry<UUID, Double> e : ratings.entrySet()) {
                scores.put(e.getKey(), e.getValue() / maxFreq);
            }
        }
        return scores;
    }

    // ═══════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════

    private List<Products> getFallbackCrossSelling(UUID productId) {
        String sql = "SELECT p.* FROM products p WHERE p.category_id = " +
                     "(SELECT category_id FROM products WHERE id = ?) " +
                     "AND p.id != ? AND p.is_available = true LIMIT 3";
        List<Products> result = new ArrayList<>();
        try {
            Connection conn = DAO.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, productId);
            ps.setObject(2, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) result.add(mapProduct(rs));
        } catch (SQLException e) { e.printStackTrace(); }

        if (result.size() < 3) {
            for (Products p : getBestSellers()) {
                if (!result.contains(p) && !p.getId().equals(productId) && result.size() < 3) result.add(p);
            }
        }
        return result;
    }

    private List<Products> loadProductsByIds(List<UUID> ids) {
        if (ids.isEmpty()) return new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE id IN (");
        for (int i = 0; i < ids.size(); i++) sql.append(i > 0 ? ",?" : "?");
        sql.append(") AND is_available = true");

        List<Products> result = new ArrayList<>();
        try {
            Connection conn = DAO.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < ids.size(); i++) ps.setObject(i + 1, ids.get(i));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) result.add(mapProduct(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    private List<Products> loadAllProducts() {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id WHERE p.is_available = true";
        List<Products> result = new ArrayList<>();
        try {
            ResultSet rs = DAO.getConnection().prepareStatement(sql).executeQuery();
            while (rs.next()) result.add(mapProduct(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    private Products findProductByName(String name) {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id WHERE p.name = ? AND p.is_available = true";
        try {
            Connection conn = DAO.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapProduct(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private Products mapProduct(ResultSet rs) throws SQLException {
        Products p = new Products();
        p.setId(UUID.fromString(rs.getString("id")));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setBasePrice(rs.getBigDecimal("base_price"));
        String catId = rs.getString("category_id");
        if (catId != null) p.setCategoryId(UUID.fromString(catId));
        p.setAvailable(rs.getBoolean("is_available"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        try { p.setImagePath(rs.getString("image_path")); } catch (SQLException ignored) {}
        try { p.setCategoryName(rs.getString("category_name")); } catch (SQLException ignored) {}
        return p;
    }

    /** Invalidate cache — gọi sau khi đặt hàng. */
    public void invalidateCache() {
        matrixLastBuilt = 0;
    }
}

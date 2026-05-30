package com.coffeeshop.view;

import com.coffeeshop.dao.CartDAO;
import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.model.CartItems;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * ProductDetailFrm - Giao diện xem chi tiết sản phẩm và tùy chọn.
 * Hiển thị thông tin sản phẩm, tùy chọn (số lượng, mức đá, đường, ghi chú),
 * nút "Thêm vào giỏ hàng".
 */
public class ProductDetailFrm extends JFrame implements ActionListener {

    private JLabel lblName, lblPrice, lblDescription, lblCategory;
    private JSpinner spnQuantity;
    private JComboBox<String> cmbSugar, cmbIce;
    private JTextField txtNote;
    private JButton btnAddToCart, btnBack;
    private final Users currentUser;
    private Products product;
    private final ProductDAO productDAO = new ProductDAO();
    private final CartDAO cartDAO = new CartDAO();

    public ProductDetailFrm(Users user, UUID productId) {
        this.currentUser = user;
        initComponents();
        loadProductDetail(productId);
    }

    private void initComponents() {
        setTitle("Chi tiết sản phẩm");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("CHI TIẾT SẢN PHẨM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(101, 67, 33));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        btnBack = new JButton("Quay lại");
        btnBack.addActionListener(this);
        headerPanel.add(btnBack, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Detail panel
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Tên sản phẩm
        gbc.gridx = 0; gbc.gridy = row;
        detailPanel.add(createLabel("Tên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        lblName = new JLabel();
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        detailPanel.add(lblName, gbc);

        // Danh mục
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Danh mục:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        lblCategory = new JLabel();
        detailPanel.add(lblCategory, gbc);

        // Giá
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Giá:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        lblPrice = new JLabel();
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPrice.setForeground(new Color(200, 50, 50));
        detailPanel.add(lblPrice, gbc);

        // Mô tả
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        lblDescription = new JLabel();
        detailPanel.add(lblDescription, gbc);

        // Số lượng
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Số lượng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        spnQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spnQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailPanel.add(spnQuantity, gbc);



        // Mức đường
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Mức đường:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbSugar = new JComboBox<>(new String[]{"100% đường", "70% đường", "50% đường", "30% đường", "Không đường"});
        cmbSugar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailPanel.add(cmbSugar, gbc);

        // Mức đá
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Mức đá:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbIce = new JComboBox<>(new String[]{"100% đá", "70% đá", "50% đá", "Ít đá", "Không đá"});
        cmbIce.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailPanel.add(cmbIce, gbc);

        // Ghi chú
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNote = new JTextField();
        txtNote.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailPanel.add(txtNote, gbc);

        mainPanel.add(detailPanel, BorderLayout.CENTER);

        // Button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnAddToCart = new JButton("Thêm vào giỏ hàng");
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddToCart.setBackground(new Color(101, 67, 33));
        btnAddToCart.setForeground(Color.BLACK);
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.setPreferredSize(new Dimension(200, 40));
        btnAddToCart.addActionListener(this);
        btnPanel.add(btnAddToCart);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    /**
     * Tải chi tiết sản phẩm.
     * Luồng MD: ProductDetailFrm() gọi lớp products → product_DAO.getProductDetail()
     */
    private void loadProductDetail(UUID productId) {
        // Gọi phương thức getProductDetail() của lớp product_DAO
        product = productDAO.getProductDetail(productId);
        if (product != null) {
            lblName.setText(product.getName());
            lblCategory.setText(product.getCategoryName() != null ? product.getCategoryName() : "");
            lblPrice.setText(String.format("%,.0f VND", product.getBasePrice()));
            lblDescription.setText(product.getDescription() != null ? product.getDescription() : "");
        }
    }

    /**
     * actionPerformed() - Xử lý sự kiện thêm vào giỏ hàng.
     * Luồng MD:
     * 1. Gọi lớp cart_items để đóng gói thông tin mặt hàng.
     * 2. Gọi phương thức addCartItem() của lớp cart_DAO.
     * 3. Gọi lớp CartFrm.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddToCart && product != null) {
            int qty = (Integer) spnQuantity.getValue();
            String sugar = (String) cmbSugar.getSelectedItem();
            String ice = (String) cmbIce.getSelectedItem();
            String note = txtNote.getText().trim();

            String options = "Đường: " + sugar + ", Đá: " + ice;
            if (!note.isEmpty()) {
                options += ", Ghi chú: " + note;
            }

            CartItems cartItem = new CartItems();
            cartItem.setProductId(product.getId());
            cartItem.setProductName(product.getName());
            cartItem.setUnitPrice(product.getBasePrice().doubleValue());
            cartItem.setQuantity(qty);
            cartItem.setOptions(options);

            // Gọi phương thức addCartItem(cartItem, userId) của lớp cart_DAO
            cartDAO.addCartItem(cartItem, currentUser.getId());

            JOptionPane.showMessageDialog(this,
                "Đã thêm \"" + product.getName() + "\" vào giỏ hàng!",
                "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Gọi lớp CartFrm
            this.dispose();
            CartFrm cartFrm = new CartFrm(currentUser);
            cartFrm.setVisible(true);

        } else if (e.getSource() == btnBack) {
            this.dispose();
            MenuFrm menuFrm = new MenuFrm(currentUser);
            menuFrm.setVisible(true);
        }
    }
}

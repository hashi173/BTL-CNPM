package com.coffeeshop.view;

import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * MenuFrm - Giao diện xem danh sách thực đơn.
 * Hiển thị danh sách sản phẩm dạng bảng, click vào sản phẩm → ProductDetailFrm.
 */
public class MenuFrm extends JFrame implements ActionListener {

    private JTable tblProducts;
    private DefaultTableModel tableModel;
    private JButton btnCart, btnBack, btnSearch;
    private JTextField txtSearch;
    private final Users currentUser;
    private final ProductDAO productDAO = new ProductDAO();
    private List<Products> productList;

    public MenuFrm(Users user) {
        this.currentUser = user;
        initComponents();
        loadProducts("");
    }

    private void initComponents() {
        setTitle("Thực đơn - Quán cà phê");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("THỰC ĐƠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(101, 67, 33));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);
        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSearch.addActionListener(this);
        btnCart = new JButton("Giỏ hàng");
        btnCart.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCart.addActionListener(this);
        btnBack = new JButton("Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.addActionListener(this);
        headerRight.add(new JLabel("Tìm đồ:"));
        headerRight.add(txtSearch);
        headerRight.add(btnSearch);
        headerRight.add(btnCart);
        headerRight.add(btnBack);
        headerPanel.add(headerRight, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"STT", "Tên sản phẩm", "Danh mục", "Giá (VND)", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProducts = new JTable(tableModel);
        tblProducts.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblProducts.setRowHeight(30);
        tblProducts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Double-click → ProductDetailFrm
        tblProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblProducts.getSelectedRow();
                    if (row >= 0 && row < productList.size()) {
                        Products selected = productList.get(row);
                        // Gọi lớp ProductDetailFrm
                        dispose();
                        ProductDetailFrm detailFrm = new ProductDetailFrm(currentUser, selected.getId());
                        detailFrm.setVisible(true);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblProducts);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JLabel lblHint = new JLabel("Double-click vào sản phẩm để xem chi tiết và thêm vào giỏ hàng");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblHint.setForeground(Color.GRAY);
        mainPanel.add(lblHint, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Tải danh sách sản phẩm.
     * Luồng MD: MenuFrm() gọi lớp products → product_DAO.getAllProducts()
     */
    private void loadProducts(String keyword) {
        tableModel.setRowCount(0);
        // Gọi phương thức getAllProducts() của lớp product_DAO
        if (keyword != null && !keyword.trim().isEmpty()) {
            productList = productDAO.searchProduct(keyword);
        } else {
            productList = productDAO.getAllProducts();
        }
        int stt = 1;
        for (Products p : productList) {
            tableModel.addRow(new Object[] {
                    stt++,
                    p.getName(),
                    p.getCategoryName() != null ? p.getCategoryName() : "",
                    String.format("%,.0f", p.getBasePrice()),
                    p.isAvailable() ? "Còn hàng" : "Hết hàng"
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            this.dispose();
            HomeFrm home = new HomeFrm(currentUser);
            home.setVisible(true);
        } else if (e.getSource() == btnCart) {
            this.dispose();
            CartFrm cartFrm = new CartFrm(currentUser);
            cartFrm.setVisible(true);
        } else if (e.getSource() == btnSearch) {
            loadProducts(txtSearch.getText().trim());
        }
    }
}

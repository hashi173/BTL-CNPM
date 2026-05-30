package com.coffeeshop.view;

import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * ManageProductFrm - Giao diện quản lý sản phẩm.
 * Hiển thị danh sách sản phẩm, ô tìm kiếm, nút Chỉnh sửa/Xóa.
 */
public class ManageProductFrm extends JFrame implements ActionListener {

    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnBack;
    private JTable tblProducts;
    private DefaultTableModel tableModel;
    private final Users currentAdmin;
    private final ProductDAO productDAO = new ProductDAO();
    private List<Products> productList;

    public ManageProductFrm(Users admin) {
        this.currentAdmin = admin;
        initComponents();
        loadAllProducts();
    }

    private void initComponents() {
        setTitle("Quản lý sản phẩm");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Header - Search & Back
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSearch = new JButton("Tìm kiếm");
        btnSearch.addActionListener(this);
        btnBack = new JButton("Trang chủ");
        btnBack.addActionListener(this);
        searchPanel.add(new JLabel("Tìm kiếm (Tên/ID):"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnBack);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Tên sản phẩm", "Danh mục", "Giá", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProducts = new JTable(tableModel);
        tblProducts.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblProducts.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(tblProducts);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer - Buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAdd = createButton("Thêm sản phẩm", new Color(39, 174, 96));
        btnEdit = createButton("Chỉnh sửa", new Color(243, 156, 18));
        btnDelete = createButton("Xóa / Khóa", new Color(192, 57, 43));
        footerPanel.add(btnAdd);
        footerPanel.add(btnEdit);
        footerPanel.add(btnDelete);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        return btn;
    }

    private void loadAllProducts() {
        productList = productDAO.getAllProductsAdmin();
        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Products p : productList) {
            tableModel.addRow(new Object[] {
                    p.getName(),
                    p.getCategoryName() != null ? p.getCategoryName() : "",
                    String.format("%,.0f", p.getBasePrice()),
                    p.isAvailable() ? "Hoạt động" : "Bị khóa"
            });
        }
    }

    /**
     * actionPerformed()
     * Luồng MD:
     * - Tìm kiếm → gọi searchProduct() của productDAO.
     * - Chọn sửa → gọi EditProductFrm.
     * - Chọn thêm → gọi EditProductFrm (chế độ thêm).
     * - Chọn xóa → gọi deleteProduct() của productDAO.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadAllProducts();
            } else {
                productList = productDAO.searchProduct(keyword);
                updateTable();
            }
        } else if (e.getSource() == btnEdit) {
            int row = tblProducts.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để chỉnh sửa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Products selectedProduct = productList.get(row);
            this.dispose();
            EditProductFrm editFrm = new EditProductFrm(currentAdmin, selectedProduct.getId());
            editFrm.setVisible(true);
        } else if (e.getSource() == btnAdd) {
            this.dispose();
            EditProductFrm editFrm = new EditProductFrm(currentAdmin, null); // null ID for add mode
            editFrm.setVisible(true);
        } else if (e.getSource() == btnDelete) {
            int row = tblProducts.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa/khóa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Products selectedProduct = productList.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa/khóa sản phẩm: " + selectedProduct.getName() + "?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = productDAO.deleteProduct(selectedProduct.getId());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Đã xử lý thành công!");
                    loadAllProducts();
                } else {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xóa/khóa sản phẩm!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == btnBack) {
            this.dispose();
            AdminHomeFrm adminHome = new AdminHomeFrm(currentAdmin);
            adminHome.setVisible(true);
        }
    }
}

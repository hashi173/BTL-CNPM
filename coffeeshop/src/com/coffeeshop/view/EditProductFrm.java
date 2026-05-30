package com.coffeeshop.view;

import com.coffeeshop.dao.CategoryDAO;
import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.model.Categories;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * EditProductFrm - Giao diện thêm/sửa sản phẩm.
 * Dùng cho cả chức năng "Chỉnh sửa giá sản phẩm" (theo MD).
 */
public class EditProductFrm extends JFrame implements ActionListener {

    private JTextField txtName, txtPrice;
    private JTextArea txtDesc;
    private JComboBox<CategoryItem> cmbCategory;
    private JCheckBox chkAvailable;
    private JButton btnSave, btnBack;
    
    private final Users currentAdmin;
    private final UUID productId;
    private final boolean isAddMode;
    private Products product;
    
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    // Helper class cho JComboBox
    private static class CategoryItem {
        UUID id;
        String name;
        CategoryItem(UUID id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }

    public EditProductFrm(Users admin, UUID productId) {
        this.currentAdmin = admin;
        this.productId = productId;
        this.isAddMode = (productId == null);
        initComponents();
        loadCategories();
        if (!isAddMode) {
            loadProductData();
        }
    }

    private void initComponents() {
        setTitle(isAddMode ? "Thêm sản phẩm mới" : "Chỉnh sửa sản phẩm");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(isAddMode ? "THÊM SẢN PHẨM" : "CHỈNH SỬA SẢN PHẨM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        // Tên SP
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Tên SP (*):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtName = new JTextField();
        formPanel.add(txtName, gbc);

        // Danh mục
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Danh mục:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbCategory = new JComboBox<>();
        formPanel.add(cmbCategory, gbc);

        // Giá
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Giá cơ bản (*):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPrice = new JTextField();
        formPanel.add(txtPrice, gbc);

        // Mô tả
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDesc = new JTextArea(3, 20);
        txtDesc.setLineWrap(true);
        formPanel.add(new JScrollPane(txtDesc), gbc);

        // Trạng thái
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        chkAvailable = new JCheckBox("Đang hoạt động", true);
        formPanel.add(chkAvailable, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnBack = new JButton("Quay lại");
        btnBack.addActionListener(this);
        btnSave = new JButton("Lưu thay đổi");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setBackground(new Color(41, 128, 185));
        btnSave.setForeground(Color.BLACK);
        btnSave.addActionListener(this);
        btnPanel.add(btnBack);
        btnPanel.add(btnSave);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadCategories() {
        List<Categories> cats = categoryDAO.getAllCategories();
        for (Categories c : cats) {
            cmbCategory.addItem(new CategoryItem(c.getId(), c.getName()));
        }
    }

    private void loadProductData() {
        product = productDAO.getProductDetail(productId);
        if (product != null) {
            txtName.setText(product.getName());
            txtPrice.setText(product.getBasePrice() != null ? product.getBasePrice().toString() : "");
            txtDesc.setText(product.getDescription());
            chkAvailable.setSelected(product.isAvailable());
            
            // Set category
            if (product.getCategoryId() != null) {
                for (int i = 0; i < cmbCategory.getItemCount(); i++) {
                    CategoryItem item = cmbCategory.getItemAt(i);
                    if (item.id.equals(product.getCategoryId())) {
                        cmbCategory.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    /**
     * actionPerformed()
     * Luồng MD (UpdatePrice):
     * 1. Gọi products đóng gói -> product_DAO.updatePrice() (hoặc updateProduct/addProduct).
     * 2. Gọi lại ManageProductFrm.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            try {
                String name = txtName.getText().trim();
                String priceStr = txtPrice.getText().trim();
                
                if (name.isEmpty() || priceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và giá sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                BigDecimal price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "Giá không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Products p = isAddMode ? new Products() : product;
                p.setName(name);
                p.setBasePrice(price);
                p.setDescription(txtDesc.getText().trim());
                p.setAvailable(chkAvailable.isSelected());
                
                CategoryItem selectedCat = (CategoryItem) cmbCategory.getSelectedItem();
                if (selectedCat != null) p.setCategoryId(selectedCat.id);

                boolean success;
                if (isAddMode) {
                    success = productDAO.addProduct(p);
                } else {
                    success = productDAO.updateProduct(p);
                }

                if (success) {
                    JOptionPane.showMessageDialog(this, isAddMode ? "Thêm thành công!" : "Cập nhật thành công!");
                    this.dispose();
                    new ManageProductFrm(currentAdmin).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá phải là một số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnBack) {
            this.dispose();
            new ManageProductFrm(currentAdmin).setVisible(true);
        }
    }
}

package com.coffeeshop.view;

import com.coffeeshop.dao.CategoryDAO;
import com.coffeeshop.model.Categories;
import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * AddCategoryFrm - Giao diện thêm (và sửa) danh mục.
 * Dựa theo thiết kế chi tiết: AddCategoryView -> addCategory() in Categories.
 */
public class AddCategoryFrm extends JFrame implements ActionListener {

    private JTextField txtName;
    private JTextArea txtDesc;
    private JButton btnSave, btnBack;

    private final Users currentAdmin;
    private final UUID categoryId;
    private final boolean isAddMode;
    private Categories category;
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public AddCategoryFrm(Users admin, UUID categoryId) {
        this.currentAdmin = admin;
        this.categoryId = categoryId;
        this.isAddMode = (categoryId == null);
        initComponents();
        if (!isAddMode) loadData();
    }

    private void initComponents() {
        setTitle(isAddMode ? "Thêm danh mục" : "Chỉnh sửa danh mục");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(isAddMode ? "THÊM DANH MỤC" : "CHỈNH SỬA DANH MỤC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Tên danh mục (*):"), gbc);
        gbc.gridx = 1; txtName = new JTextField(15); formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; 
        txtDesc = new JTextArea(4, 15);
        txtDesc.setLineWrap(true);
        formPanel.add(new JScrollPane(txtDesc), gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnBack = new JButton("Quay lại");
        btnSave = new JButton("Lưu thay đổi");
        btnBack.addActionListener(this);
        btnSave.addActionListener(this);
        btnPanel.add(btnBack);
        btnPanel.add(btnSave);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadData() {
        category = categoryDAO.getCategoryById(categoryId);
        if (category != null) {
            txtName.setText(category.getName());
            txtDesc.setText(category.getDescription());
        }
    }

    /**
     * actionPerformed()
     * Luồng MD: Click lưu -> categoryDAO.addCategory() -> ManageCategoryFrm
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên danh mục không được để trống!");
                return;
            }

            Categories c = isAddMode ? new Categories() : category;
            c.setName(name);
            c.setDescription(txtDesc.getText().trim());

            boolean success = isAddMode ? categoryDAO.addCategory(c) : categoryDAO.updateCategory(c);
            if (success) {
                JOptionPane.showMessageDialog(this, isAddMode ? "Thêm thành công!" : "Cập nhật thành công!");
                this.dispose();
                new ManageCategoryFrm(currentAdmin).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu danh mục!");
            }
        } else if (e.getSource() == btnBack) {
            this.dispose();
            new ManageCategoryFrm(currentAdmin).setVisible(true);
        }
    }
}

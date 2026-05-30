package com.coffeeshop.view;

import com.coffeeshop.dao.CategoryDAO;
import com.coffeeshop.model.Categories;
import com.coffeeshop.model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * ManageCategoryFrm - Giao diện quản lý danh mục.
 * Theo MD: Admin click nút quản lý danh mục -> ManageCategoryView.
 */
public class ManageCategoryFrm extends JFrame implements ActionListener {

    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnBack;
    private JTable tblCategories;
    private DefaultTableModel tableModel;
    private final Users currentAdmin;
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private List<Categories> categoryList;

    public ManageCategoryFrm(Users admin) {
        this.currentAdmin = admin;
        initComponents();
        loadAllCategories();
    }

    private void initComponents() {
        setTitle("Quản lý danh mục");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtSearch = new JTextField(15);
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
        String[] columns = {"STT", "ID", "Tên danh mục", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblCategories = new JTable(tableModel);
        tblCategories.setRowHeight(25);
        mainPanel.add(new JScrollPane(tblCategories), BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAdd = new JButton("Thêm danh mục");
        btnEdit = new JButton("Chỉnh sửa");
        btnDelete = new JButton("Xóa");
        
        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        
        footerPanel.add(btnAdd);
        footerPanel.add(btnEdit);
        footerPanel.add(btnDelete);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadAllCategories() {
        categoryList = categoryDAO.getAllCategories();
        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        int stt = 1;
        for (Categories c : categoryList) {
            tableModel.addRow(new Object[]{
                stt++,
                c.getId(),
                c.getName(),
                c.getDescription()
            });
        }
    }

    /**
     * actionPerformed()
     * Luồng MD: Click thêm danh mục -> gọi AddCategoryFrm.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            String kw = txtSearch.getText().trim();
            if (kw.isEmpty()) loadAllCategories();
            else {
                categoryList = categoryDAO.searchCategories(kw);
                updateTable();
            }
        } else if (e.getSource() == btnAdd) {
            this.dispose();
            AddCategoryFrm addFrm = new AddCategoryFrm(currentAdmin, null); // Add mode
            addFrm.setVisible(true);
        } else if (e.getSource() == btnEdit) {
            int row = tblCategories.getSelectedRow();
            if (row >= 0) {
                this.dispose();
                AddCategoryFrm addFrm = new AddCategoryFrm(currentAdmin, categoryList.get(row).getId()); // Edit mode
                addFrm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Chọn danh mục cần sửa!");
            }
        } else if (e.getSource() == btnDelete) {
            int row = tblCategories.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (categoryDAO.deleteCategory(categoryList.get(row).getId())) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        loadAllCategories();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể xóa danh mục đang có sản phẩm!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chọn danh mục cần xóa!");
            }
        } else if (e.getSource() == btnBack) {
            this.dispose();
            new AdminHomeFrm(currentAdmin).setVisible(true);
        }
    }
}

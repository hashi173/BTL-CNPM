package com.coffeeshop.view;

import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AdminHomeFrm - Giao diện chính của quản trị viên (Admin).
 * Có các nút: Quản lý sản phẩm, Quản lý danh mục, Quản lý đơn hàng, Thống kê.
 */
public class AdminHomeFrm extends JFrame implements ActionListener {

    private JButton btnManageProduct;
    private JButton btnManageCategory;
    private JButton btnManageOrder;
    private JButton btnStat;
    private JButton btnLogout;
    private final Users currentAdmin;

    public AdminHomeFrm(Users admin) {
        this.currentAdmin = admin;
        initComponents();
    }

    private void initComponents() {
        setTitle("Trang quản trị - Quản lý quán cà phê");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblWelcome = new JLabel("Bảng điều khiển - Xin chào, Admin " + currentAdmin.getFullName());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWelcome.setForeground(new Color(50, 50, 200));
        headerPanel.add(lblWelcome, BorderLayout.WEST);

        btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLogout.addActionListener(this);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center - buttons
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        btnManageProduct = createNavButton("Quản lý sản phẩm", new Color(41, 128, 185));
        btnManageCategory = createNavButton("Quản lý danh mục", new Color(39, 174, 96));
        btnManageOrder = createNavButton("Quản lý đơn hàng", new Color(142, 68, 173));
        btnStat = createNavButton("Xem thống kê", new Color(211, 84, 0));

        centerPanel.add(btnManageProduct);
        centerPanel.add(btnManageCategory);
        centerPanel.add(btnManageOrder);
        centerPanel.add(btnStat);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton createNavButton(String text, Color bgColor) {
        JButton btn = new JButton("<html><center>" + text + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        return btn;
    }

    /**
     * actionPerformed() - Xử lý điều hướng.
     * Theo luồng MD:
     * - Click Quản lý sản phẩm → gọi ManageProductFrm
     * - Click Quản lý danh mục → gọi ManageCategoryFrm
     * - Click Quản lý đơn hàng → gọi OrderManagementFrm
     * - Click Thống kê → gọi StatFrm
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnManageProduct) {
            // Gọi lớp ManageProductFrm
            this.dispose();
            ManageProductFrm manageProductFrm = new ManageProductFrm(currentAdmin);
            manageProductFrm.setVisible(true);
        } else if (e.getSource() == btnManageCategory) {
            // Gọi lớp ManageCategoryFrm
            this.dispose();
            ManageCategoryFrm manageCategoryFrm = new ManageCategoryFrm(currentAdmin);
            manageCategoryFrm.setVisible(true);
        } else if (e.getSource() == btnManageOrder) {
            // Gọi lớp OrderManagementFrm
            this.dispose();
            OrderManagementFrm orderManagementFrm = new OrderManagementFrm(currentAdmin);
            orderManagementFrm.setVisible(true);
        } else if (e.getSource() == btnStat) {
            // Gọi lớp StatFrm
            this.dispose();
            StatFrm statFrm = new StatFrm(currentAdmin);
            statFrm.setVisible(true);
        } else if (e.getSource() == btnLogout) {
            this.dispose();
            LoginFrm loginFrm = new LoginFrm();
            loginFrm.setVisible(true);
        }
    }
}

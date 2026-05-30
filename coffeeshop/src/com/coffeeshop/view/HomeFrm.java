package com.coffeeshop.view;

import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * HomeFrm - Giao diện chính của khách hàng.
 * Có các nút: Xem thực đơn (Menu), Giỏ hàng (Cart), Đơn hàng của tôi.
 */
public class HomeFrm extends JFrame implements ActionListener {

    private JButton btnMenu;
    private JButton btnCart;
    private JButton btnMyOrders;
    private JButton btnLogout;
    private final Users currentUser;

    public HomeFrm(Users user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Trang chủ - Quán cà phê");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblWelcome = new JLabel("Xin chào, " + currentUser.getFullName() + "!");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWelcome.setForeground(new Color(101, 67, 33));
        headerPanel.add(lblWelcome, BorderLayout.WEST);

        btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLogout.addActionListener(this);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center - buttons
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        btnMenu = createNavButton("Xem thực đơn", "fas fa-mug-hot");
        btnCart = createNavButton("Giỏ hàng", "fas fa-shopping-cart");
        btnMyOrders = createNavButton("Đơn hàng của tôi", "fas fa-box");

        centerPanel.add(btnMenu);
        centerPanel.add(btnCart);
        centerPanel.add(btnMyOrders);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton createNavButton(String text, String icon) {
        JButton btn = new JButton("<html><center>" + text + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(240, 240, 240));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 120));
        btn.addActionListener(this);
        return btn;
    }

    /**
     * actionPerformed() - Xử lý điều hướng.
     * Theo luồng MD:
     * - Click Menu → gọi MenuFrm
     * - Click Cart → gọi CartFrm
     * - Click Đơn hàng → gọi OrderListFrm
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMenu) {
            // Gọi lớp MenuFrm
            this.dispose();
            MenuFrm menuFrm = new MenuFrm(currentUser);
            menuFrm.setVisible(true);
        } else if (e.getSource() == btnCart) {
            // Gọi lớp CartFrm
            this.dispose();
            CartFrm cartFrm = new CartFrm(currentUser);
            cartFrm.setVisible(true);
        } else if (e.getSource() == btnMyOrders) {
            // Gọi lớp OrderListFrm
            this.dispose();
            OrderListFrm orderListFrm = new OrderListFrm(currentUser);
            orderListFrm.setVisible(true);
        } else if (e.getSource() == btnLogout) {
            this.dispose();
            LoginFrm loginFrm = new LoginFrm();
            loginFrm.setVisible(true);
        }
    }
}

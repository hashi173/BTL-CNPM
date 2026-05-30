package com.coffeeshop.view;

import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * OrderView - Giao diện hiển thị mã tracking code sau khi đặt hàng thành công.
 * Hiển thị mã đơn hàng và các nút điều hướng.
 */
public class OrderView extends JFrame implements ActionListener {

    private JLabel lblTrackingCode;
    private JButton btnTrackOrder, btnHome;
    private final Users currentUser;
    private final String trackingCode;

    public OrderView(Users user, String trackingCode) {
        this.currentUser = user;
        this.trackingCode = trackingCode;
        initComponents();
    }

    private void initComponents() {
        setTitle("Đặt hàng thành công");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Header
        JLabel lblTitle = new JLabel("ĐẶT HÀNG THÀNH CÔNG!");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(34, 139, 34));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Center - Tracking Code
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel lblCodeLabel = new JLabel("Mã đơn hàng của bạn:");
        lblCodeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        centerPanel.add(lblCodeLabel, gbc);

        gbc.gridy = 1;
        lblTrackingCode = new JLabel(trackingCode);
        lblTrackingCode.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTrackingCode.setForeground(new Color(101, 67, 33));
        centerPanel.add(lblTrackingCode, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        JLabel lblNote = new JLabel("Vui lòng lưu lại mã đơn hàng để theo dõi.");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNote.setForeground(Color.GRAY);
        centerPanel.add(lblNote, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        btnTrackOrder = new JButton("Theo dõi đơn hàng");
        btnTrackOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTrackOrder.setBackground(new Color(101, 67, 33));
        btnTrackOrder.setForeground(Color.BLACK);
        btnTrackOrder.setFocusPainted(false);
        btnTrackOrder.addActionListener(this);

        btnHome = new JButton("Về trang chủ");
        btnHome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnHome.addActionListener(this);

        btnPanel.add(btnTrackOrder);
        btnPanel.add(btnHome);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTrackOrder) {
            this.dispose();
            OrderListFrm orderList = new OrderListFrm(currentUser);
            orderList.setVisible(true);
        } else if (e.getSource() == btnHome) {
            this.dispose();
            HomeFrm home = new HomeFrm(currentUser);
            home.setVisible(true);
        }
    }
}

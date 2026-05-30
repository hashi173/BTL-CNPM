package com.coffeeshop.view;

import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * OrderCancelledFrm - Giao diện hiển thị kết quả hủy đơn hàng thành công.
 * Hiển thị mã đơn hàng và trạng thái "Đã hủy".
 */
public class OrderCancelledFrm extends JFrame implements ActionListener {

    private JLabel lblTrackingCode, lblStatus;
    private JButton btnOrderList, btnHome;
    private final Users currentUser;
    private final String trackingCode;

    public OrderCancelledFrm(Users user, String trackingCode) {
        this.currentUser = user;
        this.trackingCode = trackingCode;
        initComponents();
    }

    private void initComponents() {
        setTitle("Hủy đơn hàng thành công");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Header
        JLabel lblTitle = new JLabel("ĐÃ HỦY ĐƠN HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(200, 50, 50));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Center - Order Info
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel lblCodeLabel = new JLabel("Mã đơn hàng:");
        lblCodeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        centerPanel.add(lblCodeLabel, gbc);

        gbc.gridy = 1;
        lblTrackingCode = new JLabel(trackingCode);
        lblTrackingCode.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTrackingCode.setForeground(new Color(200, 50, 50));
        centerPanel.add(lblTrackingCode, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(15, 0, 0, 0);
        lblStatus = new JLabel("Trạng thái: ĐÃ HỦY");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatus.setForeground(new Color(200, 50, 50));
        centerPanel.add(lblStatus, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        btnOrderList = new JButton("Xem đơn hàng của tôi");
        btnOrderList.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOrderList.setBackground(new Color(101, 67, 33));
        btnOrderList.setForeground(Color.BLACK);
        btnOrderList.setFocusPainted(false);
        btnOrderList.addActionListener(this);

        btnHome = new JButton("Về trang chủ");
        btnHome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnHome.addActionListener(this);

        btnPanel.add(btnOrderList);
        btnPanel.add(btnHome);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOrderList) {
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

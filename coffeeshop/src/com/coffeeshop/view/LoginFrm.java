package com.coffeeshop.view;

import com.coffeeshop.dao.UserDAO;
import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrm - Giao diện đăng nhập hệ thống.
 * Có 2 ô nhập username, password và nút Login.
 */
public class LoginFrm extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    private JLabel lblMessage;

    private final UserDAO userDAO = new UserDAO();

    public LoginFrm() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Đăng nhập - Quản lý quán cà phê");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Title
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(101, 67, 33));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblUser, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblPass, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(txtPassword, gbc);

        // Message label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        lblMessage = new JLabel(" ", SwingConstants.CENTER);
        lblMessage.setForeground(Color.RED);
        lblMessage.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        formPanel.add(lblMessage, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        btnPanel.setOpaque(false);
        
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(101, 67, 33));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this);
        
        btnRegister = new JButton("Chưa có tài khoản? Đăng ký");
        btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRegister.setBackground(new Color(230, 230, 230));
        btnRegister.setForeground(Color.BLACK);
        btnRegister.setFocusPainted(false);
        btnRegister.addActionListener(this);
        
        btnPanel.add(btnLogin);
        btnPanel.add(btnRegister);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // Enter key for login
        getRootPane().setDefaultButton(btnLogin);

        add(mainPanel);
    }

    /**
     * actionPerformed() - Xử lý sự kiện đăng nhập.
     * Theo luồng MD:
     * 1. Gọi lớp users để đóng gói thông tin đăng nhập.
     * 2. Gọi phương thức checkLogin() của lớp user_DAO.
     * 3. Nếu thành công → gọi HomeFrm (client) hoặc AdminHomeFrm (admin).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                lblMessage.setText("Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // Gọi lớp users để đóng gói thông tin đăng nhập
            Users user = new Users();
            user.setUsername(username);
            user.setPassword(password);

            // Gọi phương thức checkLogin() của lớp user_DAO
            Users loggedInUser = userDAO.checkLogin(username, password);

            if (loggedInUser != null) {
                // Đăng nhập thành công
                this.dispose();

                if ("ADMIN".equals(loggedInUser.getRole())) {
                    // Gọi lớp AdminHomeFrm
                    AdminHomeFrm adminHome = new AdminHomeFrm(loggedInUser);
                    adminHome.setVisible(true);
                } else {
                    // Gọi lớp HomeFrm
                    HomeFrm home = new HomeFrm(loggedInUser);
                    home.setVisible(true);
                }
            } else {
                lblMessage.setText("Tên đăng nhập hoặc mật khẩu không đúng!");
                txtPassword.setText("");
            }
        } else if (e.getSource() == btnRegister) {
            this.dispose();
            new RegisterFrm().setVisible(true);
        }
    }
}

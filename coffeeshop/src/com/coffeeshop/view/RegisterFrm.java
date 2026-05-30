package com.coffeeshop.view;

import com.coffeeshop.dao.UserDAO;
import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrm extends JFrame implements ActionListener {

    private JTextField txtUsername, txtFullName, txtEmail, txtPhone;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnRegister, btnLogin;
    private final UserDAO userDAO = new UserDAO();

    public RegisterFrm() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Đăng ký - Quán cà phê");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250, 248, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Header
        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(101, 67, 33));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        formPanel.add(createLabel("Tên đăng nhập (*):"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(createLabel("Mật khẩu (*):"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        formPanel.add(createLabel("Nhập lại MK (*):"));
        txtConfirmPassword = new JPasswordField();
        formPanel.add(txtConfirmPassword);

        formPanel.add(createLabel("Họ tên (*):"));
        txtFullName = new JTextField();
        formPanel.add(txtFullName);

        formPanel.add(createLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(createLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        btnPanel.setOpaque(false);

        btnRegister = new JButton("Đăng ký");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setBackground(new Color(39, 174, 96));
        btnRegister.setForeground(Color.BLACK);
        btnRegister.setFocusPainted(false);
        btnRegister.addActionListener(this);

        btnLogin = new JButton("Đã có tài khoản? Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLogin.setBackground(new Color(230, 230, 230));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this);

        btnPanel.add(btnRegister);
        btnPanel.add(btnLogin);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            this.dispose();
            new LoginFrm().setVisible(true);
        } else if (e.getSource() == btnRegister) {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirm = new String(txtConfirmPassword.getPassword());
            String fullName = txtFullName.getText().trim();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Tên đăng nhập, Mật khẩu và Họ tên!");
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không khớp!");
                return;
            }

            if (userDAO.checkUsernameExist(username)) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác!");
                return;
            }

            Users newUser = new Users();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setFullName(fullName);
            newUser.setEmail(txtEmail.getText().trim());
            newUser.setPhone(txtPhone.getText().trim());

            if (userDAO.registerUser(newUser)) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.");
                this.dispose();
                new LoginFrm().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Đăng ký thất bại, vui lòng thử lại sau!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

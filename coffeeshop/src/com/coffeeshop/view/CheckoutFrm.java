package com.coffeeshop.view;

import com.coffeeshop.dao.CartDAO;
import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.dao.OrderItemDAO;
import com.coffeeshop.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * CheckoutFrm - Giao diện thanh toán.
 * Có các ô nhập: tên khách hàng, SĐT, địa chỉ, ghi chú, nút xác nhận đặt hàng.
 */
public class CheckoutFrm extends JFrame implements ActionListener {

    private JTextField txtName, txtPhone, txtAddress, txtNote;
    private JLabel lblTotal;
    private JButton btnConfirm, btnBack;
    private final Users currentUser;
    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

    public CheckoutFrm(Users user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Thanh toán - Quán cà phê");
        setSize(500, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Header
        JLabel lblTitle = new JLabel("XÁC NHẬN ĐẶT HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(101, 67, 33));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Tên khách hàng
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Tên người nhận:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtName = new JTextField(currentUser.getFullName());
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(txtName, gbc);

        // SĐT
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPhone = new JTextField(currentUser.getPhone() != null ? currentUser.getPhone() : "");
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(txtPhone, gbc);

        // Địa chỉ
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Địa chỉ nhận hàng (*):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtAddress = new JTextField();
        txtAddress.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(txtAddress, gbc);

        // Ghi chú
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNote = new JTextField();
        txtNote.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(txtNote, gbc);

        // Tổng tiền
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        double total = cartDAO.getTotalAmount(currentUser.getId());
        lblTotal = new JLabel("Tổng thanh toán: " + String.format("%,.0f", total) + " VND");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(new Color(200, 50, 50));
        formPanel.add(lblTotal, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setOpaque(false);
        btnBack = new JButton("Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.addActionListener(this);
        btnConfirm = new JButton("Xác nhận đặt hàng");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirm.setBackground(new Color(101, 67, 33));
        btnConfirm.setForeground(Color.BLACK);
        btnConfirm.setFocusPainted(false);
        btnConfirm.addActionListener(this);
        btnPanel.add(btnBack);
        btnPanel.add(btnConfirm);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    /**
     * actionPerformed() - Xử lý đặt hàng.
     * Luồng MD:
     * 1. Gọi lớp orders để đóng gói thông tin đơn hàng.
     * 2. Gọi phương thức createOrder() của lớp order_DAO.
     * 3. Gọi lớp order_items để đóng gói chi tiết → addOrderItem() của orderitem_DAO.
     * 4. Gọi clearCart() của cart_DAO.
     * 5. Navigate sang OrderView hiển thị tracking code.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConfirm) {
            String address = txtAddress.getText().trim();
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ nhận hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<CartItems> items = cartDAO.getAllCart(currentUser.getId());
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Bước 1: Gọi lớp orders để đóng gói thông tin đơn hàng
            double totalAmount = cartDAO.getTotalAmount(currentUser.getId());
            Orders order = Orders.createOrder(
                currentUser.getId(),
                txtName.getText().trim(),
                txtPhone.getText().trim(),
                address,
                txtNote.getText().trim(),
                totalAmount
            );

            // Bước 2: Gọi phương thức createOrder() của lớp order_DAO
            Orders createdOrder = orderDAO.createOrder(order);

            if (createdOrder != null) {
                // Bước 3: Gọi lớp order_items → addOrderItem() của orderitem_DAO
                for (CartItems cartItem : items) {
                    OrderItems orderItem = new OrderItems();
                    orderItem.setOrderId(createdOrder.getId());
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setSnapshotProductName(cartItem.getProductName());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setSnapshotUnitPrice(BigDecimal.valueOf(cartItem.getUnitPrice()));
                    String options = cartItem.getOptions() != null ? cartItem.getOptions() : "";
                    orderItem.setSnapshotOptions(options);
                    orderItem.setSubTotal(BigDecimal.valueOf(cartItem.getTotal()));
                    orderItemDAO.addOrderItem(orderItem);
                }

                // Bước 4: Gọi clearCart() của cart_DAO
                cartDAO.clearCart(currentUser.getId());

                // Bước 5: Navigate sang OrderView hiển thị tracking code
                this.dispose();
                OrderView orderView = new OrderView(currentUser, createdOrder.getTrackingCode());
                orderView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Đặt hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnBack) {
            this.dispose();
            CartFrm cartFrm = new CartFrm(currentUser);
            cartFrm.setVisible(true);
        }
    }
}

package com.coffeeshop.view;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Orders;
import com.coffeeshop.model.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * CancelConfirmFrm - Giao diện xác nhận hủy đơn hàng.
 * Hiển thị thông tin đơn, dropdown chọn lý do hủy, nút xác nhận.
 */
public class CancelConfirmFrm extends JFrame implements ActionListener {

    private JLabel lblOrderCode, lblStatus, lblTotal;
    private JComboBox<String> cmbReason;
    private JButton btnConfirm, btnBack;
    private final Users currentUser;
    private final UUID orderId;
    private Orders order;
    private final OrderDAO orderDAO = new OrderDAO();

    public CancelConfirmFrm(Users user, UUID orderId) {
        this.currentUser = user;
        this.orderId = orderId;
        initComponents();
        loadOrderDetail();
    }

    private void initComponents() {
        setTitle("Xác nhận hủy đơn hàng");
        setSize(450, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Header
        JLabel lblTitle = new JLabel("XÁC NHẬN HỦY ĐƠN HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(200, 50, 50));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Detail
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        detailPanel.add(createLabel("Mã đơn hàng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        lblOrderCode = new JLabel();
        lblOrderCode.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailPanel.add(lblOrderCode, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        lblStatus = new JLabel();
        detailPanel.add(lblStatus, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Tổng tiền:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        lblTotal = new JLabel();
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(new Color(200, 50, 50));
        detailPanel.add(lblTotal, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        detailPanel.add(createLabel("Lý do hủy:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbReason = new JComboBox<>(new String[]{
            "Đổi ý không muốn mua nữa",
            "Đặt nhầm sản phẩm",
            "Thay đổi địa chỉ giao hàng",
            "Thời gian giao hàng quá lâu",
            "Lý do khác"
        });
        cmbReason.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailPanel.add(cmbReason, gbc);

        mainPanel.add(detailPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setOpaque(false);
        btnBack = new JButton("Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.addActionListener(this);
        btnConfirm = new JButton("Xác nhận hủy");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirm.setBackground(new Color(200, 50, 50));
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
     * Tải chi tiết đơn hàng.
     * Luồng MD: CancelConfirmFrm() gọi lớp orders → order_DAO.getOrderDetail()
     */
    private void loadOrderDetail() {
        // Gọi phương thức getOrderDetail() của lớp order_DAO
        order = orderDAO.getOrderDetail(orderId);
        if (order != null) {
            lblOrderCode.setText(order.getTrackingCode() != null ? order.getTrackingCode() : orderId.toString().substring(0, 8));
            lblStatus.setText(order.getStatus());
            lblTotal.setText(String.format("%,.0f VND", order.getTotalAmount()));
        }
    }

    /**
     * actionPerformed() - Xử lý xác nhận hủy đơn.
     * Luồng MD:
     * 1. Gọi checkOrderStatus() → kiểm tra trạng thái.
     * 2. Gọi cancelOrder() → cập nhật "Đã hủy".
     * 3. Hiển thị thông báo hủy thành công.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConfirm) {
            // Bước 1: Gọi checkOrderStatus() - kiểm tra trạng thái
            String status = orderDAO.checkOrderStatus(orderId);
            if (status == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy đơn hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ("COMPLETED".equals(status) || "SHIPPING".equals(status) || "CANCELLED".equals(status)) {
                JOptionPane.showMessageDialog(this,
                    "Không thể hủy đơn hàng ở trạng thái: " + status,
                    "Không thể hủy", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Bước 2: Gọi cancelOrder()
            boolean success = orderDAO.cancelOrder(orderId);
            if (success) {
                // Bước 3: Hiển thị thông báo hủy thành công
                JOptionPane.showMessageDialog(this,
                    "Đơn hàng " + lblOrderCode.getText() + " đã được hủy thành công!\nLý do: " + cmbReason.getSelectedItem(),
                    "Hủy đơn thành công", JOptionPane.INFORMATION_MESSAGE);
                // Navigate sang OrderCancelledFrm hiển thị trạng thái "Đã hủy"
                this.dispose();
                OrderCancelledFrm cancelledFrm = new OrderCancelledFrm(currentUser, lblOrderCode.getText());
                cancelledFrm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Hủy đơn hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnBack) {
            this.dispose();
            OrderListFrm orderList = new OrderListFrm(currentUser);
            orderList.setVisible(true);
        }
    }
}

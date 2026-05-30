package com.coffeeshop.view;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.dao.OrderItemDAO;
import com.coffeeshop.model.OrderItems;
import com.coffeeshop.model.Orders;
import com.coffeeshop.model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

/**
 * ClientOrderDetailFrm - Giao diện chi tiết đơn hàng (Client).
 * Hiển thị thông tin đơn, thông tin người nhận, danh sách sản phẩm.
 */
public class ClientOrderDetailFrm extends JFrame implements ActionListener {

    private JLabel lblCode, lblDate, lblStatus;
    private JLabel lblName, lblPhone, lblAddress, lblNote;
    private JTable tblItems;
    private DefaultTableModel tableModel;
    private JButton btnBack;

    private final Users currentUser;
    private final UUID orderId;
    private Orders order;
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO itemDAO = new OrderItemDAO();

    public ClientOrderDetailFrm(Users user, UUID orderId) {
        this.currentUser = user;
        this.orderId = orderId;
        initComponents();
        loadOrderDetail();
    }

    private void initComponents() {
        setTitle("Chi tiết đơn hàng - Khách hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Top Panel: Info
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        infoPanel.setOpaque(false);
        
        // Order Info
        JPanel orderInfo = new JPanel(new GridLayout(3, 1));
        orderInfo.setBorder(BorderFactory.createTitledBorder("Thông tin đơn hàng"));
        orderInfo.setOpaque(false);
        lblCode = new JLabel("Mã: ");
        lblDate = new JLabel("Ngày đặt: ");
        lblStatus = new JLabel("Trạng thái: ");
        orderInfo.add(lblCode); orderInfo.add(lblDate); orderInfo.add(lblStatus);

        // Customer Info
        JPanel cusInfo = new JPanel(new GridLayout(4, 1));
        cusInfo.setBorder(BorderFactory.createTitledBorder("Thông tin nhận hàng"));
        cusInfo.setOpaque(false);
        lblName = new JLabel("Tên: ");
        lblPhone = new JLabel("SĐT: ");
        lblAddress = new JLabel("Địa chỉ: ");
        lblNote = new JLabel("Ghi chú: ");
        cusInfo.add(lblName); cusInfo.add(lblPhone); cusInfo.add(lblAddress); cusInfo.add(lblNote);

        infoPanel.add(orderInfo);
        infoPanel.add(cusInfo);
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Center: Items Table
        String[] columns = {"STT", "Tên sản phẩm", "Tùy chọn", "Số lượng", "Đơn giá", "Thành tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblItems = new JTable(tableModel);
        tblItems.setRowHeight(25);
        mainPanel.add(new JScrollPane(tblItems), BorderLayout.CENTER);

        // Bottom Panel: Back
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        bottomPanel.setOpaque(false);
        
        btnBack = new JButton("Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.addActionListener(this);

        bottomPanel.add(btnBack);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadOrderDetail() {
        order = orderDAO.getOrderDetail(orderId);
        if (order != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            lblCode.setText("Mã: " + (order.getTrackingCode() != null ? order.getTrackingCode() : ""));
            lblDate.setText("Ngày đặt: " + (order.getCreatedAt() != null ? sdf.format(order.getCreatedAt()) : ""));
            lblStatus.setText("Trạng thái: " + translateStatus(order.getStatus()));
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));

            lblName.setText("Tên: " + order.getCustomerName());
            lblPhone.setText("SĐT: " + order.getPhone());
            lblAddress.setText("Địa chỉ: " + order.getAddressText());
            lblNote.setText("Ghi chú: " + (order.getNote() != null ? order.getNote() : ""));

            // Load items
            List<OrderItems> items = itemDAO.getOrderItems(orderId);
            int stt = 1;
            for (OrderItems item : items) {
                tableModel.addRow(new Object[]{
                    stt++,
                    item.getSnapshotProductName(),
                    item.getSnapshotOptions(),
                    item.getQuantity(),
                    String.format("%,.0f VND", item.getSnapshotUnitPrice()),
                    String.format("%,.0f VND", item.getSubTotal())
                });
            }
        }
    }

    private String translateStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "Chờ xác nhận";
            case "CONFIRMED": return "Đã xác nhận";
            case "SHIPPING": return "Đang giao";
            case "COMPLETED": return "Hoàn thành";
            case "CANCELLED": return "Đã hủy";
            case "DELIVERED": return "Đã giao hàng";
            default: return status;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            this.dispose();
            new OrderListFrm(currentUser).setVisible(true);
        }
    }
}

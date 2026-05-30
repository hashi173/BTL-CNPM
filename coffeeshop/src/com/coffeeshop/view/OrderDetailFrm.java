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
 * OrderDetailFrm - Giao diện chi tiết đơn hàng (Admin).
 * Hiển thị thông tin đơn, khách hàng, danh sách sản phẩm. Cho phép cập nhật trạng thái.
 */
public class OrderDetailFrm extends JFrame implements ActionListener {

    private JLabel lblCode, lblDate, lblStatus;
    private JLabel lblName, lblPhone, lblAddress, lblNote;
    private JTable tblItems;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbUpdateStatus;
    private JButton btnUpdateStatus, btnBack;

    private final Users currentAdmin;
    private final UUID orderId;
    private Orders order;
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO itemDAO = new OrderItemDAO();

    public OrderDetailFrm(Users admin, UUID orderId) {
        this.currentAdmin = admin;
        this.orderId = orderId;
        initComponents();
        loadOrderDetail();
    }

    private void initComponents() {
        setTitle("Chi tiết đơn hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Top Panel: Info
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Order Info
        JPanel orderInfo = new JPanel(new GridLayout(3, 1));
        orderInfo.setBorder(BorderFactory.createTitledBorder("Thông tin đơn hàng"));
        lblCode = new JLabel("Mã: ");
        lblDate = new JLabel("Ngày đặt: ");
        lblStatus = new JLabel("Trạng thái: ");
        orderInfo.add(lblCode); orderInfo.add(lblDate); orderInfo.add(lblStatus);

        // Customer Info
        JPanel cusInfo = new JPanel(new GridLayout(4, 1));
        cusInfo.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
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

        // Bottom Panel: Update Status & Back
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        
        bottomPanel.add(new JLabel("Cập nhật trạng thái: "));
        cmbUpdateStatus = new JComboBox<>();
        btnUpdateStatus = new JButton("Cập nhật");
        btnUpdateStatus.setBackground(new Color(39, 174, 96));
        btnUpdateStatus.setForeground(Color.BLACK);
        btnUpdateStatus.addActionListener(this);
        
        btnBack = new JButton("Quay lại");
        btnBack.addActionListener(this);

        bottomPanel.add(cmbUpdateStatus);
        bottomPanel.add(btnUpdateStatus);
        bottomPanel.add(btnBack);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadOrderDetail() {
        order = orderDAO.getOrderDetail(orderId);
        if (order != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            lblCode.setText("Mã: " + order.getTrackingCode());
            lblDate.setText("Ngày đặt: " + (order.getCreatedAt() != null ? sdf.format(order.getCreatedAt()) : ""));
            lblStatus.setText("Trạng thái: " + translateStatus(order.getStatus()));

            lblName.setText("Tên: " + order.getCustomerName());
            lblPhone.setText("SĐT: " + order.getPhone());
            lblAddress.setText("Địa chỉ: " + order.getAddressText());
            lblNote.setText("Ghi chú: " + (order.getNote() != null ? order.getNote() : ""));

            // Setup valid status transitions
            cmbUpdateStatus.removeAllItems();
            String currentStatus = order.getStatus();
            if ("PENDING".equals(currentStatus)) {
                cmbUpdateStatus.addItem("PENDING");
                cmbUpdateStatus.addItem("CONFIRMED");
                cmbUpdateStatus.addItem("CANCELLED");
            } else if ("CONFIRMED".equals(currentStatus)) {
                cmbUpdateStatus.addItem("CONFIRMED");
                cmbUpdateStatus.addItem("SHIPPING");
                cmbUpdateStatus.addItem("CANCELLED");
            } else if ("SHIPPING".equals(currentStatus)) {
                cmbUpdateStatus.addItem("SHIPPING");
                cmbUpdateStatus.addItem("DELIVERED");
                cmbUpdateStatus.addItem("CANCELLED");
            } else {
                // DELIVERED, COMPLETED, CANCELLED
                cmbUpdateStatus.addItem(currentStatus);
                cmbUpdateStatus.setEnabled(false);
                btnUpdateStatus.setEnabled(false);
            }
            cmbUpdateStatus.setSelectedItem(currentStatus);

            // Load items
            List<OrderItems> items = itemDAO.getOrderItems(orderId);
            int stt = 1;
            for (OrderItems item : items) {
                tableModel.addRow(new Object[]{
                    stt++,
                    item.getSnapshotProductName(),
                    item.getSnapshotOptions(),
                    item.getQuantity(),
                    String.format("%,.0f", item.getSnapshotUnitPrice()),
                    String.format("%,.0f", item.getSubTotal())
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
            default: return status;
        }
    }

    /**
     * actionPerformed()
     * Luồng MD: Admin xem chi tiết, chọn cập nhật trạng thái -> updateOrderStatus()
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnUpdateStatus) {
            String newStatus = (String) cmbUpdateStatus.getSelectedItem();
            if (newStatus != null && !newStatus.equals(order.getStatus())) {
                if (orderDAO.updateOrderStatus(orderId, newStatus)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!");
                    lblStatus.setText("Trạng thái: " + translateStatus(newStatus));
                    order.setStatus(newStatus);
                    
                    // Reload transitions
                    String curr = newStatus;
                    cmbUpdateStatus.removeAllItems();
                    if ("PENDING".equals(curr)) {
                        cmbUpdateStatus.addItem("PENDING");
                        cmbUpdateStatus.addItem("CONFIRMED");
                        cmbUpdateStatus.addItem("CANCELLED");
                    } else if ("CONFIRMED".equals(curr)) {
                        cmbUpdateStatus.addItem("CONFIRMED");
                        cmbUpdateStatus.addItem("SHIPPING");
                        cmbUpdateStatus.addItem("CANCELLED");
                    } else if ("SHIPPING".equals(curr)) {
                        cmbUpdateStatus.addItem("SHIPPING");
                        cmbUpdateStatus.addItem("DELIVERED");
                        cmbUpdateStatus.addItem("CANCELLED");
                    } else {
                        cmbUpdateStatus.addItem(curr);
                        cmbUpdateStatus.setEnabled(false);
                        btnUpdateStatus.setEnabled(false);
                    }
                    cmbUpdateStatus.setSelectedItem(curr);
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == btnBack) {
            this.dispose();
            new OrderManagementFrm(currentAdmin).setVisible(true);
        }
    }
}

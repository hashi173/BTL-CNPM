package com.coffeeshop.view;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Orders;
import com.coffeeshop.model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * OrderListFrm - Giao diện hiển thị danh sách đơn hàng của khách hàng.
 * Mỗi đơn có nút "Hủy".
 */
public class OrderListFrm extends JFrame implements ActionListener {

    private JTable tblOrders;
    private DefaultTableModel tableModel;
    private JButton btnCancel, btnBack, btnSearch, btnTrack;
    private JTextField txtSearch;
    private final Users currentUser;
    private final OrderDAO orderDAO = new OrderDAO();
    private List<Orders> orderList;

    public OrderListFrm(Users user) {
        this.currentUser = user;
        initComponents();
        loadOrders("");
    }

    private void initComponents() {
        setTitle("Đơn hàng của tôi - Quán cà phê");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("ĐƠN HÀNG CỦA TÔI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(101, 67, 33));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);
        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSearch.addActionListener(this);
        btnBack = new JButton("Trang chủ");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.addActionListener(this);
        headerRight.add(new JLabel("Tìm mã/ngày:"));
        headerRight.add(txtSearch);
        headerRight.add(btnSearch);
        headerRight.add(btnBack);
        headerPanel.add(headerRight, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"STT", "Mã đơn", "Ngày đặt", "Tổng tiền", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblOrders = new JTable(tableModel);
        tblOrders.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblOrders.setRowHeight(28);
        tblOrders.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(tblOrders);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        footerPanel.setOpaque(false);
        btnTrack = new JButton("Xem chi tiết");
        btnTrack.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTrack.setBackground(new Color(41, 128, 185));
        btnTrack.setForeground(Color.BLACK);
        btnTrack.setFocusPainted(false);
        btnTrack.addActionListener(this);
        btnCancel = new JButton("Hủy đơn hàng");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setBackground(new Color(200, 50, 50));
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(this);
        footerPanel.add(btnTrack);
        footerPanel.add(btnCancel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Tải danh sách đơn hàng.
     * Luồng MD: OrderListFrm() gọi lớp orders → order_DAO.getOrdersByUser()
     */
    private void loadOrders(String keyword) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (keyword != null && !keyword.trim().isEmpty()) {
            orderList = orderDAO.searchOrdersByUser(currentUser.getId(), keyword);
        } else {
            orderList = orderDAO.getOrdersByUser(currentUser.getId());
        }
        int stt = 1;
        for (Orders o : orderList) {
            String status = translateStatus(o.getStatus());
            tableModel.addRow(new Object[]{
                stt++,
                o.getTrackingCode() != null ? o.getTrackingCode() : o.getId().toString().substring(0, 8),
                o.getCreatedAt() != null ? sdf.format(o.getCreatedAt()) : "",
                String.format("%,.0f VND", o.getTotalAmount()),
                status
            });
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
     * actionPerformed() - Xử lý hủy đơn hàng.
     * Luồng MD: Click "Hủy" → gọi CancelConfirmFrm.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCancel) {
            int row = tblOrders.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng cần hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Orders selectedOrder = orderList.get(row);
            // Gọi lớp CancelConfirmFrm
            this.dispose();
            CancelConfirmFrm cancelFrm = new CancelConfirmFrm(currentUser, selectedOrder.getId());
            cancelFrm.setVisible(true);
        } else if (e.getSource() == btnBack) {
            this.dispose();
            HomeFrm home = new HomeFrm(currentUser);
            home.setVisible(true);
        } else if (e.getSource() == btnSearch) {
            loadOrders(txtSearch.getText().trim());
        } else if (e.getSource() == btnTrack) {
            int row = tblOrders.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng cần xem!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Orders selectedOrder = orderList.get(row);
            this.dispose();
            ClientOrderDetailFrm detailFrm = new ClientOrderDetailFrm(currentUser, selectedOrder.getId());
            detailFrm.setVisible(true);
        }
    }
}

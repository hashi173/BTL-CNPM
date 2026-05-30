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
 * OrderManagementFrm - Giao diện quản lý đơn hàng cho Admin.
 * Hiển thị tất cả đơn hàng, tìm kiếm, nút "Xem chi tiết".
 */
public class OrderManagementFrm extends JFrame implements ActionListener {

    private JTextField txtSearch;
    private JButton btnSearch, btnViewDetail, btnBack;
    private JTable tblOrders;
    private DefaultTableModel tableModel;
    private final Users currentAdmin;
    private final OrderDAO orderDAO = new OrderDAO();
    private List<Orders> orderList;

    public OrderManagementFrm(Users admin) {
        this.currentAdmin = admin;
        initComponents();
        loadAllOrders();
    }

    private void initComponents() {
        setTitle("Quản lý đơn hàng");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐƠN HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Tìm kiếm");
        btnSearch.addActionListener(this);
        btnBack = new JButton("Trang chủ");
        btnBack.addActionListener(this);
        searchPanel.add(new JLabel("Tìm kiếm (Tên/SĐT/Mã):"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnBack);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"STT", "Mã đơn hàng", "Khách hàng", "Ngày đặt", "Tổng tiền", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblOrders = new JTable(tableModel);
        tblOrders.setRowHeight(28);
        mainPanel.add(new JScrollPane(tblOrders), BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnViewDetail = new JButton("Xem chi tiết / Xử lý");
        btnViewDetail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnViewDetail.setBackground(new Color(41, 128, 185));
        btnViewDetail.setForeground(Color.BLACK);
        btnViewDetail.addActionListener(this);
        footerPanel.add(btnViewDetail);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadAllOrders() {
        orderList = orderDAO.getAllOrders();
        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        int stt = 1;
        for (Orders o : orderList) {
            tableModel.addRow(new Object[]{
                stt++,
                o.getTrackingCode(),
                o.getCustomerName() + " - " + o.getPhone(),
                o.getCreatedAt() != null ? sdf.format(o.getCreatedAt()) : "",
                String.format("%,.0f", o.getTotalAmount()),
                translateStatus(o.getStatus())
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
     * actionPerformed()
     * Luồng MD: Click "Xem chi tiết" -> gọi OrderDetailFrm.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            String kw = txtSearch.getText().trim();
            if (kw.isEmpty()) loadAllOrders();
            else {
                orderList = orderDAO.searchOrders(kw);
                updateTable();
            }
        } else if (e.getSource() == btnViewDetail) {
            int row = tblOrders.getSelectedRow();
            if (row >= 0) {
                this.dispose();
                OrderDetailFrm detailFrm = new OrderDetailFrm(currentAdmin, orderList.get(row).getId());
                detailFrm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn hàng để xem!");
            }
        } else if (e.getSource() == btnBack) {
            this.dispose();
            new AdminHomeFrm(currentAdmin).setVisible(true);
        }
    }
}

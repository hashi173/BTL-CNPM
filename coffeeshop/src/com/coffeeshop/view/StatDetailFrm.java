package com.coffeeshop.view;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Orders;
import com.coffeeshop.model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * StatDetailFrm - Hiển thị chi tiết các đơn hàng đã đặt sản phẩm được chọn.
 */
public class StatDetailFrm extends JFrame {

    private final Users currentAdmin;
    private final String productName;
    private final String fromDate;
    private final String toDate;
    
    private final OrderDAO orderDAO = new OrderDAO();
    private JTable tblOrders;
    private DefaultTableModel tableModel;

    public StatDetailFrm(Users admin, String productName, String fromDate, String toDate) {
        this.currentAdmin = admin;
        this.productName = productName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Chi tiết đơn hàng - " + productName);
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("CÁC ĐƠN HÀNG CÓ MÓN: " + productName.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        
        String timeStr = "Khoảng thời gian: ";
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            timeStr += "Tất cả";
        } else {
            timeStr += fromDate + " đến " + toDate;
        }
        JLabel lblTime = new JLabel(timeStr);
        lblTime.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        headerPanel.add(lblTime, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"STT", "Mã đơn", "Khách hàng", "SĐT", "Số lượng mua", "Tổng tiền (VND)", "Trạng thái", "Ngày tạo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblOrders = new JTable(tableModel);
        tblOrders.setRowHeight(28);
        mainPanel.add(new JScrollPane(tblOrders), BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> this.dispose());
        footerPanel.add(btnClose);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Object[]> ordersList = orderDAO.getOrderDetailsByProductAndTime(productName, fromDate, toDate);
        
        int stt = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Object[] o : ordersList) {
            java.util.Date createdAt = (java.util.Date) o[5];
            tableModel.addRow(new Object[]{
                    stt++,
                    o[0], // tracking_code
                    o[1], // customer_name
                    o[2], // phone
                    o[6], // quantity
                    String.format("%,.0f", (Double) o[3]), // total_amount
                    o[4], // status
                    createdAt != null ? sdf.format(createdAt) : ""
            });
        }
    }
}

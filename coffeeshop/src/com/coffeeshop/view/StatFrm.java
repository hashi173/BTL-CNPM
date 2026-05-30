package com.coffeeshop.view;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * StatFrm - Giao diện thống kê cho Admin.
 * Hiển thị thống kê theo sản phẩm, tìm kiếm theo tên.
 */
public class StatFrm extends JFrame implements ActionListener {

    private JTextField txtProductSearch;
    private JSpinner spinFromDate, spinToDate;
    private JComboBox<String> cmbTimeFilter;
    private JButton btnFilter, btnBack;
    private JTable tblStat;
    private DefaultTableModel tableModel;
    private JLabel lblTotalRevenue, lblBestSeller;

    private final Users currentAdmin;
    private final OrderDAO orderDAO = new OrderDAO();

    public StatFrm(Users admin) {
        this.currentAdmin = admin;
        initComponents();
        loadStatData("", "", "");
    }

    private void initComponents() {
        setTitle("Thống kê doanh thu");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU THEO MÓN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        searchPanel.add(new JLabel("Khoảng t/gian:"));
        cmbTimeFilter = new JComboBox<>(new String[]{"Tất cả", "Hôm nay", "Tuần này", "Tháng này", "Tùy chỉnh"});
        cmbTimeFilter.addActionListener(e -> {
            boolean isCustom = "Tùy chỉnh".equals(cmbTimeFilter.getSelectedItem());
            spinFromDate.setEnabled(isCustom);
            spinToDate.setEnabled(isCustom);
            if (btnFilter != null) {
                btnFilter.doClick();
            }
        });
        searchPanel.add(cmbTimeFilter);

        searchPanel.add(new JLabel("Từ:"));
        spinFromDate = new JSpinner(new SpinnerDateModel());
        spinFromDate.setEditor(new JSpinner.DateEditor(spinFromDate, "yyyy-MM-dd"));
        spinFromDate.setEnabled(false);
        searchPanel.add(spinFromDate);
        
        searchPanel.add(new JLabel("Đến:"));
        spinToDate = new JSpinner(new SpinnerDateModel());
        spinToDate.setEditor(new JSpinner.DateEditor(spinToDate, "yyyy-MM-dd"));
        spinToDate.setEnabled(false);
        searchPanel.add(spinToDate);

        searchPanel.add(new JLabel("Tên món:"));
        txtProductSearch = new JTextField(10);
        searchPanel.add(txtProductSearch);

        btnFilter = new JButton("Lọc thống kê");
        btnFilter.addActionListener(this);
        searchPanel.add(btnFilter);

        btnBack = new JButton("Trang chủ");
        btnBack.addActionListener(this);
        searchPanel.add(btnBack);
        headerPanel.add(searchPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"STT", "Tên món", "Số lượng bán", "Doanh thu (VND)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblStat = new JTable(tableModel);
        tblStat.setRowHeight(28);
        tblStat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblStat.getSelectedRow();
                    if (row != -1) {
                        String productName = (String) tblStat.getValueAt(row, 1);
                        String from = "", to = "";
                        if ("Tùy chỉnh".equals(cmbTimeFilter.getSelectedItem())) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            from = sdf.format((Date) spinFromDate.getValue());
                            to = sdf.format((Date) spinToDate.getValue());
                        } else {
                            String filter = (String) cmbTimeFilter.getSelectedItem();
                            java.time.LocalDate today = java.time.LocalDate.now();
                            if ("Hôm nay".equals(filter)) {
                                from = today.toString();
                                to = today.toString();
                            } else if ("Tuần này".equals(filter)) {
                                from = today.minusDays(today.getDayOfWeek().getValue() - 1).toString();
                                to = today.plusDays(7 - today.getDayOfWeek().getValue()).toString();
                            } else if ("Tháng này".equals(filter)) {
                                from = today.withDayOfMonth(1).toString();
                                to = today.withDayOfMonth(today.lengthOfMonth()).toString();
                            }
                        }
                        new StatDetailFrm(currentAdmin, productName, from, to).setVisible(true);
                    }
                }
            }
        });
        mainPanel.add(new JScrollPane(tblStat), BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        
        lblBestSeller = new JLabel("Sản phẩm bán chạy nhất: Chưa có dữ liệu");
        lblBestSeller.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblBestSeller.setForeground(new Color(41, 128, 185));
        footerPanel.add(lblBestSeller, BorderLayout.WEST);
        
        lblTotalRevenue = new JLabel("Tổng doanh thu: 0 VND");
        lblTotalRevenue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalRevenue.setForeground(new Color(200, 50, 50));
        footerPanel.add(lblTotalRevenue, BorderLayout.EAST);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Tải dữ liệu thống kê.
     * Luồng MD: StatFrm gọi Orders -> getStatByProduct()
     */
    private void loadStatData(String productName, String fromDate, String toDate) {
        tableModel.setRowCount(0);
        List<Object[]> stats = orderDAO.getStatByProductAndTime(productName, fromDate, toDate);
        int stt = 1;
        double totalRev = 0;
        
        for (Object[] row : stats) {
            double rev = (Double) row[2];
            totalRev += rev;
            tableModel.addRow(new Object[]{
                stt++,
                row[0], // Tên món
                row[1], // SL bán
                String.format("%,.0f", rev)
            });
        }
        
        if (!stats.isEmpty()) {
            Object[] best = stats.get(0);
            lblBestSeller.setText("Sản phẩm bán chạy nhất: " + best[0] + " (" + best[1] + " ly)");
        } else {
            lblBestSeller.setText("Sản phẩm bán chạy nhất: Chưa có dữ liệu");
        }
        
        lblTotalRevenue.setText("Tổng doanh thu: " + String.format("%,.0f VND", totalRev));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFilter) {
            String kw = txtProductSearch.getText().trim();
            String filter = (String) cmbTimeFilter.getSelectedItem();
            String from = "", to = "";
            java.time.LocalDate today = java.time.LocalDate.now();
            
            if ("Hôm nay".equals(filter)) {
                from = today.toString();
                to = today.toString();
            } else if ("Tuần này".equals(filter)) {
                from = today.minusDays(today.getDayOfWeek().getValue() - 1).toString();
                to = today.plusDays(7 - today.getDayOfWeek().getValue()).toString();
            } else if ("Tháng này".equals(filter)) {
                from = today.withDayOfMonth(1).toString();
                to = today.withDayOfMonth(today.lengthOfMonth()).toString();
            } else if ("Tùy chỉnh".equals(filter)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                from = sdf.format((Date) spinFromDate.getValue());
                to = sdf.format((Date) spinToDate.getValue());
            }
            
            loadStatData(kw, from, to);
        } else if (e.getSource() == btnBack) {
            this.dispose();
            new AdminHomeFrm(currentAdmin).setVisible(true);
        }
    }
}

package com.coffeeshop.view;

import com.coffeeshop.dao.CartDAO;
import com.coffeeshop.model.CartItems;
import com.coffeeshop.model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;

/**
 * CartFrm - Giao diện hiển thị danh sách các món trong giỏ hàng.
 * Có nút Checkout, Xóa món, Quay lại.
 */
public class CartFrm extends JFrame implements ActionListener {

    private JTable tblCart;
    private DefaultTableModel tableModel;
    private JLabel lblTotal;
    private JButton btnCheckout, btnRemove, btnBack, btnMenu;
    private final Users currentUser;
    private final CartDAO cartDAO = new CartDAO();

    public CartFrm(Users user) {
        this.currentUser = user;
        initComponents();
        loadCart();
    }

    private void initComponents() {
        setTitle("Giỏ hàng - Quán cà phê");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("GIỎ HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(101, 67, 33));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);
        btnMenu = new JButton("Tiếp tục mua");
        btnMenu.addActionListener(this);
        btnBack = new JButton("Trang chủ");
        btnBack.addActionListener(this);
        headerRight.add(btnMenu);
        headerRight.add(btnBack);
        headerPanel.add(headerRight, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"STT", "Tên sản phẩm", "Tùy chọn", "SL", "Đơn giá", "Thành tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblCart = new JTable(tableModel);
        tblCart.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblCart.setRowHeight(28);
        tblCart.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(tblCart);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout(10, 5));
        footerPanel.setOpaque(false);

        lblTotal = new JLabel("Tổng: 0 VND");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(new Color(200, 50, 50));
        footerPanel.add(lblTotal, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.setOpaque(false);
        btnRemove = new JButton("Xóa món");
        btnRemove.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRemove.addActionListener(this);
        btnCheckout = new JButton("Thanh toán");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCheckout.setBackground(new Color(101, 67, 33));
        btnCheckout.setForeground(Color.BLACK);
        btnCheckout.setFocusPainted(false);
        btnCheckout.addActionListener(this);
        btnPanel.add(btnRemove);
        btnPanel.add(btnCheckout);
        footerPanel.add(btnPanel, BorderLayout.EAST);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Tải danh sách giỏ hàng.
     * Luồng MD: CartFrm() gọi lớp cart_items → cart_DAO.getAllCart(userId)
     */
    private void loadCart() {
        tableModel.setRowCount(0);
        // Gọi phương thức getAllCart(userId) của lớp cart_DAO
        List<CartItems> items = cartDAO.getAllCart(currentUser.getId());
        int stt = 1;
        double total = 0;
        for (CartItems item : items) {
            double subtotal = item.getTotal();
            total += subtotal;
            tableModel.addRow(new Object[]{
                stt++,
                item.getProductName(),
                item.getOptions() != null ? item.getOptions() : "",
                item.getQuantity(),
                String.format("%,.0f", item.getUnitPrice()),
                String.format("%,.0f", subtotal)
            });
        }
        lblTotal.setText("Tổng: " + String.format("%,.0f", total) + " VND");
    }

    /**
     * actionPerformed() - Xử lý sự kiện.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCheckout) {
            List<CartItems> items = cartDAO.getAllCart(currentUser.getId());
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Gọi lớp CheckoutFrm
            this.dispose();
            CheckoutFrm checkoutFrm = new CheckoutFrm(currentUser);
            checkoutFrm.setVisible(true);
        } else if (e.getSource() == btnRemove) {
            int row = tblCart.getSelectedRow();
            if (row >= 0) {
                // Lấy item ID từ danh sách
                List<CartItems> items = cartDAO.getAllCart(currentUser.getId());
                if (row < items.size()) {
                    UUID itemId = items.get(row).getId();
                    // Gọi phương thức removeCartItem(itemId) của lớp cart_DAO
                    cartDAO.removeCartItem(itemId);
                    loadCart();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == btnMenu) {
            this.dispose();
            MenuFrm menuFrm = new MenuFrm(currentUser);
            menuFrm.setVisible(true);
        } else if (e.getSource() == btnBack) {
            this.dispose();
            HomeFrm home = new HomeFrm(currentUser);
            home.setVisible(true);
        }
    }
}

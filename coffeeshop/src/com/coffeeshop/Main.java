package com.coffeeshop;

import com.coffeeshop.view.LoginFrm;

import javax.swing.*;

/**
 * Main class - Điểm bắt đầu của chương trình Java Swing.
 */
public class Main {
    public static void main(String[] args) {
        // Thiết lập Look and Feel cho giao diện
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chạy LoginFrm
        SwingUtilities.invokeLater(() -> {
            LoginFrm loginFrm = new LoginFrm();
            loginFrm.setVisible(true);
        });
    }
}

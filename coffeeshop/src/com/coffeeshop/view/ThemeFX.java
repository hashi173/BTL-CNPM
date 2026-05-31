package com.coffeeshop.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * ThemeFX - Factory cho các component JavaFX theo design system mới.
 */
public final class ThemeFX {

    private ThemeFX() {}

    // ─── Buttons ─────────────────────────────────────────────

    public static Button primaryBtn(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-primary");
        btn.setMinHeight(40);
        return btn;
    }

    public static Button dangerBtn(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-danger");
        btn.setMinHeight(40);
        return btn;
    }

    public static Button successBtn(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-success");
        btn.setMinHeight(40);
        return btn;
    }

    public static Button accentBtn(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-accent");
        btn.setMinHeight(40);
        return btn;
    }

    public static Button outlineBtn(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-outline");
        btn.setMinHeight(40);
        return btn;
    }

    public static Button ghostBtn(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-ghost");
        return btn;
    }

    // ─── Labels ──────────────────────────────────────────────

    public static Label titleLabel(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("title");
        return lbl;
    }

    public static Label subtitleLabel(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("subtitle");
        return lbl;
    }

    public static Label bodyLabel(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("body");
        return lbl;
    }

    public static Label captionLabel(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("caption");
        return lbl;
    }

    public static Label formLabel(String text) {
        Label lbl = new Label(text);
        lbl.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
        return lbl;
    }

    // ─── Inputs ──────────────────────────────────────────────

    public static TextField textField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(40);
        return tf;
    }

    public static PasswordField passwordField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setPrefHeight(40);
        return pf;
    }

    // ─── Layout Helpers ──────────────────────────────────────

    public static VBox formField(String labelText, Node input) {
        Label lbl = formLabel(labelText);
        VBox.setMargin(lbl, new Insets(0, 0, 4, 0));
        return new VBox(4, lbl, input);
    }

    public static VBox card() {
        VBox c = new VBox(12);
        c.getStyleClass().add("card");
        return c;
    }

    public static VBox card(double padding) {
        VBox c = card();
        c.setPadding(new Insets(padding));
        return c;
    }

    public static VBox pageHeader(String title, Node rightComponent) {
        Label lbl = titleLabel(title);

        if (rightComponent != null) {
            HBox header = new HBox();
            header.setAlignment(Pos.CENTER_LEFT);
            lbl.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(lbl, Priority.ALWAYS);
            header.getChildren().addAll(lbl, rightComponent);

            Separator sep = new Separator();
            sep.getStyleClass().add("page-header-separator");

            VBox box = new VBox(12, header, sep);
            box.getStyleClass().add("page-header");
            return box;
        }

        Separator sep = new Separator();
        sep.getStyleClass().add("page-header-separator");

        VBox box = new VBox(8, lbl, sep);
        box.getStyleClass().add("page-header");
        return box;
    }

    public static VBox pageHeader(String title) {
        return pageHeader(title, null);
    }

    public static HBox buttonBar(Node... buttons) {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_RIGHT);
        bar.getChildren().addAll(buttons);
        return bar;
    }

    public static HBox centeredButtonBar(Node... buttons) {
        HBox bar = new HBox(15);
        bar.setAlignment(Pos.CENTER);
        bar.getChildren().addAll(buttons);
        return bar;
    }

    public static HBox searchBar(String placeholder, TextField field, Button btn) {
        Label lbl = captionLabel(placeholder);
        lbl.setMinHeight(40);
        HBox bar = new HBox(8, lbl, field, btn);
        bar.setAlignment(Pos.CENTER_RIGHT);
        return bar;
    }

    public static Label statusBadge(String status) {
        Label badge = new Label(status);
        badge.getStyleClass().add("badge");
        switch (status) {
            case "Chờ xác nhận": badge.getStyleClass().add("badge-pending"); break;
            case "Đã xác nhận": badge.getStyleClass().add("badge-confirmed"); break;
            case "Đang giao": badge.getStyleClass().add("badge-shipping"); break;
            case "Hoàn thành": case "Đã giao hàng": badge.getStyleClass().add("badge-completed"); break;
            case "Đã hủy": badge.getStyleClass().add("badge-cancelled"); break;
            default: badge.getStyleClass().add("badge-pending"); break;
        }
        return badge;
    }

    public static VBox statCard(String label, String value, String gradientClass) {
        Label lbl = new Label(label);
        lbl.getStyleClass().add("stat-label");
        Label val = new Label(value);
        val.getStyleClass().add("stat-value");
        VBox c = new VBox(6, lbl, val);
        c.getStyleClass().addAll("stat-card", gradientClass);
        c.setPadding(new Insets(20, 22, 20, 22));
        return c;
    }

    public static String translateStatus(String status) {
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
}

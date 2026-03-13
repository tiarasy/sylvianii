package com.tiara.utstiarajava.controller;

import com.tiara.utstiarajava.model.PenggunaModel;
import com.tiara.utstiarajava.model.HistoriModel;
import com.tiara.utstiarajava.view.PenggunaView;
import com.tiara.utstiarajava.view.HistoriLoginView;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class PenggunaController {
    private PenggunaView view;
    private PenggunaModel model;

    public PenggunaController(PenggunaView view, PenggunaModel model) {
        this.view = view;
        this.model = model;
        loadData();
        this.view.btnSimpan.addActionListener(e -> aksiSimpan());
        this.view.btnUbah.addActionListener(e -> aksiUbah());
        this.view.btnHapus.addActionListener(e -> aksiHapus());
        this.view.btnReset.addActionListener(e -> view.resetForm());
        this.view.btnHistori.addActionListener(e -> bukaHistori());
        this.view.tablePengguna.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && view.tablePengguna.getSelectedRow() != -1) {
                int row = view.tablePengguna.getSelectedRow();
                view.txtUsername.setText(view.modelTable.getValueAt(row, 0).toString());
                view.cbLevel.setSelectedItem(view.modelTable.getValueAt(row, 1).toString());
                view.cbStatus.setSelectedItem(view.modelTable.getValueAt(row, 2).toString());
                view.txtPassword.setText("");
                view.txtUsername.setEnabled(false);
                view.kondisiProses();
            }
        });
    }

    private void loadData() {
        view.modelTable.setRowCount(0);
        try {
            ResultSet rs = model.tampilkanSemua();
            while (rs.next()) {
                view.modelTable.addRow(new Object[]{
                    rs.getString("nm_pengguna"),
                    rs.getString("level_user"),
                    rs.getString("status_akun"),
                    rs.getString("terakhir_login") != null ? rs.getString("terakhir_login") : ""
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }

    private void aksiSimpan() {
        String username = view.txtUsername.getText();
        String password = new String(view.txtPassword.getPassword());
        String level = view.cbLevel.getSelectedItem().toString();
        String status = view.cbStatus.getSelectedItem().toString();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Username dan Password tidak boleh kosong!");
            return;
        }
        try {
            if (model.simpan(username, password, level, status)) {
                JOptionPane.showMessageDialog(view, "Data berhasil disimpan!");
                loadData();
                view.resetForm();
                view.kondisiAwal();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal: " + e.getMessage());
        }
    }

    private void aksiUbah() {
        if (view.txtUsername.getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan diubah!");
            return;
        }
        
        String username = view.txtUsername.getText();
        String password = new String(view.txtPassword.getPassword());
        String level = view.cbLevel.getSelectedItem().toString();
        String status = view.cbStatus.getSelectedItem().toString();
        
        try {
            if (model.ubah(username, password, level, status)) {
                JOptionPane.showMessageDialog(view, "Data berhasil diubah!");
                loadData();
                view.resetForm();
                view.kondisiAwal();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal: " + e.getMessage());
        }
    }

    private void aksiHapus() {
        if (view.txtUsername.getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan dihapus!");
            return;
        }
        
        String username = view.txtUsername.getText();
        
        int confirm = JOptionPane.showConfirmDialog(view, "Yakin hapus " + username + "?", 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (model.hapus(username)) {
                    JOptionPane.showMessageDialog(view, "Data berhasil dihapus!");
                    loadData();
                    view.resetForm();
                    view.kondisiAwal();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Error: " + e.getMessage());
            }
        }
    }

    private void bukaHistori() {
        HistoriLoginView hView = new HistoriLoginView();
        HistoriModel hModel = new HistoriModel();
        new HistoriController(hView, hModel);
        hView.setVisible(true);
    }
}
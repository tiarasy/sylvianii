package com.tiara.utstiarajava.controller;

import com.tiara.utstiarajava.model.CobaModel;
import com.tiara.utstiarajava.view.CobaView;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class CobaController {
    private CobaView view;
    private CobaModel model;
    
    public CobaController(CobaView view, CobaModel model) {
        this.view = view;
        this.model = model;
        loadData();
        this.view.btnSimpan.addActionListener(e -> aksiSimpan());
        this.view.btnUbah.addActionListener(e -> aksiUbah());
        this.view.btnHapus.addActionListener(e -> aksiHapus());
        this.view.btnRefresh.addActionListener(e -> loadData());
        this.view.tableCoba.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && view.tableCoba.getSelectedRow() != -1) {
                int row = view.tableCoba.getSelectedRow();
                view.txtId.setText(view.modelTable.getValueAt(row, 0).toString());
                view.txtNama.setText(view.modelTable.getValueAt(row, 1).toString());
                view.txtKeterangan.setText(view.modelTable.getValueAt(row, 2).toString());
            }
        });
    }
    
    private void loadData() {
        view.modelTable.setRowCount(0);
        try {
            ResultSet rs = model.tampilkanSemua();
            while (rs.next()) {
                view.modelTable.addRow(new Object[]{
                    rs.getInt("id_coba"),
                    rs.getString("nama"),
                    rs.getString("keterangan"),
                    rs.getString("created_at")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error load data: " + e.getMessage());
        }
    }
    
    private void aksiSimpan() {
        String nama = view.txtNama.getText();
        String keterangan = view.txtKeterangan.getText();
        
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nama tidak boleh kosong!");
            return;
        }
        
        try {
            if (model.simpan(nama, keterangan)) {
                JOptionPane.showMessageDialog(view, "Data berhasil disimpan!");
                loadData();
                view.txtId.setText("");
                view.txtNama.setText("");
                view.txtKeterangan.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error simpan: " + e.getMessage());
        }
    }
    
    private void aksiUbah() {
        if (view.txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan diubah!");
            return;
        }
        
        int id = Integer.parseInt(view.txtId.getText());
        String nama = view.txtNama.getText();
        String keterangan = view.txtKeterangan.getText();
        
        try {
            if (model.ubah(id, nama, keterangan)) {
                JOptionPane.showMessageDialog(view, "Data berhasil diubah!");
                loadData();
                view.txtId.setText("");
                view.txtNama.setText("");
                view.txtKeterangan.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error ubah: " + e.getMessage());
        }
    }
    
    private void aksiHapus() {
        if (view.txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan dihapus!");
            return;
        }
        
        int id = Integer.parseInt(view.txtId.getText());
        String nama = view.txtNama.getText();
        
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Yakin ingin menghapus data '" + nama + "'?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (model.hapus(id)) {
                    JOptionPane.showMessageDialog(view, "Data berhasil dihapus!");
                    loadData();
                    view.txtId.setText("");
                    view.txtNama.setText("");
                    view.txtKeterangan.setText("");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Error hapus: " + e.getMessage());
            }
        }
    }
}
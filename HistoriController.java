package com.tiara.utstiarajava.controller;

import com.tiara.utstiarajava.model.HistoriModel;
import com.tiara.utstiarajava.view.HistoriLoginView;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class HistoriController {
    private HistoriLoginView view;
    private HistoriModel model;

    public HistoriController(HistoriLoginView view, HistoriModel model) {
        this.view = view;
        this.model = model;
        loadData();
        this.view.btnRefresh.addActionListener(e -> loadData());
        this.view.btnHapusSemua.addActionListener(e -> hapusSemuaHistori());
    }

    private void loadData() {
        view.modelTable.setRowCount(0);
        try {
            ResultSet rs = model.ambilHistori();
            int count = 0;
            while (rs.next()) {
                view.modelTable.addRow(new Object[]{
                    rs.getInt("kd_histori"),
                    rs.getString("nm_pengguna"),
                    rs.getString("keterangan"),
                    rs.getString("tgl_wkt")
                });
                count++;
            }
            view.lblStatus.setText("Total histori: " + count);
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(view, "Error load histori: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void hapusSemuaHistori() {
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Yakin hapus semua histori?", 
            "Konfirmasi Hapus Semua", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (model.hapusSemuaHistori()) {
                    JOptionPane.showMessageDialog(view, "Semua histori berhasil dihapus!");
                    loadData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
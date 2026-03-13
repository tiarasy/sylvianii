package com.tiara.utstiarajava.controller;

import com.tiara.utstiarajava.model.PendidikanModel;
import com.tiara.utstiarajava.view.PendidikanView;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class PendidikanController {
    private PendidikanView view;
    private PendidikanModel model;
    
    public PendidikanController(PendidikanView view, PendidikanModel model) {
        this.view = view;
        this.model = model;
        loadData();
        this.view.btnSimpan.addActionListener(e -> aksiSimpan());
        this.view.btnUbah.addActionListener(e -> aksiUbah());
        this.view.btnHapus.addActionListener(e -> aksiHapus());
        this.view.btnRefresh.addActionListener(e -> loadData());
        this.view.tablePendidikan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && view.tablePendidikan.getSelectedRow() != -1) {
                int row = view.tablePendidikan.getSelectedRow();
                view.txtKdPendidikan.setText(view.modelTable.getValueAt(row, 0).toString());
                view.txtKode.setText(view.modelTable.getValueAt(row, 1).toString());
                view.txtNamaInstansi.setText(view.modelTable.getValueAt(row, 2).toString());
                view.txtDariTahun.setText(view.modelTable.getValueAt(row, 3).toString());
                view.txtSampaiTahun.setText(view.modelTable.getValueAt(row, 4).toString());
                view.txtJurusan.setText(view.modelTable.getValueAt(row, 5).toString());
                view.txtKdPendidikan.setEnabled(false);
            }
        });
    }
    
    private void loadData() {
        view.modelTable.setRowCount(0);
        try {
            ResultSet rs = model.tampilkanSemua();
            while (rs.next()) {
                view.modelTable.addRow(new Object[]{
                    rs.getInt("kd_pendidikan"),
                    rs.getString("kode"),
                    rs.getString("nama_instansi"),
                    rs.getString("dari_tahun"),
                    rs.getString("sampai_tahun"),
                    rs.getString("jurusan")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error load data: " + e.getMessage());
        }
    }
    
    private void aksiSimpan() {
        String kode = view.txtKode.getText().trim();
        String namaInstansi = view.txtNamaInstansi.getText().trim();
        String dariTahun = view.txtDariTahun.getText().trim();
        String sampaiTahun = view.txtSampaiTahun.getText().trim();
        String jurusan = view.txtJurusan.getText().trim();
        
        if (kode.isEmpty() || namaInstansi.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Kode dan Nama Instansi tidak boleh kosong!");
            return;
        }
        
        try {
            if (model.simpan(kode, namaInstansi, dariTahun, sampaiTahun, jurusan)) {
                JOptionPane.showMessageDialog(view, "Data berhasil disimpan!");
                loadData();
                view.resetForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error simpan: " + e.getMessage());
        }
    }
    
    private void aksiUbah() {
        if (view.txtKdPendidikan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan diubah!");
            return;
        }
        
        int kdPendidikan = Integer.parseInt(view.txtKdPendidikan.getText());
        String kode = view.txtKode.getText().trim();
        String namaInstansi = view.txtNamaInstansi.getText().trim();
        String dariTahun = view.txtDariTahun.getText().trim();
        String sampaiTahun = view.txtSampaiTahun.getText().trim();
        String jurusan = view.txtJurusan.getText().trim();
        
        try {
            if (model.ubah(kdPendidikan, kode, namaInstansi, dariTahun, sampaiTahun, jurusan)) {
                JOptionPane.showMessageDialog(view, "Data berhasil diubah!");
                loadData();
                view.resetForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error ubah: " + e.getMessage());
        }
    }
    
    private void aksiHapus() {
        if (view.txtKdPendidikan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan dihapus!");
            return;
        }
        
        int kdPendidikan = Integer.parseInt(view.txtKdPendidikan.getText());
        
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (model.hapus(kdPendidikan)) {
                    JOptionPane.showMessageDialog(view, "Data berhasil dihapus!");
                    loadData();
                    view.resetForm();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Error hapus: " + e.getMessage());
            }
        }
    }
}
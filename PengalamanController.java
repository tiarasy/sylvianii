package com.tiara.utstiarajava.controller;

import com.tiara.utstiarajava.model.PengalamanModel;
import com.tiara.utstiarajava.view.PengalamanView;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class PengalamanController {
    private PengalamanView view;
    private PengalamanModel model;
    
    public PengalamanController(PengalamanView view, PengalamanModel model) {
        this.view = view;
        this.model = model;
        loadData();
        
        this.view.btnSimpan.addActionListener(e -> aksiSimpan());
        this.view.btnUbah.addActionListener(e -> aksiUbah());
        this.view.btnHapus.addActionListener(e -> aksiHapus());
        this.view.btnBatal.addActionListener(e -> view.resetForm());
        this.view.btnRefresh.addActionListener(e -> loadData());
        
        this.view.tablePengalaman.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && view.tablePengalaman.getSelectedRow() != -1) {
                int row = view.tablePengalaman.getSelectedRow();
                view.txtKdPengalaman.setText(view.modelTable.getValueAt(row, 0).toString());
                view.txtKode.setText(view.modelTable.getValueAt(row, 1).toString());
                view.txtNamaPerusahaan.setText(view.modelTable.getValueAt(row, 2).toString());
                view.txtJabatan.setText(view.modelTable.getValueAt(row, 3).toString());
                view.txtTahunBekerja.setText(view.modelTable.getValueAt(row, 4).toString());
                view.txtKode.setEnabled(false);
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
                    rs.getInt("kd_pengalaman"),
                    rs.getString("kode"),
                    rs.getString("nama_perusahaan"),
                    rs.getString("jabatan_terakhir"),
                    rs.getString("terakhir_bekerja")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error load data: " + e.getMessage());
        }
    }
    
    private void aksiSimpan() {
        String kode = view.txtKode.getText().trim();
        String namaPerusahaan = view.txtNamaPerusahaan.getText().trim();
        String jabatan = view.txtJabatan.getText().trim();
        String tahun = view.txtTahunBekerja.getText().trim();
        
        if (kode.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Kode tidak boleh kosong!");
            view.txtKode.requestFocus();
            return;
        }
        
        if (namaPerusahaan.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nama Perusahaan tidak boleh kosong!");
            view.txtNamaPerusahaan.requestFocus();
            return;
        }
        
        if (jabatan.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Jabatan tidak boleh kosong!");
            view.txtJabatan.requestFocus();
            return;
        }
        
        try {
            if (model.simpan(kode, namaPerusahaan, jabatan, tahun)) {
                JOptionPane.showMessageDialog(view, "Data berhasil disimpan!");
                loadData();
                view.resetForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error simpan: " + e.getMessage());
        }
    }
    
    private void aksiUbah() {
        if (view.txtKdPengalaman.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan diubah!");
            return;
        }
        
        int kdPengalaman = Integer.parseInt(view.txtKdPengalaman.getText());
        String kode = view.txtKode.getText().trim();
        String namaPerusahaan = view.txtNamaPerusahaan.getText().trim();
        String jabatan = view.txtJabatan.getText().trim();
        String tahun = view.txtTahunBekerja.getText().trim();
        
        if (kode.isEmpty() || namaPerusahaan.isEmpty() || jabatan.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Semua field harus diisi!");
            return;
        }
        
        try {
            if (model.ubah(kdPengalaman, kode, namaPerusahaan, jabatan, tahun)) {
                JOptionPane.showMessageDialog(view, "Data berhasil diubah!");
                loadData();
                view.resetForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error ubah: " + e.getMessage());
        }
    }
    
    private void aksiHapus() {
        if (view.txtKdPengalaman.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data yang akan dihapus!");
            return;
        }
        
        int kdPengalaman = Integer.parseInt(view.txtKdPengalaman.getText());
        String kode = view.txtKode.getText();
        
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Yakin ingin menghapus data dengan kode " + kode + "?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (model.hapus(kdPengalaman)) {
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
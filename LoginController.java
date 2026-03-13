package com.tiara.utstiarajava.controller;

import com.tiara.utstiarajava.model.PenggunaModel;
import com.tiara.utstiarajava.model.HistoriModel;
import com.tiara.utstiarajava.model.Koneksi;
import com.tiara.utstiarajava.view.LoginView;
import com.tiara.utstiarajava.view.MenuUtamaView;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class LoginController {
    private LoginView view;
    private PenggunaModel model;
    private HistoriModel historiModel;
    
    public LoginController(LoginView view, PenggunaModel model, HistoriModel historiModel) {
        this.view = view;
        this.model = model;
        this.historiModel = historiModel;
        this.view.btnLogin.addActionListener(e -> prosesLogin());
        this.view.btnBatal.addActionListener(e -> System.exit(0));
    }
    
    private void prosesLogin() {
        String username = view.txtUsername.getText();
        String password = new String(view.txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            view.lblStatus.setText("Username dan Password harus diisi!");
            return;
        }
        
        try {
            // Test koneksi
            if (!Koneksi.testKoneksi()) {
                view.lblStatus.setText("Koneksi database putus!");
                return;
            }
            
            // Cari user
            ResultSet rs = model.cariPengguna(username);
            
            if (rs.next()) {
                String passwordDB = rs.getString("psw_pengguna");
                String level = rs.getString("level_user");
                String status = rs.getString("status_akun");
                
                if (password.equals(passwordDB)) {
                    if (status.equalsIgnoreCase("Aktif")) {
                        // Simpan histori login
                        historiModel.simpanHistori(username, "Login berhasil");
                        model.updateLastLogin(username);
                        
                        JOptionPane.showMessageDialog(view, "Login berhasil sebagai " + level);
                        
                        // Buka menu utama
                        MenuUtamaView menuView = new MenuUtamaView();
                        new MenuUtamaController(menuView, username, level);
                        menuView.setVisible(true);
                        view.dispose(); // Tutup login form
                        
                    } else {
                        view.lblStatus.setText("Akun Anda " + status);
                    }
                } else {
                    view.lblStatus.setText("Password salah!");
                }
            } else {
                view.lblStatus.setText("Username tidak ditemukan!");
            }
            
            rs.close();
            
        } catch (SQLException e) {
            view.lblStatus.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
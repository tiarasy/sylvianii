package com.tiara.utstiarajava.controller;

import com.tiara.utstiarajava.model.*;
import com.tiara.utstiarajava.view.*;
import javax.swing.*;
import java.awt.*;

public class MenuUtamaController {
    private MenuUtamaView view;
    private String username;
    private String level;
    
    public MenuUtamaController(MenuUtamaView view, String username, String level) {
        this.view = view;
        this.username = username;
        this.level = level;
        
        // Set informasi user
        view.setUserInfo(username, level);
        
        // Tambahkan action listener
        view.btnBiodata.addActionListener(e -> bukaBiodata());
        view.btnPengguna.addActionListener(e -> bukaPengguna());
        view.btnHistori.addActionListener(e -> bukaHistori());
        view.btnLogout.addActionListener(e -> logout());
        view.btnKeluar.addActionListener(e -> keluar());
    }
    
    private void bukaBiodata() {
        try {
            BiodataView biodataView = new BiodataView();
            BiodataModel biodataModel = new BiodataModel();
            new BiodataController(biodataView, biodataModel);
            
            JFrame frame = new JFrame("Data Biodata Mahasiswa");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(biodataView);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(view);
            frame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, 
                "Error membuka Biodata: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void bukaPengguna() {
        try {
            PenggunaView penggunaView = new PenggunaView();
            PenggunaModel penggunaModel = new PenggunaModel();
            new PenggunaController(penggunaView, penggunaModel);
            
            JFrame frame = new JFrame("Manajemen Pengguna");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(penggunaView);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(view);
            frame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, 
                "Error membuka Pengguna: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void bukaHistori() {
        try {
            HistoriLoginView historiView = new HistoriLoginView();
            HistoriModel historiModel = new HistoriModel();
            new HistoriController(historiView, historiModel);
            
            JFrame frame = new JFrame("Histori Login");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(historiView);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(view);
            frame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, 
                "Error membuka Histori: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Yakin ingin logout?", "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
            
            // Buka login form lagi
            LoginView loginView = new LoginView();
            PenggunaModel penggunaModel = new PenggunaModel();
            HistoriModel historiModel = new HistoriModel();
            new LoginController(loginView, penggunaModel, historiModel);
            loginView.setVisible(true);
        }
    }
    
    private void keluar() {
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Yakin ingin keluar dari aplikasi?", "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
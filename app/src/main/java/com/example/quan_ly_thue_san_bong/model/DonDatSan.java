package com.example.quan_ly_thue_san_bong.model;

import java.io.Serializable;

public class DonDatSan implements Serializable {
    private int maDon;
    private String maSan;
    private String tenSan;
    private String hoTen;
    private String soDienThoai;
    private String ngayThue;
    private String gioBatDau;
    private int soGioThue;
    private double tongTien;
    private String trangThai;

    public DonDatSan() {
    }

    public DonDatSan(int maDon, String maSan, String tenSan, String hoTen, String soDienThoai,
                     String ngayThue, String gioBatDau, int soGioThue, double tongTien, String trangThai) {
        this.maDon = maDon;
        this.maSan = maSan;
        this.tenSan = tenSan;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.ngayThue = ngayThue;
        this.gioBatDau = gioBatDau;
        this.soGioThue = soGioThue;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public int getMaDon() {
        return maDon;
    }

    public void setMaDon(int maDon) {
        this.maDon = maDon;
    }

    public String getMaSan() {
        return maSan;
    }

    public void setMaSan(String maSan) {
        this.maSan = maSan;
    }

    public String getTenSan() {
        return tenSan;
    }

    public void setTenSan(String tenSan) {
        this.tenSan = tenSan;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getNgayThue() {
        return ngayThue;
    }

    public void setNgayThue(String ngayThue) {
        this.ngayThue = ngayThue;
    }

    public String getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(String gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public int getSoGioThue() {
        return soGioThue;
    }

    public void setSoGioThue(int soGioThue) {
        this.soGioThue = soGioThue;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

package com.example.quan_ly_thue_san_bong.model;

import java.io.Serializable;

public class SanBong implements Serializable {
    private String maSan;
    private String tenSan;
    private String diaChi;
    private double giaThue;
    private String trangThai;

    public SanBong() {
    }

    public SanBong(String maSan, String tenSan, String diaChi, double giaThue, String trangThai) {
        this.maSan = maSan;
        this.tenSan = tenSan;
        this.diaChi = diaChi;
        this.giaThue = giaThue;
        this.trangThai = trangThai;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public double getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(double giaThue) {
        this.giaThue = giaThue;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

package com.example.quan_ly_thue_san_bong.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quan_ly_thue_san_bong.model.DonDatSan;
import com.example.quan_ly_thue_san_bong.model.SanBong;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuanLySanBong.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SAN_BONG = "tbl_san_bong";
    public static final String COLUMN_MA_SAN = "ma_san";
    public static final String COLUMN_TEN_SAN = "ten_san";
    public static final String COLUMN_DIA_CHI = "dia_chi";
    public static final String COLUMN_GIA_THUE = "gia_thue";
    public static final String COLUMN_TRANG_THAI_SAN = "trang_thai";

    public static final String TABLE_DON_DAT_SAN = "tbl_don_dat_san";
    public static final String COLUMN_MA_DON = "ma_don";
    public static final String COLUMN_MA_SAN_DON = "ma_san";
    public static final String COLUMN_TEN_SAN_DON = "ten_san";
    public static final String COLUMN_HO_TEN = "ho_ten";
    public static final String COLUMN_SO_DIEN_THOAI = "so_dien_thoai";
    public static final String COLUMN_NGAY_THUE = "ngay_thue";
    public static final String COLUMN_GIO_BAT_DAU = "gio_bat_dau";
    public static final String COLUMN_SO_GIO_THUE = "so_gio_thue";
    public static final String COLUMN_TONG_TIEN = "tong_tien";
    public static final String COLUMN_TRANG_THAI_DON = "trang_thai";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSanBongTable = "CREATE TABLE " + TABLE_SAN_BONG + " (" +
                COLUMN_MA_SAN + " TEXT PRIMARY KEY, " +
                COLUMN_TEN_SAN + " TEXT NOT NULL, " +
                COLUMN_DIA_CHI + " TEXT NOT NULL, " +
                COLUMN_GIA_THUE + " REAL NOT NULL, " +
                COLUMN_TRANG_THAI_SAN + " TEXT NOT NULL" +
                ");";

        String createDonDatSanTable = "CREATE TABLE " + TABLE_DON_DAT_SAN + " (" +
                COLUMN_MA_DON + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MA_SAN_DON + " TEXT NOT NULL, " +
                COLUMN_TEN_SAN_DON + " TEXT NOT NULL, " +
                COLUMN_HO_TEN + " TEXT NOT NULL, " +
                COLUMN_SO_DIEN_THOAI + " TEXT NOT NULL, " +
                COLUMN_NGAY_THUE + " TEXT NOT NULL, " +
                COLUMN_GIO_BAT_DAU + " TEXT NOT NULL, " +
                COLUMN_SO_GIO_THUE + " INTEGER NOT NULL, " +
                COLUMN_TONG_TIEN + " REAL NOT NULL, " +
                COLUMN_TRANG_THAI_DON + " TEXT NOT NULL" +
                ");";

        db.execSQL(createSanBongTable);
        db.execSQL(createDonDatSanTable);

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_MA_SAN, "SAN01");
        values1.put(COLUMN_TEN_SAN, "Sân cỏ nhân tạo A (5 người)");
        values1.put(COLUMN_DIA_CHI, "123 Nguyễn Trãi, Thanh Xuân, Hà Nội");
        values1.put(COLUMN_GIA_THUE, 200000);
        values1.put(COLUMN_TRANG_THAI_SAN, "Còn trống");
        db.insert(TABLE_SAN_BONG, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_MA_SAN, "SAN02");
        values2.put(COLUMN_TEN_SAN, "Sân cỏ nhân tạo B (7 người)");
        values2.put(COLUMN_DIA_CHI, "456 Lê Văn Lương, Cầu Giấy, Hà Nội");
        values2.put(COLUMN_GIA_THUE, 350000);
        values2.put(COLUMN_TRANG_THAI_SAN, "Còn trống");
        db.insert(TABLE_SAN_BONG, null, values2);

        ContentValues values3 = new ContentValues();
        values3.put(COLUMN_MA_SAN, "SAN03");
        values3.put(COLUMN_TEN_SAN, "Sân tiêu chuẩn C (11 người)");
        values3.put(COLUMN_DIA_CHI, "789 Mỹ Đình, Nam Từ Liêm, Hà Nội");
        values3.put(COLUMN_GIA_THUE, 600000);
        values3.put(COLUMN_TRANG_THAI_SAN, "Còn trống");
        db.insert(TABLE_SAN_BONG, null, values3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAN_BONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DON_DAT_SAN);
        onCreate(db);
    }

    public List<SanBong> getAllSanBong() {
        List<SanBong> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SAN_BONG, null);
        if (cursor.moveToFirst()) {
            do {
                SanBong san = new SanBong(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_SAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_SAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIA_CHI)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GIA_THUE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRANG_THAI_SAN))
                );
                list.add(san);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public SanBong getSanBongByMa(String maSan) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SAN_BONG, null, COLUMN_MA_SAN + "=?",
                new String[]{maSan}, null, null, null);
        SanBong san = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                san = new SanBong(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_SAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_SAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIA_CHI)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GIA_THUE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRANG_THAI_SAN))
                );
            }
            cursor.close();
        }
        return san;
    }

    public boolean insertSanBong(SanBong sanBong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_SAN, sanBong.getMaSan());
        values.put(COLUMN_TEN_SAN, sanBong.getTenSan());
        values.put(COLUMN_DIA_CHI, sanBong.getDiaChi());
        values.put(COLUMN_GIA_THUE, sanBong.getGiaThue());
        values.put(COLUMN_TRANG_THAI_SAN, sanBong.getTrangThai() != null ? sanBong.getTrangThai() : "Còn trống");

        long result = db.insert(TABLE_SAN_BONG, null, values);
        return result != -1;
    }

    public boolean deleteSanBong(String maSan) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_SAN_BONG, COLUMN_MA_SAN + "=?", new String[]{maSan});
        return rows > 0;
    }

    public void updateTrangThaiSan(String maSan, String trangThai) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANG_THAI_SAN, trangThai);
        db.update(TABLE_SAN_BONG, values, COLUMN_MA_SAN + "=?", new String[]{maSan});
    }

    public void updateAllSanBongStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesReset = new ContentValues();
        valuesReset.put(COLUMN_TRANG_THAI_SAN, "Còn trống");
        db.update(TABLE_SAN_BONG, valuesReset, null, null);

        String query = "SELECT DISTINCT " + COLUMN_MA_SAN_DON + " FROM " + TABLE_DON_DAT_SAN +
                " WHERE " + COLUMN_TRANG_THAI_DON + " = 'Đang đặt'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String maSan = cursor.getString(0);
                ContentValues values = new ContentValues();
                values.put(COLUMN_TRANG_THAI_SAN, "Đã được đặt");
                db.update(TABLE_SAN_BONG, values, COLUMN_MA_SAN + "=?", new String[]{maSan});
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public List<DonDatSan> getAllDonDatSan() {
        List<DonDatSan> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DON_DAT_SAN + " ORDER BY " + COLUMN_MA_DON + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                DonDatSan don = new DonDatSan(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MA_DON)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_SAN_DON)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_SAN_DON)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HO_TEN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SO_DIEN_THOAI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NGAY_THUE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GIO_BAT_DAU)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SO_GIO_THUE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TONG_TIEN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRANG_THAI_DON))
                );
                list.add(don);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean insertDonDatSan(DonDatSan don) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_SAN_DON, don.getMaSan());
        values.put(COLUMN_TEN_SAN_DON, don.getTenSan());
        values.put(COLUMN_HO_TEN, don.getHoTen());
        values.put(COLUMN_SO_DIEN_THOAI, don.getSoDienThoai());
        values.put(COLUMN_NGAY_THUE, don.getNgayThue());
        values.put(COLUMN_GIO_BAT_DAU, don.getGioBatDau());
        values.put(COLUMN_SO_GIO_THUE, don.getSoGioThue());
        values.put(COLUMN_TONG_TIEN, don.getTongTien());
        values.put(COLUMN_TRANG_THAI_DON, "Đang đặt");

        long result = db.insert(TABLE_DON_DAT_SAN, null, values);
        if (result != -1) {
            updateTrangThaiSan(don.getMaSan(), "Đã được đặt");
            return true;
        }
        return false;
    }

    public boolean cancelDonDatSan(int maDon, String maSan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANG_THAI_DON, "Đã hủy");
        int rows = db.update(TABLE_DON_DAT_SAN, values, COLUMN_MA_DON + "=?", new String[]{String.valueOf(maDon)});
        if (rows > 0) {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DON_DAT_SAN +
                    " WHERE " + COLUMN_MA_SAN_DON + "=? AND " + COLUMN_TRANG_THAI_DON + "='Đang đặt'", new String[]{maSan});
            if (cursor.moveToFirst()) {
                int activeCount = cursor.getInt(0);
                if (activeCount == 0) {
                    updateTrangThaiSan(maSan, "Còn trống");
                }
            }
            cursor.close();
            return true;
        }
        return false;
    }

    public boolean isTimeOverlapping(String maSan, String ngayThue, String gioBatDau, int soGioThue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_GIO_BAT_DAU + ", " + COLUMN_SO_GIO_THUE +
                " FROM " + TABLE_DON_DAT_SAN +
                " WHERE " + COLUMN_MA_SAN_DON + "=? AND " + COLUMN_NGAY_THUE + "=? AND " + COLUMN_TRANG_THAI_DON + "='Đang đặt'";

        Cursor cursor = db.rawQuery(query, new String[]{maSan, ngayThue});

        int newStartMinutes = parseTimeToMinutes(gioBatDau);
        int newEndMinutes = newStartMinutes + (soGioThue * 60);

        boolean isOverlap = false;
        if (cursor.moveToFirst()) {
            do {
                String existingTimeStr = cursor.getString(0);
                int existingDuration = cursor.getInt(1);

                int existingStartMinutes = parseTimeToMinutes(existingTimeStr);
                int existingEndMinutes = existingStartMinutes + (existingDuration * 60);

                if (Math.max(newStartMinutes, existingStartMinutes) < Math.min(newEndMinutes, existingEndMinutes)) {
                    isOverlap = true;
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return isOverlap;
    }

    private int parseTimeToMinutes(String timeStr) {
        try {
            String[] parts = timeStr.split(":");
            int hours = Integer.parseInt(parts[0].trim());
            int minutes = Integer.parseInt(parts[1].trim());
            return hours * 60 + minutes;
        } catch (Exception e) {
            return 0;
        }
    }
}

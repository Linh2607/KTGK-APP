package com.example.quan_ly_thue_san_bong.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_thue_san_bong.R;
import com.example.quan_ly_thue_san_bong.adapter.DonDatSanAdapter;
import com.example.quan_ly_thue_san_bong.database.DatabaseHelper;
import com.example.quan_ly_thue_san_bong.model.DonDatSan;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DonDatSanFragment extends Fragment implements DonDatSanAdapter.OnDonDatActionListener {

    private RecyclerView rvDonDatSan;
    private TextView tvEmptyDon;
    private DonDatSanAdapter adapter;
    private List<DonDatSan> listDonDat = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private DecimalFormat currencyFormat = new DecimalFormat("#,### VNĐ");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_don_dat_san, container, false);

        rvDonDatSan = view.findViewById(R.id.rvDonDatSan);
        tvEmptyDon = view.findViewById(R.id.tvEmptyDon);

        dbHelper = new DatabaseHelper(requireContext());

        rvDonDatSan.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new DonDatSanAdapter(requireContext(), listDonDat, this);
        rvDonDatSan.setAdapter(adapter);

        loadData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        if (dbHelper != null) {
            listDonDat = dbHelper.getAllDonDatSan();
            adapter.setList(listDonDat);

            if (listDonDat.isEmpty()) {
                tvEmptyDon.setVisibility(View.VISIBLE);
                rvDonDatSan.setVisibility(View.GONE);
            } else {
                tvEmptyDon.setVisibility(View.GONE);
                rvDonDatSan.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDetail(DonDatSan donDatSan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_detail_don_dat, null);
        builder.setView(dialogView);

        TextView tvDetailMaDon = dialogView.findViewById(R.id.tvDetailMaDon);
        TextView tvDetailNguoiDat = dialogView.findViewById(R.id.tvDetailNguoiDat);
        TextView tvDetailSDT = dialogView.findViewById(R.id.tvDetailSDT);
        TextView tvDetailTenSan = dialogView.findViewById(R.id.tvDetailTenSan);
        TextView tvDetailNgay = dialogView.findViewById(R.id.tvDetailNgay);
        TextView tvDetailGio = dialogView.findViewById(R.id.tvDetailGio);
        TextView tvDetailSoGio = dialogView.findViewById(R.id.tvDetailSoGio);
        TextView tvDetailTongTien = dialogView.findViewById(R.id.tvDetailTongTien);
        TextView tvDetailTrangThai = dialogView.findViewById(R.id.tvDetailTrangThai);

        tvDetailMaDon.setText("Mã đơn: #" + donDatSan.getMaDon());
        tvDetailNguoiDat.setText("Họ tên: " + donDatSan.getHoTen());
        tvDetailSDT.setText("Số điện thoại: " + donDatSan.getSoDienThoai());
        tvDetailTenSan.setText("Sân bóng: " + donDatSan.getTenSan() + " (" + donDatSan.getMaSan() + ")");
        tvDetailNgay.setText("Ngày thuê: " + donDatSan.getNgayThue());
        tvDetailGio.setText("Giờ bắt đầu: " + donDatSan.getGioBatDau());
        tvDetailSoGio.setText("Số giờ thuê: " + donDatSan.getSoGioThue() + " giờ");
        tvDetailTongTien.setText("Tổng tiền: " + currencyFormat.format(donDatSan.getTongTien()));
        tvDetailTrangThai.setText("Trạng thái: " + donDatSan.getTrangThai());

        builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public void onCancel(DonDatSan donDatSan) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận hủy đơn")
                .setMessage("Bạn có chắc chắn muốn hủy đơn đặt sân: " + donDatSan.getTenSan() + " ngày " + donDatSan.getNgayThue() + "?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> {
                    boolean success = dbHelper.cancelDonDatSan(donDatSan.getMaDon(), donDatSan.getMaSan());
                    if (success) {
                        Toast.makeText(requireContext(), "Đã hủy đơn đặt sân thành công!", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(requireContext(), "Hủy đơn thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Quay lại", null)
                .show();
    }
}

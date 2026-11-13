package com.example.quan_ly_thue_san_bong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_thue_san_bong.R;
import com.example.quan_ly_thue_san_bong.model.DonDatSan;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.List;

public class DonDatSanAdapter extends RecyclerView.Adapter<DonDatSanAdapter.DonDatViewHolder> {

    public interface OnDonDatActionListener {
        void onDetail(DonDatSan donDatSan);
        void onCancel(DonDatSan donDatSan);
    }

    private Context context;
    private List<DonDatSan> list;
    private OnDonDatActionListener listener;
    private DecimalFormat currencyFormat = new DecimalFormat("#,### VNĐ");

    public DonDatSanAdapter(Context context, List<DonDatSan> list, OnDonDatActionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<DonDatSan> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DonDatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_don_dat_san, parent, false);
        return new DonDatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonDatViewHolder holder, int position) {
        DonDatSan don = list.get(position);
        holder.tvNguoiDat.setText(don.getHoTen() + " (" + don.getSoDienThoai() + ")");
        holder.tvTenSanDon.setText("Sân: " + don.getTenSan());
        holder.tvThoiGian.setText("Ngày: " + don.getNgayThue() + " | Giờ: " + don.getGioBatDau() + " (" + don.getSoGioThue() + " giờ)");
        holder.tvTongTienDon.setText("Tổng tiền: " + currencyFormat.format(don.getTongTien()));

        if ("Đang đặt".equalsIgnoreCase(don.getTrangThai())) {
            holder.tvTrangThaiDon.setText("Đang đặt");
            holder.tvTrangThaiDon.setBackgroundResource(R.drawable.bg_status_booked);
            holder.btnCancel.setVisibility(View.VISIBLE);
        } else {
            holder.tvTrangThaiDon.setText("Đã hủy");
            holder.tvTrangThaiDon.setBackgroundResource(R.drawable.bg_status_canceled);
            holder.btnCancel.setVisibility(View.GONE);
        }

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) listener.onDetail(don);
        });

        holder.btnCancel.setOnClickListener(v -> {
            if (listener != null) listener.onCancel(don);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class DonDatViewHolder extends RecyclerView.ViewHolder {
        TextView tvNguoiDat, tvTenSanDon, tvThoiGian, tvTongTienDon, tvTrangThaiDon;
        MaterialButton btnDetail, btnCancel;

        public DonDatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNguoiDat = itemView.findViewById(R.id.tvNguoiDat);
            tvTenSanDon = itemView.findViewById(R.id.tvTenSanDon);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            tvTongTienDon = itemView.findViewById(R.id.tvTongTienDon);
            tvTrangThaiDon = itemView.findViewById(R.id.tvTrangThaiDon);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}

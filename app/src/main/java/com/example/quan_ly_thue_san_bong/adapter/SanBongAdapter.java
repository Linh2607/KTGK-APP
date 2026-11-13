package com.example.quan_ly_thue_san_bong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_thue_san_bong.R;
import com.example.quan_ly_thue_san_bong.model.SanBong;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.List;

public class SanBongAdapter extends RecyclerView.Adapter<SanBongAdapter.SanBongViewHolder> {

    public interface OnSanBongActionListener {
        void onDelete(SanBong sanBong);
        void onBook(SanBong sanBong);
    }

    private Context context;
    private List<SanBong> list;
    private OnSanBongActionListener listener;
    private DecimalFormat currencyFormat = new DecimalFormat("#,### VNĐ/giờ");

    public SanBongAdapter(Context context, List<SanBong> list, OnSanBongActionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<SanBong> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SanBongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_bong, parent, false);
        return new SanBongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanBongViewHolder holder, int position) {
        SanBong san = list.get(position);
        holder.tvMaSan.setText(san.getMaSan());
        holder.tvTenSan.setText(san.getTenSan());
        holder.tvDiaChi.setText("Địa chỉ: " + san.getDiaChi());
        holder.tvGiaThue.setText("Giá thuê: " + currencyFormat.format(san.getGiaThue()));

        if ("Còn trống".equalsIgnoreCase(san.getTrangThai())) {
            holder.tvTrangThai.setText("Còn trống");
            holder.tvTrangThai.setBackgroundResource(R.drawable.bg_status_available);
        } else {
            holder.tvTrangThai.setText("Đã được đặt");
            holder.tvTrangThai.setBackgroundResource(R.drawable.bg_status_booked);
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(san);
        });

        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) listener.onBook(san);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class SanBongViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaSan, tvTenSan, tvDiaChi, tvGiaThue, tvTrangThai;
        MaterialButton btnDelete, btnBook;

        public SanBongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaSan = itemView.findViewById(R.id.tvMaSan);
            tvTenSan = itemView.findViewById(R.id.tvTenSan);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
            tvGiaThue = itemView.findViewById(R.id.tvGiaThue);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}

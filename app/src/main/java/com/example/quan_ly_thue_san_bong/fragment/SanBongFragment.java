package com.example.quan_ly_thue_san_bong.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_thue_san_bong.MainActivity;
import com.example.quan_ly_thue_san_bong.R;
import com.example.quan_ly_thue_san_bong.adapter.SanBongAdapter;
import com.example.quan_ly_thue_san_bong.database.DatabaseHelper;
import com.example.quan_ly_thue_san_bong.model.SanBong;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SanBongFragment extends Fragment implements SanBongAdapter.OnSanBongActionListener {

    private RecyclerView rvSanBong;
    private TextView tvEmpty;
    private FloatingActionButton fabAddSan;
    private SanBongAdapter adapter;
    private List<SanBong> listSanBong = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_san_bong, container, false);

        rvSanBong = view.findViewById(R.id.rvSanBong);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        fabAddSan = view.findViewById(R.id.fabAddSan);

        dbHelper = new DatabaseHelper(requireContext());

        rvSanBong.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SanBongAdapter(requireContext(), listSanBong, this);
        rvSanBong.setAdapter(adapter);

        fabAddSan.setOnClickListener(v -> showAddSanDialog());

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
            dbHelper.updateAllSanBongStatus();
            listSanBong = dbHelper.getAllSanBong();
            adapter.setList(listSanBong);

            if (listSanBong.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvSanBong.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvSanBong.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showAddSanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_san_bong, null);
        builder.setView(dialogView);

        EditText etMaSan = dialogView.findViewById(R.id.etMaSan);
        EditText etTenSan = dialogView.findViewById(R.id.etTenSan);
        EditText etDiaChi = dialogView.findViewById(R.id.etDiaChi);
        EditText etGiaThue = dialogView.findViewById(R.id.etGiaThue);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String maSan = etMaSan.getText().toString().trim();
            String tenSan = etTenSan.getText().toString().trim();
            String diaChi = etDiaChi.getText().toString().trim();
            String giaThueStr = etGiaThue.getText().toString().trim();

            if (TextUtils.isEmpty(maSan) || TextUtils.isEmpty(tenSan) ||
                    TextUtils.isEmpty(diaChi) || TextUtils.isEmpty(giaThueStr)) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.getSanBongByMa(maSan) != null) {
                Toast.makeText(requireContext(), "Mã sân bóng đã tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }

            double giaThue;
            try {
                giaThue = Double.parseDouble(giaThueStr);
                if (giaThue <= 0) {
                    Toast.makeText(requireContext(), "Giá thuê phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Giá thuê không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            SanBong sanMoi = new SanBong(maSan, tenSan, diaChi, giaThue, "Còn trống");
            boolean success = dbHelper.insertSanBong(sanMoi);
            if (success) {
                Toast.makeText(requireContext(), "Thêm sân bóng thành công!", Toast.LENGTH_SHORT).show();
                loadData();
            } else {
                Toast.makeText(requireContext(), "Thêm sân bóng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    @Override
    public void onDelete(SanBong sanBong) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sân: " + sanBong.getTenSan() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean success = dbHelper.deleteSanBong(sanBong.getMaSan());
                    if (success) {
                        Toast.makeText(requireContext(), "Xóa sân bóng thành công!", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(requireContext(), "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onBook(SanBong sanBong) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToDatSan(sanBong.getMaSan());
        }
    }
}

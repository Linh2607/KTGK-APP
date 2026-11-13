package com.example.quan_ly_thue_san_bong.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quan_ly_thue_san_bong.MainActivity;
import com.example.quan_ly_thue_san_bong.R;
import com.example.quan_ly_thue_san_bong.database.DatabaseHelper;
import com.example.quan_ly_thue_san_bong.model.DonDatSan;
import com.example.quan_ly_thue_san_bong.model.SanBong;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatSanFragment extends Fragment {

    private Spinner spSanBong;
    private TextView tvGiaGoiY, tvTongTienCalculate;
    private EditText etHoTen, etSoDienThoai, etNgayThue, etGioBatDau, etSoGioThue;
    private MaterialButton btnSubmitDatSan;

    private DatabaseHelper dbHelper;
    private List<SanBong> listSanBong = new ArrayList<>();
    private SanBong selectedSanBong = null;

    private DecimalFormat currencyFormat = new DecimalFormat("#,### VNĐ");
    private String preSelectedMaSan = null;

    public void setPreSelectedMaSan(String maSan) {
        this.preSelectedMaSan = maSan;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dat_san, container, false);

        spSanBong = view.findViewById(R.id.spSanBong);
        tvGiaGoiY = view.findViewById(R.id.tvGiaGoiY);
        tvTongTienCalculate = view.findViewById(R.id.tvTongTienCalculate);
        etHoTen = view.findViewById(R.id.etHoTen);
        etSoDienThoai = view.findViewById(R.id.etSoDienThoai);
        etNgayThue = view.findViewById(R.id.etNgayThue);
        etGioBatDau = view.findViewById(R.id.etGioBatDau);
        etSoGioThue = view.findViewById(R.id.etSoGioThue);
        btnSubmitDatSan = view.findViewById(R.id.btnSubmitDatSan);

        dbHelper = new DatabaseHelper(requireContext());

        setupDatePicker();
        setupTimePicker();
        setupSpinner();

        etSoGioThue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSubmitDatSan.setOnClickListener(v -> performBooking());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSanBongList();
    }

    private void loadSanBongList() {
        listSanBong = dbHelper.getAllSanBong();
        List<String> displayNames = new ArrayList<>();
        for (SanBong san : listSanBong) {
            displayNames.add(san.getTenSan() + " (" + san.getMaSan() + ")");
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, displayNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSanBong.setAdapter(spinnerAdapter);

        if (preSelectedMaSan != null && !listSanBong.isEmpty()) {
            for (int i = 0; i < listSanBong.size(); i++) {
                if (listSanBong.get(i).getMaSan().equalsIgnoreCase(preSelectedMaSan)) {
                    spSanBong.setSelection(i);
                    break;
                }
            }
            preSelectedMaSan = null;
        }
    }

    private void setupSpinner() {
        spSanBong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < listSanBong.size()) {
                    selectedSanBong = listSanBong.get(position);
                    tvGiaGoiY.setText("Giá thuê: " + currencyFormat.format(selectedSanBong.getGiaThue()) + "/giờ");
                    calculateTotal();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSanBong = null;
            }
        });
    }

    private void setupDatePicker() {
        etNgayThue.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
                String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                etNgayThue.setText(formattedDate);
            }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });
    }

    private void setupTimePicker() {
        etGioBatDau.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute1) -> {
                String formattedTime = String.format("%02d:%02d", hourOfDay, minute1);
                etGioBatDau.setText(formattedTime);
            }, hour, minute, true);
            dialog.show();
        });
    }

    private void calculateTotal() {
        if (selectedSanBong == null) {
            tvTongTienCalculate.setText("TỔNG TIỀN: 0 VNĐ");
            return;
        }

        String soGioStr = etSoGioThue.getText().toString().trim();
        if (TextUtils.isEmpty(soGioStr)) {
            tvTongTienCalculate.setText("TỔNG TIỀN: 0 VNĐ");
            return;
        }

        try {
            int soGio = Integer.parseInt(soGioStr);
            if (soGio > 0) {
                double total = selectedSanBong.getGiaThue() * soGio;
                tvTongTienCalculate.setText("TỔNG TIỀN: " + currencyFormat.format(total));
            } else {
                tvTongTienCalculate.setText("TỔNG TIỀN: 0 VNĐ");
            }
        } catch (NumberFormatException e) {
            tvTongTienCalculate.setText("TỔNG TIỀN: 0 VNĐ");
        }
    }

    private void performBooking() {
        if (selectedSanBong == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn sân bóng!", Toast.LENGTH_SHORT).show();
            return;
        }

        String hoTen = etHoTen.getText().toString().trim();
        String soDienThoai = etSoDienThoai.getText().toString().trim();
        String ngayThue = etNgayThue.getText().toString().trim();
        String gioBatDau = etGioBatDau.getText().toString().trim();
        String soGioStr = etSoGioThue.getText().toString().trim();

        if (TextUtils.isEmpty(hoTen) || TextUtils.isEmpty(soDienThoai) ||
                TextUtils.isEmpty(ngayThue) || TextUtils.isEmpty(gioBatDau) || TextUtils.isEmpty(soGioStr)) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ các trường thông tin bắt buộc!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (soDienThoai.length() < 9 || !soDienThoai.matches("\\d+")) {
            Toast.makeText(requireContext(), "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        int soGioThue;
        try {
            soGioThue = Integer.parseInt(soGioStr);
            if (soGioThue <= 0) {
                Toast.makeText(requireContext(), "Số giờ thuê phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Số giờ thuê không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isOverlapping = dbHelper.isTimeOverlapping(
                selectedSanBong.getMaSan(), ngayThue, gioBatDau, soGioThue);

        if (isOverlapping) {
            Toast.makeText(requireContext(), "LỖI: Sân " + selectedSanBong.getTenSan() +
                    " đã có lịch đặt trùng vào khung giờ này (" + ngayThue + " " + gioBatDau + ")!", Toast.LENGTH_LONG).show();
            return;
        }

        double tongTien = selectedSanBong.getGiaThue() * soGioThue;

        DonDatSan donMoi = new DonDatSan(
                0,
                selectedSanBong.getMaSan(),
                selectedSanBong.getTenSan(),
                hoTen,
                soDienThoai,
                ngayThue,
                gioBatDau,
                soGioThue,
                tongTien,
                "Đang đặt"
        );

        boolean success = dbHelper.insertDonDatSan(donMoi);
        if (success) {
            Toast.makeText(requireContext(), "Đặt sân bóng thành công!", Toast.LENGTH_SHORT).show();
            clearForm();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToDonDat();
            }
        } else {
            Toast.makeText(requireContext(), "Đặt sân thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        etHoTen.setText("");
        etSoDienThoai.setText("");
        etNgayThue.setText("");
        etGioBatDau.setText("");
        etSoGioThue.setText("1");
        calculateTotal();
    }
}

package com.example.quan_ly_thue_san_bong;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.quan_ly_thue_san_bong.fragment.DatSanFragment;
import com.example.quan_ly_thue_san_bong.fragment.DonDatSanFragment;
import com.example.quan_ly_thue_san_bong.fragment.SanBongFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private final SanBongFragment sanBongFragment = new SanBongFragment();
    private final DatSanFragment datSanFragment = new DatSanFragment();
    private final DonDatSanFragment donDatSanFragment = new DonDatSanFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, sanBongFragment)
                .commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_san_bong) {
                replaceFragment(sanBongFragment);
                sanBongFragment.loadData();
                return true;
            } else if (itemId == R.id.nav_dat_san) {
                replaceFragment(datSanFragment);
                return true;
            } else if (itemId == R.id.nav_don_dat) {
                replaceFragment(donDatSanFragment);
                donDatSanFragment.loadData();
                return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void navigateToDatSan(String maSan) {
        datSanFragment.setPreSelectedMaSan(maSan);
        bottomNavigationView.setSelectedItemId(R.id.nav_dat_san);
    }

    public void navigateToDonDat() {
        bottomNavigationView.setSelectedItemId(R.id.nav_don_dat);
    }
}

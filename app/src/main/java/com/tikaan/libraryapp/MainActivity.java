package com.tikaan.libraryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tikaan.libraryapp.fragments.AddFragment;
import com.tikaan.libraryapp.fragments.FavouriteFragment;
import com.tikaan.libraryapp.fragments.HomeFragment;
import com.tikaan.libraryapp.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCustomActionBar();
        setupNavBar();
    }

    private void setupNavBar(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Fragment fragmentHome = new HomeFragment();
        Fragment searchFragment = new SearchFragment();
        Fragment favouriteFragment = new FavouriteFragment();

        setCurrentFragment(fragmentHome);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                setCurrentFragment(fragmentHome);
            } else if (id == R.id.nav_search) {
                setCurrentFragment(searchFragment);
            } else if (id == R.id.nav_favorite) {
                setCurrentFragment(favouriteFragment);
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }

    private void setCustomActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView btnAdd = findViewById(R.id.btn_add);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrentFragment(new AddFragment());
                }
            });
        }
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
}

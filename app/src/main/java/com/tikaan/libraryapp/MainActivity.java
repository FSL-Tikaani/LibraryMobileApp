package com.tikaan.libraryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tikaan.libraryapp.fragments.EditFragment;
import com.tikaan.libraryapp.fragments.FavouriteFragment;
import com.tikaan.libraryapp.fragments.HomeFragment;
import com.tikaan.libraryapp.fragments.SearchFragment;


/**
 * Главная база данных приложения Room
 * Содержит таблицу книг и использует конвертеры для сложных типов данных
 */

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
        // Создаем фрагменты заранее для переиспользования
        Fragment fragmentHome = new HomeFragment();
        Fragment searchFragment = new SearchFragment();
        Fragment favouriteFragment = new FavouriteFragment();

        // Устанавливаем начальный фрагмент при запуске
        setCurrentFragment(fragmentHome);

        // Обработчик выбора пунктов нижней навигации
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                setCurrentFragment(fragmentHome);
            } else if (id == R.id.nav_search) {
                setCurrentFragment(searchFragment);
            } else if (id == R.id.nav_favorite) {
                setCurrentFragment(favouriteFragment);
            }
            return true; // Возвращаем true для подсветки выбранного пункта
        });
    }

    /**
     * Метод для замены фрагментов в контейнере
     * Использует FragmentManager для управления транзакциями фрагментов
     */
    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment) // Замена текущего фрагмента в контейнере
                .commit(); // Немедленное выполнение транзакции
    }

    /**
     * Публичный метод для навигации из других классов
     * Например, для перехода к редактированию книги из списка
     */
    public void navigateToFragment(Fragment fragment) {
        setCurrentFragment(fragment);
    }

    private void setCustomActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView btnAdd = findViewById(R.id.btn_add);

        // Обработчик кнопки добавления новой книги
        if (btnAdd != null) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Создаем новый экземпляр фрагмента редактирования при каждом нажатии
                    setCurrentFragment(new EditFragment());
                }
            });
        }
        setSupportActionBar(toolbar);

        // Скрываем стандартный заголовок ActionBar для кастомного отображения
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
}
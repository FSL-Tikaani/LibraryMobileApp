package com.tikaan.libraryapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tikaan.libraryapp.adapter.BookCardsAdapter;
import com.tikaan.libraryapp.adapter.VerticalSpaceItemDecoration;
import com.tikaan.libraryapp.model.BookModel;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<BookModel> books = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateData();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        BookCardsAdapter bookCardsAdapter = new BookCardsAdapter(this.getLayoutInflater(), books);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(50));
        recyclerView.setAdapter(bookCardsAdapter);
    }

    private void generateData() {
        books.add(new BookModel(
                "1",
                "Мастер и Маргарита",
                "Фантасмагорическая история о любви, добре и зле в советской Москве",
                "Михаил Булгаков",
                Arrays.asList("классика", "мистика", "роман"),
                "https://example.com/images/master.jpg"
        ));

        books.add(new BookModel(
                "2",
                "1984",
                "Антиутопия о тоталитарном обществе под постоянным контролем Большого Брата",
                "Джордж Оруэлл",
                Arrays.asList("антиутопия", "политика", "научная фантастика"),
                "https://example.com/images/1984.jpg"
        ));

        books.add(new BookModel(
                "3",
                "Три товарища",
                "История о дружбе и любви в период экономического кризиса в Германии",
                "Эрих Мария Ремарк",
                Arrays.asList("классика", "военная проза"),
                "https://example.com/images/three_comrades.jpg"
        ));

        books.add(new BookModel(
                "4",
                "Маленький принц",
                "Философская сказка о дружбе, любви и ответственности",
                "Антуан де Сент-Экзюпери",
                Arrays.asList("сказка", "философия", "детская литература"),
                "https://example.com/images/little_prince.jpg"
        ));

        books.add(new BookModel(
                "5",
                "Маленький принц",
                "Философская сказка о дружбе, любви и ответственности",
                "Антуан де Сент-Экзюпери",
                Arrays.asList("сказка", "философия", "детская литература"),
                "https://example.com/images/little_prince.jpg"
        ));
    }

}

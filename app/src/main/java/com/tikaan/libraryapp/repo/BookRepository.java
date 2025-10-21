package com.tikaan.libraryapp.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.tikaan.libraryapp.model.BookModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookRepository {
    private BooksDao booksDao;
    private ExecutorService executorService;

    public BookRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        booksDao = database.booksDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<BookModel>> getAllBooks(){
        return booksDao.getAllBooks();
    }

    public LiveData<BookModel> getBook(int id){
        return booksDao.getBookFromId(id);
    }

    public LiveData<List<BookModel>> getFavouriteBooks(){
        return booksDao.getFavourite();
    }

    public LiveData<List<BookModel>> searchBook(String query){
        return booksDao.searchProducts(query);
    }

    public void insertBook(BookModel bookModel){
        executorService.execute(
                () -> booksDao.insertProduct(bookModel)
        );
    }

    public void updateFavouriteStatus(int bookId, boolean isFavourite){
        executorService.execute(
                () -> booksDao.updateFavoriteStatus(bookId, isFavourite)
        );
    }

    public void deleteBook(BookModel bookModel){
        executorService.execute(
                () -> booksDao.deleteProduct(bookModel)
        );
    }

    public void generateTestData(){
        List<BookModel> books = new ArrayList<>();

        books.add(new BookModel(
                1,
                "Мастер и Маргарита",
                "Фантасмагорическая история о любви, добре и зле в советской Москве",
                "Михаил Булгаков",
                Arrays.asList("классика", "мистика", "роман"),
                true,
                "https://example.com/images/master.jpg"
        ));

        books.add(new BookModel(
                2,
                "1984",
                "Антиутопия о тоталитарном обществе под постоянным контролем Большого Брата",
                "Джордж Оруэлл",
                Arrays.asList("антиутопия", "политика", "научная фантастика"),
                false,
                "https://example.com/images/1984.jpg"
        ));

        books.add(new BookModel(
                3,
                "Три товарища",
                "История о дружбе и любви в период экономического кризиса в Германии",
                "Эрих Мария Ремарк",
                Arrays.asList("классика", "военная проза"),
                true,
                "https://example.com/images/three_comrades.jpg"
        ));

        books.add(new BookModel(
                4,
                "Маленький принц",
                "Философская сказка о дружбе, любви и ответственности",
                "Антуан де Сент-Экзюпери",
                Arrays.asList("сказка", "философия", "детская литература"),
                true,
                "https://example.com/images/little_prince.jpg"
        ));

        books.add(new BookModel(
                5,
                "Маленький принц",
                "Философская сказка о дружбе, любви и ответственности",
                "Антуан де Сент-Экзюпери",
                Arrays.asList("сказка", "философия", "детская литература"),
                false,
                "https://example.com/images/little_prince.jpg"
        ));

        executorService.execute(() -> {
            for(BookModel bookModel: books){
                booksDao.insertProduct(bookModel);
            }
        });
    }




}

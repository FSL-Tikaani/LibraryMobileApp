package com.tikaan.libraryapp.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.tikaan.libraryapp.model.BookModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookRepository {
    private BooksDao booksDao;
    private ExecutorService executorService;

    public BookRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        booksDao = database.booksDao();
        executorService = Executors.newSingleThreadExecutor();
        Log.d("BookRepository", "Репозиторий инициализирован");
    }

    public LiveData<List<BookModel>> getAllBooks(){
        return booksDao.getAllBooks();
    }

    public LiveData<BookModel> getBook(String id){ // Изменено на String
        return booksDao.getBookFromId(id);
    }

    public LiveData<List<BookModel>> getFavouriteBooks(){
        return booksDao.getFavourite();
    }

    public LiveData<List<BookModel>> searchBook(String query){
        return booksDao.searchProducts(query);
    }

    public void insertBook(BookModel bookModel){
        Log.d("BookRepository", "Вставка книги: " + bookModel.getTitle() + ", ID: " + bookModel.getId());
        executorService.execute(() -> {
            try {
                booksDao.insertProduct(bookModel);
                Log.d("BookRepository", "Книга успешно вставлена в DAO");
            } catch (Exception e) {
                Log.e("BookRepository", "Ошибка вставки книги: ", e);
            }
        });
    }

    public void updateFavouriteStatus(String bookId, boolean isFavourite){ // Изменено на String
        executorService.execute(() -> booksDao.updateFavoriteStatus(bookId, isFavourite));
    }

    public void deleteBook(BookModel bookModel){
        executorService.execute(() -> booksDao.deleteProduct(bookModel));
    }

    public void updateBook(BookModel bookModel){
        Log.d("BookRepository", "Обновление книги: " + bookModel.getTitle() + ", ID: " + bookModel.getId());
        executorService.execute(() -> {
            try {
                booksDao.updateProduct(bookModel);
                Log.d("BookRepository", "Книга успешно обновлена в DAO");
            } catch (Exception e) {
                Log.e("BookRepository", "Ошибка обновления книги: ", e);
            }
        });
    }
}
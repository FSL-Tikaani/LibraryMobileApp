package com.tikaan.libraryapp.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.tikaan.libraryapp.model.BookModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Репозиторий для работы с данными книг
 * Служит прослойкой между ViewModel и Room DAO
 * Обрабатывает асинхронные операции в фоновом потоке
 */
public class BookRepository {
    private BooksDao booksDao;
    private ExecutorService executorService;

    /**
     * Конструктор репозитория
     */
    public BookRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        booksDao = database.booksDao();
        executorService = Executors.newSingleThreadExecutor(); // Создание фонового потока
        Log.d("BookRepository", "Репозиторий инициализирован");
    }

    /**
     * Получение всех книг из базы данных
     */
    public LiveData<List<BookModel>> getAllBooks(){
        return booksDao.getAllBooks();
    }

    /**
     * Получение конкретной книги по ID
     */
    public LiveData<BookModel> getBook(String id){
        return booksDao.getBookFromId(id);
    }

    /**
     * Получение списка избранных книг
     */
    public LiveData<List<BookModel>> getFavouriteBooks(){
        return booksDao.getFavourite();
    }

    /**
     * Поиск книг по запросу
     */
    public LiveData<List<BookModel>> searchBook(String query){
        return booksDao.searchProducts(query);
    }

    /**
     * Добавление новой книги в базу данных
     */
    public void insertBook(BookModel bookModel){
        executorService.execute(() -> {
            try {
                booksDao.insertProduct(bookModel);
            } catch (Exception e) {
                Log.e("BookRepository", "Ошибка вставки книги: ", e);
            }
        });
    }

    /**
     * Обновление статуса избранного для книги
     */
    public void updateFavouriteStatus(String bookId, boolean isFavourite){
        executorService.execute(() -> booksDao.updateFavoriteStatus(bookId, isFavourite));
    }

    /**
     * Удаление книги из базы данных
     */
    public void deleteBook(BookModel bookModel){
        executorService.execute(() -> booksDao.deleteProduct(bookModel));
    }

    /**
     * Обновление данных книги
     */
    public void updateBook(BookModel bookModel){
        executorService.execute(() -> {
            try {
                booksDao.updateProduct(bookModel);
            } catch (Exception e) {
                Log.e("BookRepository", "Ошибка обновления книги: ", e);
            }
        });
    }
}
package com.tikaan.libraryapp.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tikaan.libraryapp.model.BookModel;

import java.util.List;

/**
 * DAO для работы с таблицей книг
 * Определяет методы доступа к данным с использованием Room
 */
@Dao
public interface BooksDao {

    /**
     * Получение всех книг из базы данных
     */
    @Query("SELECT * FROM books")
    LiveData<List<BookModel>> getAllBooks();

    /**
     * Получение книги по идентификатору
     */
    @Query("SELECT * FROM books WHERE id = :id")
    LiveData<BookModel> getBookFromId(String id);

    /**
     * Получение списка избранных книг
     */
    @Query("SELECT * FROM books WHERE isFavourite = 1")
    LiveData<List<BookModel>> getFavourite();

    /**
     * Поиск книг по названию или описанию
     */
    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    LiveData<List<BookModel>> searchProducts(String query);

    /**
     * Добавление новой книги
     */
    @Insert
    void insertProduct(BookModel bookModel);

    /**
     * Обновление данных книги
     */
    @Update
    void updateProduct(BookModel bookModel);

    /**
     * Удаление книги
     */
    @Delete
    void deleteProduct(BookModel bookModel);

    /**
     * Обновление статуса избранного для конкретной книги
     */
    @Query("UPDATE books SET isFavourite = :isFavourite WHERE id = :id")
    void updateFavoriteStatus(String id, boolean isFavourite);
}
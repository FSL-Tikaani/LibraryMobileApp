package com.tikaan.libraryapp.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tikaan.libraryapp.model.BookModel;

import java.util.List;

@Dao
public interface BooksDao {
    @Query("SELECT * FROM books")
    LiveData<List<BookModel>> getAllBooks();

    @Query("SELECT * FROM books WHERE id = :id")
    LiveData<BookModel> getBookFromId(String id);

    @Query("SELECT * FROM books WHERE isFavourite = 1")
    LiveData<List<BookModel>> getFavourite();

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    LiveData<List<BookModel>> searchProducts(String query);

    @Insert
    void insertProduct(BookModel bookModel);

    @Update
    void updateProduct(BookModel bookModel);

    @Delete
    void deleteProduct(BookModel bookModel);

    @Query("UPDATE books SET isFavourite = :isFavourite WHERE id = :id")
    void updateFavoriteStatus(String id, boolean isFavourite);
}
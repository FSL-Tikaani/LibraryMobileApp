package com.tikaan.libraryapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.tikaan.libraryapp.model.BookModel;
import com.tikaan.libraryapp.repo.BookRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private BookRepository bookRepository;
    private LiveData<List<BookModel>> allBooks;
    private LiveData<List<BookModel>> favouriteBooks;
    private MutableLiveData<List<BookModel>> searchResults = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application){
        super(application);
        bookRepository = new BookRepository(application);

        allBooks = bookRepository.getAllBooks();
        favouriteBooks = bookRepository.getFavouriteBooks();

        Log.d("MainViewModel", "ViewModel создан");
    }

    public LiveData<List<BookModel>> getAllBooks(){
        return allBooks;
    }

    public LiveData<BookModel> getBook(String id){
        return bookRepository.getBook(id);
    }

    public void searchBook(String query){
        if (query.trim().isEmpty()){
            searchResults.setValue(null);
        } else {
            LiveData<List<BookModel>> results = bookRepository.searchBook(query);
            results.observeForever(new Observer<List<BookModel>>() {
                @Override
                public void onChanged(List<BookModel> bookModels) {
                    searchResults.setValue(bookModels);
                    results.removeObserver(this);
                }
            });
        }
    }

    public LiveData<List<BookModel>> getSearchResults(){
        return searchResults;
    }

    public LiveData<List<BookModel>> getFavouriteBooks(){
        return favouriteBooks;
    }

    public void toggleFavorite(String productId, boolean isCurrentlyFavorite) { // Изменено на String
        Log.d("MainViewModel", "Изменение избранного для ID: " + productId);
        bookRepository.updateFavouriteStatus(productId, !isCurrentlyFavorite);
    }

    public void addBook(BookModel bookModel){
        Log.d("MainViewModel", "addBook: " + bookModel.getTitle() + ", ID: " + bookModel.getId());
        bookRepository.insertBook(bookModel);
    }

    public void updateBook(BookModel bookModel){
        Log.d("MainViewModel", "updateBook: " + bookModel.getTitle() + ", ID: " + bookModel.getId());
        bookRepository.updateBook(bookModel);
    }

    public void deleteBook(BookModel bookModel){
        Log.d("MainViewModel", "deleteBook: " + bookModel.getTitle());
        bookRepository.deleteBook(bookModel);
    }
}
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

/**
 * ViewModel для главной активности, управляющая данными книг
 * Следует архитектуре MVVM, предоставляет данные для UI слоя
 * Содержит LiveData для наблюдения за изменениями в реальном времени
 * Обрабатывает операции CRUD и поиск по книгам
 */

public class MainViewModel extends AndroidViewModel {
    private BookRepository bookRepository;

    // LiveData для наблюдения за изменениями в базе данных
    private LiveData<List<BookModel>> allBooks;
    private LiveData<List<BookModel>> favouriteBooks;

    // MutableLiveData для результатов поиска - может изменяться из ViewModel
    private MutableLiveData<List<BookModel>> searchResults = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application){
        super(application);
        bookRepository = new BookRepository(application);

        // Инициализируем LiveData из репозитория
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

    /**
     * Метод поиска книг с обработкой пустого запроса
     * Использует observeForever для одноразового получения результатов
     */
    public void searchBook(String query){
        if (query.trim().isEmpty()){
            // Очищаем результаты при пустом запросе
            searchResults.setValue(null);
        } else {
            LiveData<List<BookModel>> results = bookRepository.searchBook(query);
            // Временный наблюдатель для получения результатов поиска
            results.observeForever(new Observer<List<BookModel>>() {
                @Override
                public void onChanged(List<BookModel> bookModels) {
                    // Передаем результаты в MutableLiveData
                    searchResults.setValue(bookModels);
                    // Важно: удаляем наблюдатель после получения данных
                    // чтобы избежать утечек памяти и повторных срабатываний
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

    /**
     * Переключение статуса "Избранное" для книги
     * Инвертируем текущее состояние и обновляем в репозитории
     */
    public void toggleFavorite(String productId, boolean isCurrentlyFavorite) {
        Log.d("MainViewModel", "Изменение избранного для ID: " + productId);
        bookRepository.updateFavouriteStatus(productId, !isCurrentlyFavorite);
    }

    // Методы для работы с CRUD операциями
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
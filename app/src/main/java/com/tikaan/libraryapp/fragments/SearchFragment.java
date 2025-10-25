package com.tikaan.libraryapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tikaan.libraryapp.MainActivity;
import com.tikaan.libraryapp.MainViewModel;
import com.tikaan.libraryapp.R;
import com.tikaan.libraryapp.adapter.BookCardsAdapter;
import com.tikaan.libraryapp.adapter.VerticalSpaceItemDecoration;
import com.tikaan.libraryapp.model.BookModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Фрагмент для поиска книг по различным критериям
 */
public class SearchFragment extends Fragment {

    private MainViewModel viewModel;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private BookCardsAdapter adapter;
    private List<BookModel> originalBookList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация ViewModel для работы с данными
        viewModel = new MainViewModel(requireActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        // Инициализация View элементов
        searchEditText = view.findViewById(R.id.etSearch);
        recyclerView = view.findViewById(R.id.search_recyclerView);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10)); // Отступы между элементами

        adapter = new BookCardsAdapter(inflater);
        recyclerView.setAdapter(adapter);

        // Наблюдение за изменениями списка всех книг
        viewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                originalBookList = books;
                adapter.setBooks(originalBookList);
            }
        });

        // Обработчик ввода текста для поиска
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Не используется
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Фильтрация книг при изменении текста
                filterBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Не используется
            }
        });

        // Обработчики кликов по карточкам книг
        adapter.setOnBookClickListener(new BookCardsAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(BookModel bookModel) {
                openBookDetail(bookModel);
            }

            @Override
            public void onFavouriteClick(BookModel bookModel) {
                boolean newState = !bookModel.getIsFavourite();
                adapter.updateBookFavoriteState(bookModel.getId(), newState);
                viewModel.toggleFavorite(bookModel.getId(), bookModel.getIsFavourite());
            }

            @Override
            public void onLongClick(BookModel bookModel) {
                showActionsDialog(bookModel);
            }
        });

        return view;
    }

    // Фильтрация книг по поисковому запросу
    private void filterBooks(String text) {
        List<BookModel> filteredList = new ArrayList<>();

        if (text.isEmpty()) {
            // Если запрос пустой, показываем все книги
            filteredList.addAll(originalBookList);
        } else {
            String query = text.toLowerCase().trim();
            // Поиск книг, соответствующих запросу
            for (BookModel book : originalBookList) {
                if (matchesSearch(book, query)) {
                    filteredList.add(book);
                }
            }
        }

        // Обновление адаптера отфильтрованным списком
        adapter.setBooks(filteredList);
    }

    // Проверка, соответствует ли книга поисковому запросу
    private boolean matchesSearch(BookModel book, String query) {
        // Поиск по названию
        if (book.getTitle() != null && book.getTitle().toLowerCase().contains(query)) {
            return true;
        }
        // Поиск по автору
        if (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(query)) {
            return true;
        }
        // Поиск по описанию
        if (book.getDescription() != null && book.getDescription().toLowerCase().contains(query)) {
            return true;
        }
        // Поиск по тегам
        if (book.getTags() != null) {
            for (String tag : book.getTags()) {
                if (tag != null && tag.toLowerCase().contains(query)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Показ диалога с действиями для книги
    private void showActionsDialog(BookModel book) {
        String[] options = {"Редактировать", "Удалить"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите действие")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // Редактировать
                            openBookEdit(book);
                            break;
                        case 1: // Удалить
                            viewModel.deleteBook(book);
                            break;
                    }
                });
        builder.create().show();
    }

    // Открытие фрагмента с детальной информацией о книге
    private void openBookDetail(BookModel bookModel) {
        setCurrentFragment(new DetailFragment(bookModel));
    }

    // Открытие фрагмента редактирования книги
    private void openBookEdit(BookModel bookModel) {
        MainActivity activity = (MainActivity) requireActivity();
        activity.navigateToFragment(new EditFragment(bookModel));
    }

    // Переход к указанному фрагменту
    private void setCurrentFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}
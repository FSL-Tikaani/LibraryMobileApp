package com.tikaan.libraryapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Фрагмент для отображения списка избранных книг
 */
public class FavouriteFragment extends Fragment {

    private MainViewModel viewModel;
    private BookCardsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация ViewModel
        viewModel = new MainViewModel(requireActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_fragment, container, false);

        // Настройка RecyclerView для списка книг
        RecyclerView recyclerView = view.findViewById(R.id.favourite_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10)); // Отступы между элементами
        adapter = new BookCardsAdapter(inflater);
        recyclerView.setAdapter(adapter);

        // Наблюдение за изменениями списка избранных книг
        viewModel.getFavouriteBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                adapter.setBooks(books);
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
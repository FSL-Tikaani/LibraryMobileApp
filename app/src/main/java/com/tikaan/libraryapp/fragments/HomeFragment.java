package com.tikaan.libraryapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Главный фрагмент для отображения всех книг
 */
public class HomeFragment extends Fragment {

    private MainViewModel viewModel;
    private BookCardsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация ViewModel для работы с данными
        viewModel = new MainViewModel(requireActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        // Настройка RecyclerView для отображения списка книг
        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10)); // Отступы между элементами
        adapter = new BookCardsAdapter(inflater);
        recyclerView.setAdapter(adapter);

        // Обработчики кликов по карточкам книг
        adapter.setOnBookClickListener(new BookCardsAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(BookModel bookModel) {
                openBookDetail(bookModel);
            }

            @Override
            public void onFavouriteClick(BookModel bookModel) {
                Log.d("HomeFragment", "id: " + bookModel.getId() + " isFavourite: " + bookModel.getIsFavourite());

                // НЕМЕДЛЕННО обновляем UI
                boolean newState = !bookModel.getIsFavourite();
                adapter.updateBookFavoriteState(bookModel.getId(), newState);

                // Затем обновляем в базе данных
                viewModel.toggleFavorite(bookModel.getId(), bookModel.getIsFavourite());
            }

            @Override
            public void onLongClick(BookModel bookModel) {
                showActionsDialog(bookModel);
            }
        });

        // Наблюдение за изменениями списка всех книг
        viewModel.getAllBooks().observe(getViewLifecycleOwner(), bookModels -> {
            adapter.setBooks(bookModels);
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
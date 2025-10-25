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

public class FavouriteFragment extends Fragment {

    private MainViewModel viewModel;
    private BookCardsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainViewModel(requireActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.favourite_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        adapter = new BookCardsAdapter(inflater);

        recyclerView.setAdapter(adapter);

        viewModel.getFavouriteBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                adapter.setBooksList(books);
            }
        });

        adapter.setOnBookClickListener(new BookCardsAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(BookModel bookModel) {
                openBookDetail(bookModel);
            }

            @Override
            public void onFavouriteClick(BookModel bookModel) {
                boolean newState = !bookModel.getIsFavourite();
                adapter.updateBookState(bookModel.getId(), newState);
                viewModel.toggleFavorite(bookModel.getId(), bookModel.getIsFavourite());
            }

            @Override
            public void onLongClick(BookModel bookModel) {
                showActionsDialog(bookModel);
            }
        });

        return view;
    }

    private void showActionsDialog(BookModel book) {
        String[] options = {"Редактировать", "Удалить"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите действие")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            openBookEdit(book);
                            break;
                        case 1:
                            viewModel.deleteBook(book);
                            break;
                    }
                });
        builder.create().show();
    }

    private void openBookDetail(BookModel bookModel) {
        Toast.makeText(getContext(), "Book clicked: " + bookModel.getTitle(), Toast.LENGTH_SHORT).show();
        setCurrentFragment(new DetailFragment(bookModel));
    }

    private void openBookEdit(BookModel bookModel) {
        MainActivity activity = (MainActivity) requireActivity();
        activity.navigateToFragment(new EditFragment(bookModel));
    }

    private void setCurrentFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}

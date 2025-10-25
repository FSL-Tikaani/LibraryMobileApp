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

public class SearchFragment extends Fragment {

    private MainViewModel viewModel;
    private EditText search;
    private RecyclerView recyclerView;
    private BookCardsAdapter adapter;
    private List<BookModel> originalBookList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainViewModel(requireActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        search = view.findViewById(R.id.etSearch);
        recyclerView = view.findViewById(R.id.search_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));

        adapter = new BookCardsAdapter(inflater);
        recyclerView.setAdapter(adapter);

        viewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                originalBookList = books;
                adapter.setBooksList(originalBookList);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
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

    private void filterBooks(String text) {
        List<BookModel> filteredList = new ArrayList<>();

        if (text.isEmpty()) {
            filteredList.addAll(originalBookList);
        } else {
            String query = text.toLowerCase().trim();
            for (BookModel book : originalBookList) {
                if (matchesSearch(book, query)) {
                    filteredList.add(book);
                }
            }
        }

        adapter.setBooksList(filteredList);
    }

    private boolean matchesSearch(BookModel book, String query) {
        if (book.getTitle() != null && book.getTitle().toLowerCase().contains(query)) {
            return true;
        }
        if (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(query)) {
            return true;
        }
        if (book.getDescription() != null && book.getDescription().toLowerCase().contains(query)) {
            return true;
        }
        if (book.getTags() != null) {
            for (String tag : book.getTags()) {
                if (tag != null && tag.toLowerCase().contains(query)) {
                    return true;
                }
            }
        }
        return false;
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
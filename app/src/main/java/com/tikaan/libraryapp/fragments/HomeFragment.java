package com.tikaan.libraryapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tikaan.libraryapp.MainViewModel;
import com.tikaan.libraryapp.R;
import com.tikaan.libraryapp.adapter.BookCardsAdapter;
import com.tikaan.libraryapp.adapter.VerticalSpaceItemDecoration;
import com.tikaan.libraryapp.model.BookModel;


public class HomeFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(50));
        adapter = new BookCardsAdapter(inflater);

        recyclerView.setAdapter(adapter);

        adapter.setOnBookClickListener(new BookCardsAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(BookModel bookModel) {
                Toast.makeText(inflater.getContext(), bookModel.getTitle(), Toast.LENGTH_SHORT).show();
                //openBookDetail(bookModel);
            }

            @Override
            public void onFavouriteClick(BookModel bookModel) {
                viewModel.toggleFavorite(bookModel.getId(), bookModel.getIsFavourite());
                //boolean newState = viewModel.getBook(bookModel.getId()).getValue().getIsFavourite();
                Toast.makeText(inflater.getContext(), Integer.toString(bookModel.getId()), Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getAllBooks().observe(getViewLifecycleOwner(), bookModels -> {
            adapter.setBooksList(bookModels);
        });

        return view;
    }
}

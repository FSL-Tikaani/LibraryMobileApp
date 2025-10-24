package com.tikaan.libraryapp.fragments;

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

import com.tikaan.libraryapp.MainViewModel;
import com.tikaan.libraryapp.R;
import com.tikaan.libraryapp.adapter.BookCardsAdapter;
import com.tikaan.libraryapp.adapter.VerticalSpaceItemDecoration;
import com.tikaan.libraryapp.model.BookModel;

public class HomeFragment extends Fragment {

    private MainViewModel viewModel;
    private BookCardsAdapter adapter;
    private LayoutInflater inflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainViewModel(requireActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
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
                Log.d("HomeFragment", "id: " + bookModel.getId() + " isFavourite: " + bookModel.getIsFavourite());

                // НЕМЕДЛЕННО обновляем UI
                boolean newState = !bookModel.getIsFavourite();
                adapter.updateBookState(bookModel.getId(), newState);

                // Затем обновляем в базе данных
                viewModel.toggleFavorite(bookModel.getId(), bookModel.getIsFavourite());

                Toast.makeText(inflater.getContext(), "Favorite updated", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getAllBooks().observe(getViewLifecycleOwner(), bookModels -> {
            adapter.setBooksList(bookModels);
        });

        return view;
    }
}
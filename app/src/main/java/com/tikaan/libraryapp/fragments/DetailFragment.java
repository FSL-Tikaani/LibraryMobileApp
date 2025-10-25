package com.tikaan.libraryapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;
import com.tikaan.libraryapp.R;
import com.tikaan.libraryapp.model.BookModel;

public class DetailFragment extends Fragment {
    private BookModel book;
    private TextView tv_title, tv_author, tv_desc;

    private ImageView image;

    private ChipGroup cg;

    public DetailFragment(BookModel book) {
        this.book = book;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        tv_title = (TextView) view.findViewById(R.id.detail_tvTitle);
        tv_author = (TextView) view.findViewById(R.id.detail_tvAuthor);
        tv_desc = (TextView) view.findViewById(R.id.detail_tvDescription);
        image = (ImageView) view.findViewById(R.id.detail_ivCover);
        cg = (ChipGroup) view.findViewById(R.id.detail_chipGroupTags);

        Picasso.get()
                .load(book.getSrcImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(image);

        tv_title.setText(book.getTitle());
        tv_author.setText(book.getAuthor());
        tv_desc.setText(book.getDescription());

        for (String tag : book.getTags()) {
            Chip chip = new Chip(getContext());
            chip.setText(tag);
            cg.addView(chip);
        }

        return view;
    }
}

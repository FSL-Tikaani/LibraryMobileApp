package com.tikaan.libraryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tikaan.libraryapp.R;
import com.tikaan.libraryapp.model.BookModel;

import java.util.List;

public class BookCardsAdapter extends RecyclerView.Adapter<BookCardsAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<BookModel> books;

    public BookCardsAdapter(LayoutInflater inflater, List<BookModel> books) {
        this.inflater = inflater;
        this.books = books;
    }

    @NonNull
    @Override
    public BookCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.book_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookCardsAdapter.ViewHolder holder, int position) {
        BookModel book = books.get(position);
        holder.tv_title.setText(book.getTitle());
        holder.tv_description.setText(book.getDescription());
        Picasso.get()
                .load(book.getSrcImage())
                .placeholder(R.drawable.ic_launcher_background) // изображение-заглушка
                .error(R.drawable.ic_launcher_foreground) // изображение при ошибке
                .into(holder.imageBook);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageBook;
        final TextView tv_title, tv_description;
        ViewHolder(View view){
            super(view);
            imageBook = (ImageView) view.findViewById(R.id.bookCard_imageView_photoBook);
            tv_title = (TextView) view.findViewById(R.id.bookCard_textView_titleBook);
            tv_description = (TextView) view.findViewById(R.id.bookCard_textView_descriptionBook);

        }
    }

}

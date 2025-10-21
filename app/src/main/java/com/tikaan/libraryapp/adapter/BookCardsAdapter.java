package com.tikaan.libraryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tikaan.libraryapp.R;
import com.tikaan.libraryapp.model.BookModel;

import java.util.ArrayList;
import java.util.List;

public class BookCardsAdapter extends RecyclerView.Adapter<BookCardsAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<BookModel> books = new ArrayList<>();
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(BookModel bookModel);
        void onFavouriteClick(BookModel bookModel);
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.listener = listener;
    }

    public BookCardsAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public BookCardsAdapter(LayoutInflater inflater, List<BookModel> books) {
        this.inflater = inflater;
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.book_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookModel book = books.get(position);
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooksList(List<BookModel> data) {
        this.books = data != null ? data : new ArrayList<>();
        notifyDataSetChanged();
    }

    // Метод для обновления отдельной книги
    public void updateBook(BookModel updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == updatedBook.getId()) {
                books.set(i, updatedBook);
                notifyItemChanged(i);
                break;
            }
        }
    }

    // Метод для добавления списка книг
    public void addBooks(List<BookModel> newBooks) {
        if (newBooks != null && !newBooks.isEmpty()) {
            int startPosition = books.size();
            books.addAll(newBooks);
            notifyItemRangeInserted(startPosition, newBooks.size());
        }
    }

    // Метод для очистки списка
    public void clearBooks() {
        books.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageBook;
        final TextView tv_title, tv_description;
        final ImageButton btn_favorite;

        ViewHolder(View view) {
            super(view);
            imageBook = view.findViewById(R.id.bookCard_imageView_photoBook);
            tv_title = view.findViewById(R.id.bookCard_textView_titleBook);
            tv_description = view.findViewById(R.id.bookCard_textView_descriptionBook);
            btn_favorite = view.findViewById(R.id.bookCard_imageButton_favorite);
        }

        public void bind(BookModel book, OnBookClickListener listener) {
            tv_title.setText(book.getTitle());
            tv_description.setText(book.getDescription());

            // Загружаем изображение с помощью Picasso
            Picasso.get()
                    .load(book.getSrcImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageBook);

            // Обработчик клика на кнопку избранного
            btn_favorite.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFavouriteClick(book);
                }
            });



            // Обработчик клика на всю карточку
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(book);
                }
            });
        }
    }
}
package com.tikaan.libraryapp.adapter;

import android.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.List;

public class BookCardsAdapter extends RecyclerView.Adapter<BookCardsAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<BookModel> books = new ArrayList<>();
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(BookModel bookModel);
        void onFavouriteClick(BookModel bookModel);

        void onLongClick(BookModel bookModel);
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

    // Новый метод для немедленного обновления состояния избранного
    public void updateBookState(String bookId, boolean newFavouriteState) {
        for (int i = 0; i < books.size(); i++) {
            BookModel book = books.get(i);
            if (book.getId().equals(bookId) ) {
                // Создаем новый объект с обновленным состоянием
                BookModel updatedBook = new BookModel(
                        book.getId(),
                        book.getTitle(),
                        book.getDescription(),
                        book.getAuthor(),
                        book.getTags(),
                        newFavouriteState, // новое состояние
                        book.getSrcImage()
                );
                books.set(i, updatedBook);
                notifyItemChanged(i);
                break;
            }
        }
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
        final TextView tv_title, tv_description, tv_author;
        final ImageView btn_favorite;

        ViewHolder(View view) {
            super(view);
            imageBook = view.findViewById(R.id.bookCard_imageView_photoBook);
            tv_title = view.findViewById(R.id.bookCard_textView_titleBook);
            tv_author = view.findViewById(R.id.bookCard_textView_author);
            tv_description = view.findViewById(R.id.bookCard_textView_description);
            btn_favorite = view.findViewById(R.id.bookCard_imageView_favourite);
        }

        public void bind(BookModel book, OnBookClickListener listener) {
            tv_title.setText(book.getTitle());
            tv_description.setText(book.getDescription());
            tv_author.setText(book.getAuthor());

            // Устанавливаем правильную иконку для избранного
            updateFavoriteIcon(book.getIsFavourite());

            // Загружаем изображение с помощью Picasso
            Picasso.get()
                    .load("https://api.bookmate.ru/assets/books-covers/4c/92/h2P7sCIT-ipad.jpeg?image_hash=5848150d75897b6dc2055ba52b06f6a4")
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageBook);

            // Обработчик клика на кнопку избранного
            btn_favorite.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFavouriteClick(book);
                    // Немедленно обновляем иконку после клика
                    updateFavoriteIcon(!book.getIsFavourite());
                }
            });

            // Обработчик клика на всю карточку
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(book);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onLongClick(book);
                }
                return true;
            });
        }

        private void updateFavoriteIcon(boolean isFavourite) {
            if (isFavourite) {
                btn_favorite.setImageResource(R.drawable.baseline_favorite_24); // заполненное сердечко
            } else {
                btn_favorite.setImageResource(R.drawable.outline_favorite_24); // контур сердечка
            }
        }
    }
}
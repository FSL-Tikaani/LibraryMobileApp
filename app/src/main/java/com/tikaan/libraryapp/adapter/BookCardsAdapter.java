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

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для отображения книг в RecyclerView в виде карточек.
 * Поддерживает обработку кликов для выбора книги и управления избранным.
 */
public class BookCardsAdapter extends RecyclerView.Adapter<BookCardsAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<BookModel> books;
    private OnBookClickListener listener;

    /**
     * Интерфейс для обработки различных типов кликов по карточке книги
     */
    public interface OnBookClickListener {
        void onBookClick(BookModel bookModel);
        void onFavouriteClick(BookModel bookModel);
        void onLongClick(BookModel bookModel);
    }

    public BookCardsAdapter(@NonNull LayoutInflater inflater) {
        this.inflater = inflater;
        this.books = new ArrayList<>();
    }

    public BookCardsAdapter(@NonNull LayoutInflater inflater, @NonNull List<BookModel> books) {
        this.inflater = inflater;
        this.books = new ArrayList<>(books);
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    /**
     * Полная замена списка книг
     */
    public void setBooks(@NonNull List<BookModel> books) {
        this.books = new ArrayList<>(books);
        notifyDataSetChanged();
    }

    /**
     * Обновляет состояние избранного для конкретной книги
     */
    public void updateBookFavoriteState(@NonNull String bookId, boolean isFavorite) {
        for (int i = 0; i < books.size(); i++) {
            BookModel book = books.get(i);
            if (book.getId().equals(bookId)) {
                // Создаем новый объект книги с обновленным состоянием избранного
                BookModel updatedBook = createBookWithUpdatedFavorite(book, isFavorite);
                books.set(i, updatedBook);
                notifyItemChanged(i); // Обновляем только измененный элемент
                break;
            }
        }
    }

    /**
     * Обновляет данные одной книги в списке
     */
    public void updateBook(@NonNull BookModel updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(updatedBook.getId())) {
                books.set(i, updatedBook);
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * Добавляет новые книги в существующий список
     */
    public void addBooks(@NonNull List<BookModel> newBooks) {
        if (!newBooks.isEmpty()) {
            int startPosition = books.size();
            books.addAll(newBooks);
            // Эффективное обновление - только для добавленных элементов
            notifyItemRangeInserted(startPosition, newBooks.size());
        }
    }

    /**
     * Очищает весь список книг
     */
    public void clearBooks() {
        int itemCount = books.size();
        books.clear();
        // Уведомляем об удалении всех элементов
        notifyItemRangeRemoved(0, itemCount);
    }

    /**
     * Создает копию книги с обновленным состоянием избранного
     */
    @NonNull
    private BookModel createBookWithUpdatedFavorite(@NonNull BookModel book, boolean isFavorite) {
        return new BookModel(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getAuthor(),
                book.getTags(),
                isFavorite,
                book.getSrcImage()
        );
    }

    /**
     * ViewHolder для отображения карточки отдельной книги
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView bookImageView;
        private final TextView titleTextView;
        private final TextView authorTextView;
        private final TextView descriptionTextView;
        private final ImageView favoriteImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Инициализация всех View элементов карточки
            bookImageView = itemView.findViewById(R.id.bookCard_imageView_photoBook);
            titleTextView = itemView.findViewById(R.id.bookCard_textView_titleBook);
            authorTextView = itemView.findViewById(R.id.bookCard_textView_author);
            descriptionTextView = itemView.findViewById(R.id.bookCard_textView_description);
            favoriteImageView = itemView.findViewById(R.id.bookCard_imageView_favourite);
        }

        /**
         * Привязывает данные книги к View элементам
         * @param book данные книги для отображения
         * @param listener обработчик кликов
         */
        public void bind(@NonNull BookModel book, OnBookClickListener listener) {
            // Установка текстовых данных
            titleTextView.setText(book.getTitle());
            descriptionTextView.setText(book.getDescription());
            authorTextView.setText(book.getAuthor());

            // Обновление иконки избранного
            updateFavoriteIcon(book.getIsFavourite());

            // Загрузка изображения книги с использованием Picasso
            // Если у книги нет своего изображения, используем заглушку
            String imageUrl = book.getSrcImage();
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background) // Показываем пока грузится
                    .error(R.drawable.ic_launcher_foreground)       // Показываем при ошибке
                    .into(bookImageView);

            // Настройка обработчиков кликов
            setupClickListeners(book, listener);
        }

        /**
         * Настраивает все типы кликов по карточке книги
         */
        private void setupClickListeners(@NonNull BookModel book, OnBookClickListener listener) {
            // Клик по иконке избранного
            favoriteImageView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFavouriteClick(book);
                }
            });

            // Клик по всей карточке (для открытия деталей книги)
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(book);
                }
            });

            // Долгий клик по карточке (для контекстного меню)
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onLongClick(book);
                }
                return true;
            });
        }

        /**
         * Обновляет иконку избранного в зависимости от состояния
         */
        private void updateFavoriteIcon(boolean isFavourite) {
            int iconResource = isFavourite ?
                    R.drawable.baseline_favorite_24 :     // Заполненное сердечко
                    R.drawable.outline_favorite_24;       // Контур сердечка
            favoriteImageView.setImageResource(iconResource);
        }
    }
}
package com.tikaan.libraryapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.tikaan.libraryapp.MainViewModel;
import com.tikaan.libraryapp.R;
import com.tikaan.libraryapp.model.BookModel;

import java.util.List;
import java.util.UUID;

/**
 * Фрагмент для редактирования существующей книги или создания новой
 */
public class EditFragment extends Fragment {

    private BookModel bookModel;
    private TextInputEditText etTitle, etAuthor, etDescription, etTags, etImageSrc;
    private MaterialCheckBox cbFavourite;
    private Button btnSave;

    private MainViewModel viewModel;

    // Конструктор для создания новой книги
    public EditFragment() {}

    // Конструктор для редактирования существующей книги
    public EditFragment(BookModel bookModel) {
        this.bookModel = bookModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Получение ViewModel для работы с данными
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);
        initViews(view);
        setupClickListeners();
        populateData();
        Log.d("EditFragment", "Переданы данные: " + Boolean.toString(bookModel != null));
        return view;
    }

    // Инициализация всех View элементов
    private void initViews(View view) {
        etTitle = view.findViewById(R.id.addFragment_et_title);
        etAuthor = view.findViewById(R.id.addFragment_et_author);
        etDescription = view.findViewById(R.id.addFragment_et_description);
        etTags = view.findViewById(R.id.addFragment_et_tags);
        etImageSrc = view.findViewById(R.id.addFragment_et_imageSrc);
        cbFavourite = view.findViewById(R.id.addFragment_cb_favourite);
        btnSave = view.findViewById(R.id.addFragment_btn_save);
    }

    // Настройка обработчиков кликов
    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveBook());
    }

    // Заполнение полей данными существующей книги (в режиме редактирования)
    private void populateData() {
        if (bookModel != null) {
            etTitle.setText(bookModel.getTitle());
            etAuthor.setText(bookModel.getAuthor());
            etDescription.setText(bookModel.getDescription());
            etImageSrc.setText(bookModel.getSrcImage());
            cbFavourite.setChecked(bookModel.getIsFavourite());

            // Объединение тегов в строку через запятую
            if (bookModel.getTags() != null && !bookModel.getTags().isEmpty()) {
                etTags.setText(String.join(", ", bookModel.getTags()));
            }
        }
    }

    // Валидация и сохранение книги
    private void saveBook() {
        // Проверка заполнения обязательных полей
        if (etTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Введите название книги", Toast.LENGTH_SHORT).show();
            return;
        }

        if(etAuthor.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Введите автора", Toast.LENGTH_SHORT).show();
            return;
        }

        if(etDescription.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Введите описание", Toast.LENGTH_SHORT).show();
            return;
        }

        if(etTags.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Введите теги", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создание нового объекта книги из введенных данных
        BookModel newBook = new BookModel();

        newBook.setTitle(etTitle.getText().toString().trim());
        newBook.setAuthor(etAuthor.getText().toString().trim());
        newBook.setDescription(etDescription.getText().toString().trim());
        newBook.setSrcImage(etImageSrc.getText().toString().trim());
        newBook.setFavourite(cbFavourite.isChecked());
        newBook.setTags(List.of(etTags.getText().toString().trim().split(",")));

        // Проверка, изменились ли данные
        if (checkEqualBooks(bookModel, newBook)) {
            Toast.makeText(getContext(), "Книга не изменена!", Toast.LENGTH_SHORT).show();
        }else{
            // Создание новой книги или обновление существующей
            if (bookModel == null) {
                newBook.setId(UUID.randomUUID().toString()); // Генерация нового ID
                createNewBook(newBook);
            }else {
                newBook.setId(bookModel.getId()); // Сохранение существующего ID
                updateBook(newBook);
            }
        }
    }

    // Обновление существующей книги
    private void updateBook(BookModel newBook) {
        viewModel.updateBook(newBook);
        Toast.makeText(getContext(), "Книга обновлена", Toast.LENGTH_SHORT).show();
        setCurrentFragment(new HomeFragment());
    }

    // Сравнение двух книг по всем полям
    private Boolean checkEqualBooks(BookModel book1, BookModel book2) {
        boolean equal = false;
        if (book1 == null || book2 == null) {
            return false;
        }else {
            if (book1.getTitle().equals(book2.getTitle())) {
                if (book1.getAuthor().equals(book2.getAuthor())) {
                    if (book1.getDescription().equals(book2.getDescription())) {
                        if (book1.getSrcImage().equals(book2.getSrcImage())) {
                            if (book1.getIsFavourite() == book2.getIsFavourite()) {
                                if (book1.getTags().equals(book2.getTags())) {
                                    equal = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return equal;
    }

    // Создание новой книги
    private void createNewBook(BookModel newBook) {
        viewModel.addBook(newBook);
        Toast.makeText(getContext(), "Книга добавлена", Toast.LENGTH_SHORT).show();
        setCurrentFragment(new HomeFragment());
    }

    // Переход к указанному фрагменту
    private void setCurrentFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}
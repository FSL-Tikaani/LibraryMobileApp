package com.tikaan.libraryapp.repo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.tikaan.libraryapp.model.BookModel;

/**
 * Главная база данных приложения Room
 * Содержит таблицу книг и использует конвертеры для сложных типов данных
 */
@Database(
        entities = {BookModel.class}, // Сущности базы данных
        version = 2,                  // Версия базы данных
        exportSchema = false          // Отключение экспорта схемы
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract BooksDao booksDao();

    private static volatile AppDatabase INSTANCE;

    /**
     * Получение экземпляра базы данных
     */
    public static AppDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    // Создание базы данных
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
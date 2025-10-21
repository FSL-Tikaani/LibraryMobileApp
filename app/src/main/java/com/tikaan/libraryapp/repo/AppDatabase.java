package com.tikaan.libraryapp.repo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.tikaan.libraryapp.model.BookModel;

@Database(
        entities = {BookModel.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BooksDao booksDao();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
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

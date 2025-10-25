package com.tikaan.libraryapp.repo;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Конвертеры для преобразования сложных типов данных для Room
 * Позволяют сохранять списки в базе данных как строки JSON
 */
public class Converters {
    private static final Gson gson = new Gson(); // Экземпляр Gson для JSON преобразований

    /**
     * Конвертация строки JSON в список строк
     */
    @TypeConverter
    public static List<String> fromString(String data){
        if (data == null){
            return Collections.emptyList(); // Возвращаем пустой список вместо null
        }else {
            Type listType = new TypeToken<List<String>>() {}.getType(); // Определение типа для десериализации
            return gson.fromJson(data, listType); // Преобразование JSON в список
        }
    }

    /**
     * Конвертация списка строк в строку JSON
     */
    @TypeConverter
    public static String fromList(List<String> list){
        return gson.toJson(list); // Преобразование списка в JSON
    }
}
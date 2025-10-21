package com.tikaan.libraryapp.repo;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Converters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<String> fromString(String data){
        if (data == null){
            return Collections.emptyList();
        }else {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(data, listType);
        }
    }

    @TypeConverter
    public static String fromList(List<String> list){
        return gson.toJson(list);
    }

}

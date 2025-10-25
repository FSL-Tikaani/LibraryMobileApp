package com.tikaan.libraryapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.tikaan.libraryapp.repo.Converters;

import java.util.List;
import java.util.UUID;

@Entity(tableName = "books")
@TypeConverters(Converters.class)
public class BookModel {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String description;
    private String author;
    private List<String> tags;
    private boolean isFavourite;
    private String srcImage;

    public BookModel() {
        this.id = UUID.randomUUID().toString();
    }

    public BookModel(@NonNull String id, String title, String description, String author,
                     List<String> tags, boolean isFavourite, String srcImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.tags = tags;
        this.isFavourite = isFavourite;
        this.srcImage = srcImage;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }

    @Override
    public String toString() {
        return "BookModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", tags=" + tags +
                ", isFavourite=" + isFavourite +
                ", srcImage='" + srcImage + '\'' +
                '}';
    }
}
package com.tikaan.libraryapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
@Entity(tableName = "books")
public class BookModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String author;
    private List<String> tags;
    private boolean isFavourite;
    private String srcImage;

    public BookModel() {
    }

    public BookModel(int id, String title, String description, String author, List<String> tags, boolean isFavourite, String srcImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.tags = tags;
        this.isFavourite = isFavourite;
        this.srcImage = srcImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }
}

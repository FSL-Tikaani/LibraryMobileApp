package com.tikaan.libraryapp.model;

import java.util.List;

public class BookModel {
    private String id;
    private String title;
    private String description;
    private String author;
    private List<String> tags;
    private String srcImage;

    public BookModel() {
    }

    public BookModel(String id, String title, String description, String author, List<String> tags, String srcImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.tags = tags;
        this.srcImage = srcImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }
}

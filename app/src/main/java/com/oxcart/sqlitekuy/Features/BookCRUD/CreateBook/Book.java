package com.oxcart.sqlitekuy.Features.BookCRUD.CreateBook;

public class Book {
    private long id;
    private long book_number;
    private String title;
    private String author;
    private int year;
    private String description;

    public Book(long id, long book_number, String title, String author, int year, String description) {
        this.id = id;
        this.book_number = book_number;
        this.title = title;
        this.author = author;
        this.year = year;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBook_number() {
        return book_number;
    }

    public void setBook_number(long book_number) {
        this.book_number = book_number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
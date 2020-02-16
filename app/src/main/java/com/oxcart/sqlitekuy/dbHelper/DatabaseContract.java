package com.oxcart.sqlitekuy.dbHelper;

public class DatabaseContract  {
    public static final String DATABASE_NAME = "library-db";

    //column names of student table
    public static final String TABLE_BOOK = "book";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BOOK_NUMBER = "book_number";
    public static final String COLUMN_BOOK_TITLE = "title";
    public static final String COLUMN_BOOK_AUTHOR = "author";
    public static final String COLUMN_BOOK_YEAR = "year";
    public static final String COLUMN_BOOK_DESCRIPION = "description";

    //column names of subject table
    public static final String TABLE_REVIEW = "review";
    public static final String COLUMN_REVIEW_ID = "_id";
    public static final String COLUMN_BOOK_NUMBER_FOREIGN = "fk_book_number";
    public static final String COLUMN_REVIEWER_NAME = "reviewer_name";
    public static final String COLUMN_RATTING = "ratting";
    public static final String COLUMN_COMMENT = "comment";
    public static final String BOOK_SUB_CONSTRAINT = "book_sub_unique";

    //others for general purpose key-value pair data
    public static final String TITLE = "title";
    public static final String CREATE_BOOK = "create_book";
    public static final String UPDATE_BOOK = "update_book";
    public static final String CREATE_REVIEW = "create_review";
    public static final String UPDATE_REVIEW = "update_review";
    public static final String BOOK_NUMBER = "book_number";
}

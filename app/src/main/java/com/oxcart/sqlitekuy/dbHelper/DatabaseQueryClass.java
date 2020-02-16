package com.oxcart.sqlitekuy.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.oxcart.sqlitekuy.Features.BookCRUD.CreateBook.Book;
import com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview.Review;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseQueryClass {
    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertBook(Book book){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.COLUMN_BOOK_NUMBER, book.getBook_number());
        contentValues.put(DatabaseContract.COLUMN_BOOK_TITLE, book.getTitle());
        contentValues.put(DatabaseContract.COLUMN_BOOK_AUTHOR, book.getAuthor());
        contentValues.put(DatabaseContract.COLUMN_BOOK_YEAR, book.getYear());
        contentValues.put(DatabaseContract.COLUMN_BOOK_DESCRIPION, book.getDescription());

        try {
            id = sqLiteDatabase.insertOrThrow(DatabaseContract.TABLE_BOOK, null, contentValues);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Book> getAllBook(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(DatabaseContract.TABLE_BOOK, null, null, null, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */
            //INI BELUM
            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Book> bookList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.COLUMN_ID));
                        long book_name = cursor.getLong(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_NUMBER));
                        String title = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_TITLE));
                        String author = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_AUTHOR));
                        int year = cursor.getInt(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_YEAR));
                        String description = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_DESCRIPION));

                        bookList.add(new Book(id, book_name,title, author, year, description));
                    }   while (cursor.moveToNext());

                    return bookList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Book getBookByBookNumber(long bookNumber){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Book book = null;

        try {

            cursor = sqLiteDatabase.query(DatabaseContract.TABLE_BOOK, null,
                    DatabaseContract.COLUMN_BOOK_NUMBER + " = ? ", new String[]{String.valueOf(bookNumber)},
                    null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above sqLiteDatabase.query() method.

             String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_STUDENT, Config.COLUMN_STUDENT_REGISTRATION, String.valueOf(registrationNum));
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.COLUMN_ID));
                long bookNum = cursor.getLong(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_NUMBER));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_AUTHOR));
                int year = cursor.getInt(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_YEAR));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_BOOK_DESCRIPION));

                book = new Book(id, bookNum, title, author, year, description);
            }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return book;
    }

    public long updateBookInfo(Book book){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.COLUMN_BOOK_NUMBER, book.getBook_number());
        contentValues.put(DatabaseContract.COLUMN_BOOK_TITLE, book.getTitle());
        contentValues.put(DatabaseContract.COLUMN_BOOK_AUTHOR, book.getAuthor());
        contentValues.put(DatabaseContract.COLUMN_BOOK_YEAR, book.getYear());
        contentValues.put(DatabaseContract.COLUMN_BOOK_DESCRIPION, book.getDescription());

        try {
            rowCount = sqLiteDatabase.update(DatabaseContract.TABLE_BOOK, contentValues,
                    DatabaseContract.COLUMN_ID+ " = ? ",
                    new String[] {String.valueOf(book.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteBookByBookNum(long bookNum) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(DatabaseContract.TABLE_BOOK,
                    DatabaseContract.BOOK_NUMBER+ " = ? ",
                    new String[]{ String.valueOf(bookNum)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllBook(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(DatabaseContract.TABLE_BOOK, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, DatabaseContract.TABLE_BOOK);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

    public long getNumberOfBook(){
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, DatabaseContract.TABLE_BOOK);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return count;
    }

    // review
    public long insertReview(Review review, long booksNumber){
        long rowId = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.COLUMN_REVIEWER_NAME, review.getReviwer_name());
        contentValues.put(DatabaseContract.COLUMN_RATTING, review.getRatting());
        contentValues.put(DatabaseContract.COLUMN_COMMENT, review.getComment());
        contentValues.put(DatabaseContract.COLUMN_BOOK_NUMBER_FOREIGN, booksNumber);

        try {
            rowId = sqLiteDatabase.insertOrThrow(DatabaseContract.TABLE_REVIEW, null, contentValues);
        } catch (SQLiteException e){
            Logger.d(e);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowId;
    }

    public Review getReviewById(long reviewId){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Review review = null;

        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(DatabaseContract.TABLE_REVIEW, null,
                    DatabaseContract.COLUMN_REVIEW_ID + " = ? ", new String[] {String.valueOf(reviewId)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                String reviewerName = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_REVIEWER_NAME));
                int ratting = cursor.getInt(cursor.getColumnIndex(DatabaseContract.COLUMN_RATTING));
                String comment = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_COMMENT));

                review = new Review(reviewId, reviewerName, ratting, comment);
            }
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return review;
    }

    public long updateReviewInfo(Review review){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.COLUMN_REVIEWER_NAME, review.getReviwer_name());
        contentValues.put(DatabaseContract.COLUMN_RATTING, review.getRatting());
        contentValues.put(DatabaseContract.COLUMN_COMMENT, review.getComment());

        try {
            rowCount = sqLiteDatabase.update(DatabaseContract.TABLE_REVIEW, contentValues,
                    DatabaseContract.COLUMN_REVIEW_ID+ " = ? ",
                    new String[] {String.valueOf(review.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    //INI BELUM
    public List<Review> getAllReviewByBookNo(long bookNo){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        List<Review> reviewList = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(DatabaseContract.TABLE_REVIEW,
                    new String[] {DatabaseContract.COLUMN_REVIEW_ID, DatabaseContract.COLUMN_REVIEWER_NAME, DatabaseContract.COLUMN_RATTING, DatabaseContract.COLUMN_COMMENT},
                    DatabaseContract.COLUMN_BOOK_NUMBER_FOREIGN+ " = ? ",
                    new String[] {String.valueOf(bookNo)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                reviewList = new ArrayList<>();
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.COLUMN_REVIEW_ID));
                    String reviwerName = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_REVIEWER_NAME));
                    int ratting = cursor.getInt(cursor.getColumnIndex(DatabaseContract.COLUMN_RATTING));
                    String comment = cursor.getString(cursor.getColumnIndex(DatabaseContract.COLUMN_COMMENT));

                    reviewList.add(new Review(id, reviwerName, ratting, comment));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return reviewList;
    }

    public boolean deleteReviewById(long reviewId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(DatabaseContract.TABLE_REVIEW,
                DatabaseContract.COLUMN_REVIEW_ID+ " = ? ", new String[]{String.valueOf(reviewId)});

        return row > 0;
    }

    public boolean deleteAllReviewByBookNum(long bookNum) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(DatabaseContract.TABLE_REVIEW,
                DatabaseContract.COLUMN_BOOK_NUMBER_FOREIGN+ " = ? ", new String[]{String.valueOf(bookNum)});

        return row > 0;
    }

    public long getNumberOfReview(){
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, DatabaseContract.TABLE_REVIEW);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return count;
    }



}

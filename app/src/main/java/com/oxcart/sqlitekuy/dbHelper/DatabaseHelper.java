package com.oxcart.sqlitekuy.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class DatabaseHelper extends SQLiteOpenHelper {
    // All Static variables
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = DatabaseContract.DATABASE_NAME;
    private static DatabaseHelper databaseHelper;

    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            synchronized (DatabaseHelper.class) {
                if (databaseHelper == null)
                    databaseHelper = new DatabaseHelper(context);
            }
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE " + DatabaseContract.TABLE_BOOK + "("
                + DatabaseContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.COLUMN_BOOK_NUMBER + " INTEGER NOT NULL UNIQUE, "
                + DatabaseContract.COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                + DatabaseContract.COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, "
                + DatabaseContract.COLUMN_BOOK_YEAR + " INTEGER NOT NULL, "
                + DatabaseContract.COLUMN_BOOK_DESCRIPION + " TEXT NOT NULL "
                + ")";

        String CREATE_REVIEW_TABLE = "CREATE TABLE " + DatabaseContract.TABLE_REVIEW + "("
                + DatabaseContract.COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.COLUMN_BOOK_NUMBER_FOREIGN + " INTEGER NOT NULL, "
                + DatabaseContract.COLUMN_REVIEWER_NAME + " TEXT NOT NULL, "
                + DatabaseContract.COLUMN_RATTING + " INTEGER NOT NULL, "
                + DatabaseContract.COLUMN_COMMENT + " TEXT NOT NULL, " //nullable
                + "FOREIGN KEY (" + DatabaseContract.COLUMN_BOOK_NUMBER_FOREIGN + ") REFERENCES " + DatabaseContract.TABLE_BOOK + "(" + DatabaseContract.COLUMN_BOOK_NUMBER + ") ON UPDATE CASCADE ON DELETE CASCADE, "
                + "CONSTRAINT " + DatabaseContract.BOOK_SUB_CONSTRAINT + " UNIQUE (" + DatabaseContract.COLUMN_BOOK_NUMBER_FOREIGN + "," + DatabaseContract.COLUMN_REVIEW_ID + ")"
                + ")";

        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_REVIEW_TABLE);

        Logger.d("DB created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_REVIEW);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}

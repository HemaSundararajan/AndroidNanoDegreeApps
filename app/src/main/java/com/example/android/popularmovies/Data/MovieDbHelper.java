package com.example.android.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hema on 6/3/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME
                + "( " + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_IMG__URL + " VARCHAR, "
                + MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " VARCHAR, "
                + MovieContract.MovieEntry.COLUMN_SYNOPSIS + " VARCHAR, "
                + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " VARCHAR, "
                + MovieContract.MovieEntry.COLUMN_USER_RATING + " REAL "
                + ");";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}

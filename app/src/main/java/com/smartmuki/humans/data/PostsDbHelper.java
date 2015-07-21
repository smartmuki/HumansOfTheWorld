package com.smartmuki.humans.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abmitra on 6/28/2015.
 */
public class PostsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "movies.db";
    public PostsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + PostsContract.PostEntry.TABLE_NAME + " (" +
                PostsContract.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PostsContract.PostEntry.COLUMN_ID+ " TEXT UNIQUE NOT NULL, " +
                PostsContract.PostEntry.COLUMN_OBJECT_ID + " REAL UNIQUE NOT NULL, " +
                PostsContract.PostEntry.COLUMN_MESSAGE+ " TEXT NOT NULL, " +
                PostsContract.PostEntry.COLUMN_PICTURE + " TEXT NOT NULL, "+
                " );";
        final String SQL_CREATE_FAV_TABLE = "CREATE TABLE " + PostsContract.FavoriteEntry.TABLE_NAME + " (" +
                PostsContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PostsContract.FavoriteEntry.COLUMN_ID+ " TEXT UNIQUE NOT NULL, " +
                PostsContract.FavoriteEntry.COLUMN_OBJECT_ID + " REAL UNIQUE NOT NULL, " +
                PostsContract.FavoriteEntry.COLUMN_MESSAGE+ " TEXT NOT NULL, " +
                PostsContract.FavoriteEntry.COLUMN_PICTURE + " TEXT NOT NULL, "+
                " );";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PostsContract.PostEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PostsContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}

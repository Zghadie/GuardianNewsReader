package com.example.guardiannewsreader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.guardiannewsreader.model.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Database helper used to store favourite Guardian articles.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "guardian_news.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ARTICLES = "articles";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_SECTION = "section";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_ARTICLES + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT NOT NULL, " +
                        COLUMN_URL + " TEXT NOT NULL UNIQUE, " +
                        COLUMN_SECTION + " TEXT NOT NULL)";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);

        onCreate(db);
    }

    /**
     * Insert a new article.
     */
    public long insertArticle(Article article) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, article.getTitle());
        values.put(COLUMN_URL, article.getUrl());
        values.put(COLUMN_SECTION, article.getSection());

        return db.insert(TABLE_ARTICLES, null, values);
    }

    /**
     * Get every saved article.
     */
    public List<Article> getAllArticles() {

        List<Article> articles = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ARTICLES,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );

        if (cursor.moveToFirst()) {

            do {

                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(COLUMN_ID));

                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_TITLE));

                String url = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_URL));

                String section = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_SECTION));

                articles.add(
                        new Article(
                                id,
                                title,
                                url,
                                section
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();

        return articles;
    }

    /**
     * Delete an article.
     */
    public int deleteArticle(long id) {

        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_ARTICLES,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    /**
     * Check if article already exists.
     */
    public boolean articleExists(String url) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ARTICLES,
                new String[]{COLUMN_ID},
                COLUMN_URL + "=?",
                new String[]{url},
                null,
                null,
                null
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();

        return exists;
    }
}
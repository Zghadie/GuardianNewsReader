package com.example.guardiannewsreader.business;

import android.content.Context;

import com.example.guardiannewsreader.data.DatabaseHelper;
import com.example.guardiannewsreader.model.Article;

import java.util.List;

/**
 * Business layer between the Activities and the SQLite database.
 */
public class ArticleRepository {

    private DatabaseHelper databaseHelper;

    /**
     * Constructor.
     *
     * @param context application context
     */
    public ArticleRepository(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    /**
     * Saves an article if it is not already stored.
     *
     * @param article article to save
     * @return true if saved successfully
     */
    public boolean saveArticle(Article article) {

        if (databaseHelper.articleExists(article.getUrl())) {
            return false;
        }

        return databaseHelper.insertArticle(article) != -1;
    }

    /**
     * Returns all saved articles.
     *
     * @return list of saved articles
     */
    public List<Article> getAllArticles() {
        return databaseHelper.getAllArticles();
    }

    /**
     * Deletes an article.
     *
     * @param article article to delete
     * @return true if deleted successfully
     */
    public boolean deleteArticle(Article article) {

        return databaseHelper.deleteArticle(article.getId()) > 0;

    }

}
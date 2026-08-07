package com.example.guardiannewsreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.guardiannewsreader.business.ArticleRepository;
import com.example.guardiannewsreader.model.Article;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays saved favourite articles.
 */
public class FavouritesActivity extends BaseActivity {

    private ListView favouritesListView;
    private TextView emptyTextView;
    private ArrayAdapter<Article> adapter;

    private final ArrayList<Article> favourites =
            new ArrayList<>();

    private ArticleRepository repository;

    /**
     * Creates the favourites screen.
     *
     * @param savedInstanceState previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActivityContent(R.layout.activity_favourites);
        setScreenTitle(R.string.favourites);

        favouritesListView =
                findViewById(R.id.favouritesListView);

        emptyTextView =
                findViewById(R.id.emptyTextView);

        repository =
                new ArticleRepository(this);

        adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        favourites
                );

        favouritesListView.setAdapter(adapter);

        favouritesListView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Article selectedArticle =
                            favourites.get(position);

                    Intent detailsIntent =
                            new Intent(
                                    FavouritesActivity.this,
                                    ArticleDetailsActivity.class
                            );

                    detailsIntent.putExtra(
                            ArticleDetailsActivity.EXTRA_ARTICLE,
                            selectedArticle
                    );

                    startActivity(detailsIntent);
                }
        );

        favouritesListView.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    Article selectedArticle =
                            favourites.get(position);

                    boolean deleted =
                            repository.deleteArticle(
                                    selectedArticle
                            );

                    if (deleted) {

                        favourites.remove(position);
                        adapter.notifyDataSetChanged();
                        updateEmptyMessage();

                        Snackbar.make(
                                findViewById(R.id.favouritesRoot),
                                "Favourite deleted",
                                Snackbar.LENGTH_LONG
                        ).show();
                    }

                    return true;
                }
        );
    }

    /**
     * Reloads favourites whenever the activity returns.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFavourites();
    }

    /**
     * Loads all saved articles from SQLite.
     */
    private void loadFavourites() {

        List<Article> savedArticles =
                repository.getAllArticles();

        favourites.clear();
        favourites.addAll(savedArticles);
        adapter.notifyDataSetChanged();

        updateEmptyMessage();
    }

    /**
     * Displays a message when no favourites exist.
     */
    private void updateEmptyMessage() {

        if (favourites.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getHelpMessageResource() {
        return R.string.help_favourites;
    }
}
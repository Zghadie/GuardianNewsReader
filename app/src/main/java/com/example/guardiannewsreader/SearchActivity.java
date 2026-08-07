package com.example.guardiannewsreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.guardiannewsreader.model.Article;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Allows the user to search Guardian news articles.
 */
@SuppressWarnings("deprecation")
public class SearchActivity extends BaseActivity {

    private static final String API_KEY = "test";

    private static final String PREFERENCES_NAME =
            "guardian_preferences";

    private static final String LAST_SEARCH_KEY =
            "last_search";

    private EditText searchEditText;
    private Button searchButton;
    private ProgressBar progressBar;
    private ListView resultsListView;

    private final ArrayList<Article> articles =
            new ArrayList<>();

    private ArrayAdapter<Article> adapter;

    /**
     * Initializes the search interface.
     *
     * @param savedInstanceState previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActivityContent(R.layout.activity_search);
        setScreenTitle(R.string.search);

        searchEditText =
                findViewById(R.id.searchEditText);

        searchButton =
                findViewById(R.id.searchButton);

        progressBar =
                findViewById(R.id.progressBar);

        resultsListView =
                findViewById(R.id.resultsListView);

        adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        articles
                );

        resultsListView.setAdapter(adapter);

        loadLastSearch();

        searchButton.setOnClickListener(
                view -> beginSearch()
        );

        resultsListView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Article selectedArticle =
                            articles.get(position);

                    Intent detailsIntent =
                            new Intent(
                                    SearchActivity.this,
                                    ArticleDetailsActivity.class
                            );

                    detailsIntent.putExtra(
                            ArticleDetailsActivity.EXTRA_ARTICLE,
                            selectedArticle
                    );

                    startActivity(detailsIntent);
                }
        );
    }

    /**
     * Loads the previously entered search term.
     */
    private void loadLastSearch() {

        SharedPreferences preferences =
                getSharedPreferences(
                        PREFERENCES_NAME,
                        MODE_PRIVATE
                );

        String lastSearch =
                preferences.getString(
                        LAST_SEARCH_KEY,
                        ""
                );

        searchEditText.setText(lastSearch);
    }

    /**
     * Validates the search and starts the AsyncTask.
     */
    private void beginSearch() {

        String searchTerm =
                searchEditText
                        .getText()
                        .toString()
                        .trim();

        if (searchTerm.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter a search term",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        saveLastSearch(searchTerm);

        new GuardianSearchTask()
                .execute(searchTerm);
    }

    /**
     * Saves the most recent search term.
     *
     * @param searchTerm search entered by the user
     */
    private void saveLastSearch(String searchTerm) {

        SharedPreferences preferences =
                getSharedPreferences(
                        PREFERENCES_NAME,
                        MODE_PRIVATE
                );

        SharedPreferences.Editor editor =
                preferences.edit();

        editor.putString(
                LAST_SEARCH_KEY,
                searchTerm
        );

        editor.apply();
    }

    /**
     * AsyncTask used to retrieve Guardian articles.
     */
    private class GuardianSearchTask
            extends AsyncTask<String, Void, ArrayList<Article>> {

        private boolean requestFailed = false;

        /**
         * Displays the progress bar before downloading.
         */
        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
            searchButton.setEnabled(false);

            articles.clear();
            adapter.notifyDataSetChanged();
        }

        /**
         * Downloads and parses Guardian JSON data.
         *
         * @param searchTerms search terms passed to the task
         * @return downloaded articles
         */
        @Override
        protected ArrayList<Article> doInBackground(
                String... searchTerms
        ) {

            ArrayList<Article> downloadedArticles =
                    new ArrayList<>();

            HttpURLConnection connection = null;

            try {

                String encodedSearchTerm =
                        URLEncoder.encode(
                                searchTerms[0],
                                "UTF-8"
                        );

                String apiUrl =
                        "https://content.guardianapis.com/search"
                                + "?api-key=" + API_KEY
                                + "&q=" + encodedSearchTerm
                                + "&page-size=30";

                URL url =
                        new URL(apiUrl);

                connection =
                        (HttpURLConnection)
                                url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                int responseCode =
                        connection.getResponseCode();

                if (responseCode !=
                        HttpURLConnection.HTTP_OK) {

                    requestFailed = true;
                    return downloadedArticles;
                }

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        connection.getInputStream()
                                )
                        );

                StringBuilder jsonBuilder =
                        new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                reader.close();

                JSONObject rootObject =
                        new JSONObject(
                                jsonBuilder.toString()
                        );

                JSONObject responseObject =
                        rootObject.getJSONObject(
                                "response"
                        );

                JSONArray resultsArray =
                        responseObject.getJSONArray(
                                "results"
                        );

                for (
                        int index = 0;
                        index < resultsArray.length();
                        index++
                ) {

                    JSONObject articleObject =
                            resultsArray.getJSONObject(
                                    index
                            );

                    String title =
                            articleObject.optString(
                                    "webTitle",
                                    "Untitled"
                            );

                    String articleUrl =
                            articleObject.optString(
                                    "webUrl",
                                    ""
                            );

                    String section =
                            articleObject.optString(
                                    "sectionName",
                                    "Unknown"
                            );

                    Article article =
                            new Article(
                                    title,
                                    articleUrl,
                                    section
                            );

                    downloadedArticles.add(article);
                }

            } catch (Exception exception) {

                requestFailed = true;
                exception.printStackTrace();

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

            return downloadedArticles;
        }

        /**
         * Displays downloaded results.
         *
         * @param downloadedArticles search results
         */
        @Override
        protected void onPostExecute(
                ArrayList<Article> downloadedArticles
        ) {

            progressBar.setVisibility(View.GONE);
            searchButton.setEnabled(true);

            if (requestFailed) {

                Snackbar.make(
                        findViewById(R.id.searchRoot),
                        "Unable to retrieve articles. Check your connection.",
                        Snackbar.LENGTH_LONG
                ).show();

                return;
            }

            articles.addAll(downloadedArticles);
            adapter.notifyDataSetChanged();

            if (articles.isEmpty()) {

                Toast.makeText(
                        SearchActivity.this,
                        "No articles found",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        SearchActivity.this,
                        articles.size() + " articles found",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    /**
     * Returns the help message for this screen.
     *
     * @return search help string resource
     */
    @Override
    protected int getHelpMessageResource() {
        return R.string.help_search;
    }
}
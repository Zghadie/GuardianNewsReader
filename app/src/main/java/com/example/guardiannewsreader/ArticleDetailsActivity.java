package com.example.guardiannewsreader;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guardiannewsreader.model.Article;

/**
 * Displays the selected Guardian article.
 */
public class ArticleDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ARTICLE = "extra_article";

    /**
     * Creates the details screen.
     *
     * @param savedInstanceState previously saved activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_details);

        Article article =
                (Article) getIntent()
                        .getSerializableExtra(EXTRA_ARTICLE);

        if (savedInstanceState == null) {

            ArticleDetailsFragment fragment =
                    ArticleDetailsFragment.newInstance(article);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.detailsFragmentContainer,
                            fragment
                    )
                    .commit();
        }
    }
}
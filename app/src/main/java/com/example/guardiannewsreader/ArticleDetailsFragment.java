package com.example.guardiannewsreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.guardiannewsreader.business.ArticleRepository;
import com.example.guardiannewsreader.model.Article;

/**
 * Displays the selected Guardian article inside a Fragment.
 */
public class ArticleDetailsFragment extends Fragment {

    private static final String ARG_ARTICLE = "article";

    private Article article;

    /**
     * Creates a new fragment containing one article.
     *
     * @param article selected article
     * @return configured fragment
     */
    public static ArticleDetailsFragment newInstance(Article article) {

        ArticleDetailsFragment fragment =
                new ArticleDetailsFragment();

        Bundle arguments =
                new Bundle();

        arguments.putSerializable(
                ARG_ARTICLE,
                article
        );

        fragment.setArguments(arguments);

        return fragment;
    }

    /**
     * Reads the selected article from the fragment arguments.
     */
    @Override
    public void onCreate(
            @Nullable Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            article =
                    (Article) getArguments()
                            .getSerializable(ARG_ARTICLE);
        }
    }

    /**
     * Creates the fragment interface.
     */
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(
                        R.layout.fragment_article_details,
                        container,
                        false
                );

        TextView titleTextView =
                view.findViewById(R.id.detailTitleTextView);

        TextView sectionTextView =
                view.findViewById(R.id.detailSectionTextView);

        TextView urlTextView =
                view.findViewById(R.id.detailUrlTextView);

        Button openButton =
                view.findViewById(R.id.openArticleButton);

        Button saveButton =
                view.findViewById(R.id.saveFavouriteButton);

        if (article != null) {

            titleTextView.setText(
                    article.getTitle()
            );

            sectionTextView.setText(
                    "Section: " + article.getSection()
            );

            urlTextView.setText(
                    article.getUrl()
            );

            openButton.setOnClickListener(v -> {

                Intent browserIntent =
                        new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(article.getUrl())
                        );

                startActivity(browserIntent);
            });

            saveButton.setOnClickListener(v -> {

                ArticleRepository repository =
                        new ArticleRepository(
                                requireContext()
                        );

                boolean saved =
                        repository.saveArticle(article);

                if (saved) {

                    Toast.makeText(
                            requireContext(),
                            "Article saved to favourites",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    Toast.makeText(
                            requireContext(),
                            "Article is already saved",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }

        return view;
    }
}
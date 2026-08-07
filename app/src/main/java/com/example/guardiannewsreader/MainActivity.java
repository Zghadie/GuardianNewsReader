package com.example.guardiannewsreader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * Main landing screen for the application.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActivityContent(R.layout.activity_main);
        setScreenTitle(R.string.home);

        Button searchButton =
                findViewById(R.id.homeSearchButton);

        Button favouritesButton =
                findViewById(R.id.homeFavouritesButton);

        searchButton.setOnClickListener(view ->
                startActivity(
                        new Intent(
                                MainActivity.this,
                                SearchActivity.class
                        )
                )
        );

        favouritesButton.setOnClickListener(view ->
                startActivity(
                        new Intent(
                                MainActivity.this,
                                FavouritesActivity.class
                        )
                )
        );
    }

    @Override
    protected int getHelpMessageResource() {
        return R.string.help_home;
    }
}
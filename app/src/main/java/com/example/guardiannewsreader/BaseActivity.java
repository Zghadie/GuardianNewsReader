package com.example.guardiannewsreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * Provides the shared Toolbar, Navigation Drawer,
 * version number, and Help dialog.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.drawerLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView =
                findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);

        toolbar.setSubtitle("Version 1.0");

        toolbar.setNavigationIcon(
                android.R.drawable.ic_menu_sort_by_size
        );

        toolbar.setNavigationOnClickListener(view ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        navigationView.setNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();

            drawerLayout.closeDrawer(GravityCompat.START);

            if (itemId == R.id.navHome) {

                if (!(this instanceof MainActivity)) {
                    startActivity(
                            new Intent(this, MainActivity.class)
                    );
                }

                return true;
            }

            if (itemId == R.id.navSearch) {

                if (!(this instanceof SearchActivity)) {
                    startActivity(
                            new Intent(this, SearchActivity.class)
                    );
                }

                return true;
            }

            if (itemId == R.id.navFavourites) {

                if (!(this instanceof FavouritesActivity)) {
                    startActivity(
                            new Intent(this, FavouritesActivity.class)
                    );
                }

                return true;
            }

            return false;
        });
    }

    /**
     * Places an activity layout inside the shared content area.
     *
     * @param layoutResource layout to display
     */
    protected void setActivityContent(
            @LayoutRes int layoutResource
    ) {

        FrameLayout contentFrame =
                findViewById(R.id.contentFrame);

        getLayoutInflater().inflate(
                layoutResource,
                contentFrame,
                true
        );
    }

    /**
     * Sets the Toolbar title.
     *
     * @param titleResource title string resource
     */
    protected void setScreenTitle(int titleResource) {

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(titleResource);

            getSupportActionBar().setSubtitle("Version 1.0");
        }
    }

    /**
     * Returns the help instructions for the current Activity.
     *
     * @return help-message resource
     */
    protected abstract int getHelpMessageResource();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.toolbar_menu,
                menu
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(
            @NonNull MenuItem item
    ) {

        int itemId = item.getItemId();

        if (itemId == R.id.actionHome) {

            if (!(this instanceof MainActivity)) {
                startActivity(
                        new Intent(this, MainActivity.class)
                );
            }

            return true;
        }

        if (itemId == R.id.actionSearch) {

            if (!(this instanceof SearchActivity)) {
                startActivity(
                        new Intent(this, SearchActivity.class)
                );
            }

            return true;
        }

        if (itemId == R.id.actionFavourites) {

            if (!(this instanceof FavouritesActivity)) {
                startActivity(
                        new Intent(this, FavouritesActivity.class)
                );
            }

            return true;
        }

        if (itemId == R.id.actionHelp) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.help)
                    .setMessage(getHelpMessageResource())
                    .setPositiveButton(
                            android.R.string.ok,
                            null
                    )
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
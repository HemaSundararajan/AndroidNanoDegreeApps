package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Boolean mTabletMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.container)!= null){
            mTabletMode = true;
            MovieDetailsActivityFragment detailFragment = new MovieDetailsActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isTablet() {
        return mTabletMode;
    }

    public void replaceFragment(Movie movie,String sortOrder) {

        MovieDetailsActivityFragment detailFragment = new MovieDetailsActivityFragment();
        Bundle args = new Bundle();
        if(sortOrder == "favourites") {
            movie.setIsFavourite(true);
        }
        args.putSerializable("Movie", movie);
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
    }
}

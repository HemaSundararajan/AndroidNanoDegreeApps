package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class MovieMainFragment extends Fragment{

    private MovieArrayAdapter movieArrayAdapter = null;
    private FetchMoviesTask movieUrlsTask;
    private String sortOrder;
    private Boolean mTablet;

    public MovieMainFragment() {
        // Required empty public constructor
        sortOrder="popularity.desc";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.moviemenuoptions, menu);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_main, container, false);

        movieArrayAdapter = new MovieArrayAdapter(getActivity(), new ArrayList<Movie>());
        GridView gridView = (GridView) rootView.findViewById(R.id.movieGridView);
        gridView.setAdapter(movieArrayAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTablet = ((MainActivity) getActivity()).isTablet();
                Movie movie = movieArrayAdapter.getItem(position);
                Intent movieIntent = new Intent(getContext(), MovieDetailsActivity.class);
                if(!mTablet) {
                    if(sortOrder == "favourites") {
                        movie.setIsFavourite(true);
                    }
                    movieIntent.putExtra("Movie", movie);
                    startActivity(movieIntent);
                } else {
                    ((MainActivity)getActivity()).replaceFragment(movie,sortOrder);
                }
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortOrder = prefs.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_default));
        if(sortOrder.equals("favourites")) {
            FetchFavouritesTask fetchFavouritesTask = new FetchFavouritesTask(getContext(),movieArrayAdapter);
            fetchFavouritesTask.execute();
        }
        else {
            movieUrlsTask = new FetchMoviesTask(getContext(),movieArrayAdapter);
            movieUrlsTask.execute(sortOrder);
        }
    }
}

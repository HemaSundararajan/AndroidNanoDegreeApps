package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieMainFragment extends Fragment {

    private MovieArrayAdapter movieArrayAdapter = null;
    private FetchMoviesTask movieUrlsTask;
    private String sortOrder;

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
                Movie movie = movieArrayAdapter.getItem(position);
                Intent movieIntent = new Intent(getContext(), MovieDetailsActivity.class);
                movieIntent.putExtra("Movie", movie);
                startActivity(movieIntent);
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

        movieUrlsTask = new FetchMoviesTask(getContext(),movieArrayAdapter);
        movieUrlsTask.execute(sortOrder);
    }

}

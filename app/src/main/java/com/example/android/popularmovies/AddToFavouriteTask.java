package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.popularmovies.Data.MovieContract;

/**
 * Created by hema on 19/3/16.
 */
public class AddToFavouriteTask extends AsyncTask<Movie, Void, Void> {

    private final Context mContext;

    public AddToFavouriteTask(Context context) {
        mContext = context;
    }
    @Override
    protected Void doInBackground(Movie... params) {
        Movie saveMovie = params[0];
        long movieId;

        // First, check if the location with this city name exists in the db
        Cursor locationCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(saveMovie.getMovieId())},
                null);

        if (locationCursor != null && locationCursor.moveToFirst()) {
            int locationIdIndex = locationCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            movieId = locationCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues movieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, saveMovie.getMovieId());
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMG__URL, saveMovie.getMovieImageUrl());
            movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, saveMovie.getOriginalTitle());
            movieValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, saveMovie.getSynopsis());
            movieValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, saveMovie.getUserRating());
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, saveMovie.getReleaseDate());

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    movieValues
            );
            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            //movieId = (Long)ContentUris.parseId(insertedUri);
        }

        locationCursor.close();
        // Wait, that worked?  Yes!
        //return movieId;
        return null;
    }
}

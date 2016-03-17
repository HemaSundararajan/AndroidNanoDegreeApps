package com.example.android.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.android.popularmovies.Data.MovieContract;

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

/**
 * Created by hema on 13/3/16.
 */
public class FetchMoviesTask extends AsyncTask <String, Void, String[]> {

    private ArrayAdapter<Movie> mMovieAdapter;
    private final Context mContext;

    public FetchMoviesTask(Context context, ArrayAdapter<Movie> MovieArrayAdapter) {
        mContext = context;
        mMovieAdapter = MovieArrayAdapter;
    }

    private ArrayList<Movie> movieDetails;

    public void setImageUrls(String jsonString) throws JSONException {
        movieDetails = new ArrayList<Movie>();
        JSONObject movieJson = new JSONObject(jsonString);
        JSONArray movieDetailsArray = movieJson.getJSONArray("results");
        for(int i=0;i<movieDetailsArray.length();i++) {
            Movie movie = new Movie();
            String moviePath = movieDetailsArray.getJSONObject(i).getString("poster_path");
            movie.setMovieId(movieDetailsArray.getJSONObject(i).getInt("id"));
            movie.setMovieImageUrl("http://image.tmdb.org/t/p/w185" + moviePath);
            movie.setOriginalTitle(movieDetailsArray.getJSONObject(i).getString("original_title"));
            movie.setSynopsis(movieDetailsArray.getJSONObject(i).getString("overview"));
            movie.setUserRating(movieDetailsArray.getJSONObject(i).getDouble("vote_average"));
            movie.setReleaseDate(movieDetailsArray.getJSONObject(i).getString("release_date"));
            movieDetails.add(movie);
        }
    }

    long addMovie(Movie movie) {

        long movieId;

        // First, check if the location with this city name exists in the db
        Cursor locationCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movie.getMovieId())},
                null);

        if (locationCursor.moveToFirst()) {
            int locationIdIndex = locationCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            movieId = locationCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues movieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMG__URL, movie.getMovieImageUrl());
            movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
            movieValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
            movieValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, movie.getUserRating());
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    movieValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            movieId = ContentUris.parseId(insertedUri);
        }

        locationCursor.close();
        // Wait, that worked?  Yes!
        return movieId;
    }

    @Override
    protected String[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by", params[0])
                    .appendQueryParameter("api_key", "6d08284342d5ac2f8c6cc8e8669be4e7");

            URL url = new URL (builder.build().toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            String movieApiResponse = buffer.toString();
            System.out.print(movieApiResponse);
            setImageUrls(movieApiResponse);

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MovieMainFragment", "Error closing stream", e);
                }
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(String[] result) {
        if(movieDetails != null) {
            mMovieAdapter.clear();

            for (Movie movie : movieDetails) {
                System.out.println("ImageUrlAfterExecution" + movie.getMovieImageUrl());
                mMovieAdapter.add(movie);
            }
        }
    }
}


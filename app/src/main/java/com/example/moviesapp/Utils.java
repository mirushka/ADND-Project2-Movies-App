package com.example.moviesapp;


import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {

    //URL for fetching data from theMovieDB
    public static final String THE_MOVIE_DATABASE_URL = "http://api.themoviedb.org/3/movie/";

    //API keys for Top Rated Films
    public static final String MOVIE_API_KEY = "api_key";
    public static final String RESULTS = "results";
    public static final String TITLE = "title";
    public static final String RELEASE_DATE = "release_date";
    public static final String FILM_POSTER = "poster_path";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String PLOT_SYNOPSIS = "overview";

    public static URL buildURL(String sortMode) {
        Uri buildUri = Uri.parse(THE_MOVIE_DATABASE_URL).buildUpon()
                .appendPath(sortMode)
                .appendQueryParameter(MOVIE_API_KEY, BuildConfig.THE_MOVIE_DATABASE_API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(buildUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream input = urlConnection.getInputStream();
            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<Movie> fetchMoviesData(String searchUrl) {

        URL url = buildURL(searchUrl);

        String jsonResponde = null;
        try {
            jsonResponde = getResponseFromUrl(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return etractMoviesFromJson(jsonResponde);


    }

    private static ArrayList<Movie> etractMoviesFromJson(String movieJSON) {

        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            JSONObject currentFilm;

            JSONArray filmArray = baseJsonResponse.getJSONArray(RESULTS);

            int i = 0;
            for (i = 0; i < filmArray.length(); i++) {
                String filmTitle = "";
                String releaseDate = "";
                String filmPoster = "";
                double voteAverage;
                String plotSynopsis = "";

                currentFilm = filmArray.getJSONObject(i);
                filmTitle = currentFilm.optString(TITLE);
                releaseDate = currentFilm.optString(RELEASE_DATE);
                filmPoster = currentFilm.optString(FILM_POSTER);
                voteAverage = currentFilm.optDouble(VOTE_AVERAGE);
                plotSynopsis = currentFilm.optString(PLOT_SYNOPSIS);

                Movie movie = new Movie(filmTitle, releaseDate, filmPoster, voteAverage, plotSynopsis);
                movies.add(movie);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }


}

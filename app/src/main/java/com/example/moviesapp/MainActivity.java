package com.example.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClicked {

    static final String SORT_ORDER_MOVIE = "sort_order_movie";
    static final String RECYCLER_VIEW_LAYOUT = "recycler_view_layout";
    private static final String TAG = MainActivity.class.getSimpleName();
    ArrayList<Movie> movies = new ArrayList<>();
    Context context;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private RecyclerView moviesRecyclerView;
    private String sortOrder = "popular";
    private Parcelable layoutManagerSaedState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = (RecyclerView) findViewById(R.id.rv);
        movieAdapter = new MovieAdapter(movies, this);
        moviesRecyclerView.setAdapter(movieAdapter);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter.setOnClick(this);


        if (!isOnline()) {
            setContentView(R.layout.activity_intrenet);

        }

        if (savedInstanceState != null) {
            sortOrder = savedInstanceState.getString(SORT_ORDER_MOVIE);
        }
        new MoviesAsyncTask().execute(sortOrder);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SORT_ORDER_MOVIE, sortOrder);
        savedInstanceState.putParcelable(RECYCLER_VIEW_LAYOUT, moviesRecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        layoutManagerSaedState = savedInstanceState.getParcelable(RECYCLER_VIEW_LAYOUT);
        moviesRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState);
    }

    public void setOnClick(List movies) {
        movieAdapter.setOnClick((MovieAdapter.OnItemClicked) movies);
        restoreLayoutManagerPosition();
    }

    private void restoreLayoutManagerPosition() {
        if (layoutManagerSaedState != null) {
            moviesRecyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSaedState);
        }
    }

    @Override
    public void onItemClick(int position) {
        Movie selectedMovie = movieList.get(position);
        Intent i = new Intent(this, MovieDetailsActivity.class);
        i.putExtra("movie", selectedMovie);
        startActivity(i);
    }


    public boolean isOnline() {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return connected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        CharSequence message;
        switch (item.getItemId()) {
            case R.id.popularMovies:
                message = "Popular Movies selected";
                sortOrder = "popular";
                new MoviesAsyncTask().execute(sortOrder);
                break;

            case R.id.top_rated:
                message = "Top Rated Movies selected";
                sortOrder = "top_rated";
                new MoviesAsyncTask().execute(sortOrder);
                break;

            default:
                return super.onContextItemSelected(item);
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }

    private class MoviesAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {


        @Override
        protected ArrayList<Movie> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            ArrayList<Movie> result = Utils.fetchMoviesData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            if (movies != null && !movies.isEmpty()) {
                movieList = movies;
                movieAdapter.addAll(movies);
                restoreLayoutManagerPosition();
            }
        }
    }
}

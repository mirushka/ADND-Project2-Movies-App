package com.example.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    Context context;
    private Movie selectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        selectedMovie = intent.getParcelableExtra("movie");

        if (selectedMovie != null) {
            TextView filmTitle = (TextView) findViewById(R.id.tv_title);
            filmTitle.setText(selectedMovie.getFilmTitle());

            ImageView filmPoster = (ImageView) findViewById(R.id.iv_posterDetail);
            Picasso.with(context).load(selectedMovie.getFilmPoster()).into(filmPoster);

            TextView release = (TextView) findViewById(R.id.tv_releaseDate);
            release.setText(selectedMovie.getReleaseDate());

            TextView voting = (TextView) findViewById(R.id.tv_voteAverage);
            voting.setText(Double.toString(selectedMovie.getVoteAverage()));

            TextView plot = (TextView) findViewById(R.id.tv_plot);
            plot.setText(selectedMovie.getPlotSynopsis());

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

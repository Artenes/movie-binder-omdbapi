package moviebinder.io.github.artenes.moviebinder;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ActivityMovieDetails extends AppCompatActivity {

    private final String TAG = ActivityMovieDetails.class.getSimpleName();

    private ImageView imgPoster;

    private TextView
    txvTitle,
    txvGenre,
    txvPlot,
    txvYear,
    txvRated,
    txvReleased,
    txvRuntime,
    txvDirector,
    txvWriter,
    txvActors,
    txvLanguage,
    txvCountry,
    txvAwards,
    txvMetaScore,
    txvRating,
    txvVotes;

    public final static String KEY_MOVIE_ID = "movieId";

    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            movieId = bundle.getString(KEY_MOVIE_ID);
        } else {
            finish();
        }

        imgPoster = (ImageView) findViewById(R.id.imgPoster);

        txvTitle = (TextView) findViewById(R.id.txvTitle);
        txvGenre = (TextView) findViewById(R.id.txvGenre);
        txvPlot = (TextView) findViewById(R.id.txvPlot);
        txvYear = (TextView) findViewById(R.id.txvYear);
        txvRated = (TextView) findViewById(R.id.txvRated);
        txvReleased = (TextView) findViewById(R.id.txvReleased);
        txvRuntime = (TextView) findViewById(R.id.txvRunTime);
        txvDirector = (TextView) findViewById(R.id.txvDirector);
        txvWriter = (TextView) findViewById(R.id.txvWriter);
        txvActors = (TextView) findViewById(R.id.txvActors);
        txvLanguage = (TextView) findViewById(R.id.txvLanguage);
        txvCountry = (TextView) findViewById(R.id.txvCountry);
        txvAwards = (TextView) findViewById(R.id.txvAwards);
        txvMetaScore = (TextView) findViewById(R.id.txvMetascore);
        txvRating = (TextView) findViewById(R.id.txvRating);
        txvVotes = (TextView) findViewById(R.id.txvVotes);

    }

    @Override
    protected void onResume() {
        super.onResume();

        new AsyncTask<String, Void, Movie>(){

            @Override
            protected Movie doInBackground(String... params) {

                SQLiteDatabase connection = null;

                try{

                    connection = new DBAccess(ActivityMovieDetails.this).getReadableDatabase();
                    MovieDAO dao = new MovieDAO(connection);
                    return dao.getMovieById(params[0]);

                } catch (Exception error) {

                    Log.e(TAG, error.getMessage());
                    return null;

                } finally {

                    if (connection != null)
                        connection.close();

                }

            }

            @Override
            protected void onPostExecute(Movie movie) {

                if (movie == null) {
                    finish();
                    return;
                }

                Glide.with(ActivityMovieDetails.this)
                        .load(movie.poster)
                        .placeholder(R.mipmap.img_loading)
                        .error(R.mipmap.img_not_available)
                        .into(imgPoster);

                txvTitle.setText(movie.title);
                txvGenre.setText(movie.genre);
                txvPlot.setText(movie.plot);
                txvYear.setText(movie.year);
                txvRated.setText(movie.rated);
                txvReleased.setText(movie.released);
                txvRuntime.setText(movie.runtime);
                txvDirector.setText(movie.director);
                txvWriter.setText(movie.writer);
                txvActors.setText(movie.actors);
                txvLanguage.setText(movie.language);
                txvCountry.setText(movie.country);
                txvAwards.setText(movie.awards);
                txvMetaScore.setText(movie.metascore);
                txvRating.setText(movie.metascore);
                txvVotes.setText(movie.imdbVotes);

            }
        }.execute(new String[]{movieId});

    }
}
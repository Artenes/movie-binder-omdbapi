package moviebinder.io.github.artenes.moviebinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    private EditText edtMovieName;
    private ListView lstView;
    private AdapterMovieListing adapter;
    private final String TAG = ActivityMain.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtMovieName = (EditText) findViewById(R.id.edtMovieName);
        lstView = (ListView) findViewById(R.id.lstMovieListing);

        adapter = new AdapterMovieListing(this, R.layout.row_movie, new ArrayList<Movie>());
        lstView.setAdapter(adapter);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {

                Intent intent = new Intent(ActivityMain.this, ActivityMovieDetails.class);
                intent.putExtra(ActivityMovieDetails.KEY_MOVIE_ID, adapter.getItem(index)._id);
                ActivityMain.this.startActivity(intent);
            }
        });

    }

    public void addMovie (View view) {
        String movieTitle = edtMovieName.getText().toString();
        new TaskSearchMovie(
                this,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new GetMoviesFromDB().execute();
                    }
                }).execute(new String[]{movieTitle});
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetMoviesFromDB().execute();
    }

    protected class GetMoviesFromDB extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Void... voids) {

            SQLiteDatabase connection = null;

            try {

                connection = new DBAccess(ActivityMain.this).getReadableDatabase();
                MovieDAO dao = new MovieDAO(connection);
                return dao.getAllMovies();

            } catch (Exception error) {

                Log.e(TAG, error.getMessage());

            } finally {

                if (connection != null)
                    connection.close();

            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {

            adapter.clear();
            for (Movie movie : movies) {
                adapter.add(movie);
            }

            if (adapter.getCount() == 0)
                Toast.makeText(ActivityMain.this, ActivityMain.this.getString(R.string.strNoMoviesFound), Toast.LENGTH_SHORT).show();

        }
    }
}
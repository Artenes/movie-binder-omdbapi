package moviebinder.io.github.artenes.moviebinder;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityMovieResolution extends AppCompatActivity {

    public static final String JSON_RESPONSE_KEY = "json_response_key";
    public static final String TAG = ActivityMovieResolution.class.getSimpleName();

    private String jsonResponse = null;

    private ListView lstMovieResolution;
    private AdapterMovieListing adapter;

    private HttpRequests request = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_resolution);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jsonResponse = bundle.getString(JSON_RESPONSE_KEY);
        } else {
            finish();
        }

        request = new HttpRequests();

        lstMovieResolution = (ListView) findViewById(R.id.lstMoviesResolution);
        adapter = new AdapterMovieListing(this, R.layout.row_movie, new ArrayList<Movie>());
        lstMovieResolution.setAdapter(adapter);

        lstMovieResolution.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {

                TaskInsertMovie task = new TaskInsertMovie(
                        ActivityMovieResolution.this,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }
                );
                task.execute(new String[]{adapter.getItem(index).imdbID});

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new AsyncTask<Void, Void, List<Movie>>(){

            @Override
            protected List<Movie> doInBackground(Void... voids) {

                try {

                    JSONObject jsonResult = new JSONObject(jsonResponse);
                    return request.parseSearch(jsonResult.getJSONArray(HttpRequests.SEARCH_KEY));

                } catch (JSONException error){

                    Log.e(TAG, error.getMessage());

                }

                return null;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {

                if (movies != null) {
                    adapter.clear();
                    for (Movie movie : movies){
                        adapter.add(movie);
                    }
                }

            }
        }.execute();

    }

}
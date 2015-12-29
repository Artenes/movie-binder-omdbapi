package moviebinder.io.github.artenes.moviebinder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Artenes on 29/12/2015.
 */
public class TaskSearchMovie extends AsyncTask <String, Void, String> {

    private final String TAG = TaskSearchMovie.class.getSimpleName();

    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private Context context;
    private HttpRequests httpRequest;

    private String errorStatus = null;
    private DialogInterface.OnClickListener action;

    public TaskSearchMovie(Context context, DialogInterface.OnClickListener action) {

        this.context = context;

        progressDialog = new ProgressDialog(context);
        alertDialog = new AlertDialog.Builder(context);
        httpRequest = new HttpRequests();

        errorStatus = context.getString(R.string.strErrorMessage);

        this.action = action;
    }

    @Override
    protected void onPreExecute() {

        progressDialog.setMessage(context.getString(R.string.strFetchingMovieInfo));
        progressDialog.setCancelable(false);

        alertDialog.setNeutralButton(context.getString(R.string.strOk), null);
        alertDialog.setCancelable(false);

        progressDialog.show();

    }

    @Override
    protected String doInBackground(String... strings) {

        String movieTitle = strings[0];

        try {

            String jsonResponse = httpRequest.searchMovies(movieTitle);

            if (jsonResponse == null) {
                errorStatus = context.getString(R.string.strConnectionError);
                return null;
            }

            JSONObject response = new JSONObject(jsonResponse);

            if (!response.has(HttpRequests.SEARCH_KEY)) {
                errorStatus = context.getString(R.string.strMovieNotFound);
                return null;
            }

            return jsonResponse;

        } catch (Exception error) {

            Log.e(TAG, error.getMessage());

            return null;

        }

    }

    @Override
    protected void onPostExecute(String jsonResponse) {

        if (jsonResponse == null) {
            displayAlertDialog(context.getString(R.string.strOhNo), errorStatus);
            return;
        }


        try {

            JSONObject jsonMovies = new JSONObject(jsonResponse);
            JSONArray movies = jsonMovies.getJSONArray(HttpRequests.SEARCH_KEY);

            if (movies.length() > 1) {

                Intent intent = new Intent(context, ActivityMovieResolution.class);
                intent.putExtra(ActivityMovieResolution.JSON_RESPONSE_KEY, jsonResponse);

                progressDialog.dismiss();

                context.startActivity(intent);

            } else {

                JSONObject movie = movies.getJSONObject(0);
                String imdbID = movie.getString(DBContract.Movies.IMDBID);
                TaskInsertMovie task = new TaskInsertMovie(context, action);

                progressDialog.dismiss();

                task.execute(new String[]{imdbID});

            }

        } catch (JSONException error) {

            Log.e(TAG, error.getMessage());

            displayAlertDialog(context.getString(R.string.strOhNo), context.getString(R.string.strErrorMessage));

        }

    }

    private void displayAlertDialog (String title, String message) {

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        progressDialog.dismiss();
        alertDialog.show();

    }
}
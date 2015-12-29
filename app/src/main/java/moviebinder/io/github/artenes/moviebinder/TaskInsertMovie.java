package moviebinder.io.github.artenes.moviebinder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Artenes on 28/12/2015.
 */
public class TaskInsertMovie extends AsyncTask<String, Void, String> {

    private final String TAG = TaskInsertMovie.class.getSimpleName();

    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private Context context;
    private HttpRequests httpRequest;

    private String errorStatus = null;

    public TaskInsertMovie(Context context, DialogInterface.OnClickListener action) {

        this.context = context;

        progressDialog = new ProgressDialog(context);
        alertDialog = new AlertDialog.Builder(context);
        httpRequest = new HttpRequests();

        errorStatus = context.getString(R.string.strErrorMessage);

        alertDialog.setNeutralButton(context.getString(R.string.strOk), action);

    }

    @Override
    protected void onPreExecute() {

        progressDialog.setMessage(context.getString(R.string.strFetchingMovieInfo));
        progressDialog.setCancelable(false);

        alertDialog.setCancelable(false);

        progressDialog.show();

    }

    @Override
    protected String doInBackground(String ...params) {

        String imdbId = params[0];
        SQLiteDatabase connection = null;

        try {

            String jsonResponse = httpRequest.fetchMovie(imdbId);

            if (jsonResponse == null) {
                errorStatus = context.getString(R.string.strConnectionError);
                return null;
            }

            JSONObject response = new JSONObject(jsonResponse);

            if (!response.has(DBContract.Movies.TITLE)) {
                errorStatus = context.getString(R.string.strMovieNotFound);
                return null;
            }

            Movie movie = httpRequest.parseMovie(response);

            connection = new DBAccess(context).getWritableDatabase();
            MovieDAO dao = new MovieDAO(connection);


            if (dao.doesMovieExistsInDB(movie.imdbID)) {
                errorStatus = context.getString(R.string.strMovieAlreadyExists);
                return null;
            }

            movie.poster = httpRequest.downloadAndSavePoster(movie.poster);

            dao.insertMovie(movie);
            Log.v(TAG, "Inserted movie: " + movie.title);

            return movie.title;

        } catch (Exception error) {

            Log.e(TAG, error.getMessage());
            return null;

        } finally {

            if (connection != null)
                connection.close();

        }

    }

    @Override
    protected void onPostExecute(String movieTitle) {

        if (movieTitle != null) {
            displayAlertDialog(context.getString(R.string.strWellDone), movieTitle + " " + context.getString(R.string.strMovieAdded));
        } else {
            displayAlertDialog(context.getString(R.string.strOhNo), errorStatus);
        }

    }

    private void displayAlertDialog (String title, String message) {

        alertDialog.setMessage(message);
        progressDialog.dismiss();
        alertDialog.setTitle(title);
        alertDialog.show();

    }

}
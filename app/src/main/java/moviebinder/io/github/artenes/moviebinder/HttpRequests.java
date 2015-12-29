package moviebinder.io.github.artenes.moviebinder;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artenes on 29/12/2015.
 */
public class HttpRequests {

    private final String BASE_URL = "http://www.omdbapi.com/?";
    private final String SEARCH_PARAM = "s";
    private final String ID_PARAM = "i";
    private final String QUERY_TYPE = "type";
    private final String TYPE = "movie";
    private final String NA = "N/A";
    private final String POSTER_FOLDER = "movie_binder_posters";

    public final static String SEARCH_KEY = "Search";

    public final static String TAG = HttpRequests.class.getSimpleName();


    public String searchMovies (String title) throws MalformedURLException {

        Uri buildUri =
                Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SEARCH_PARAM, title)
                        .appendQueryParameter(QUERY_TYPE, TYPE)
                        .build();

        URL url = new URL(buildUri.toString());

        return executeRequest(url);

    }

    public String fetchMovie (String imdbID) throws MalformedURLException {

        Uri buildUri =
                Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(ID_PARAM, imdbID)
                        .appendQueryParameter(QUERY_TYPE, TYPE)
                        .build();

        URL url = new URL(buildUri.toString());

        return executeRequest(url);

    }

    private String executeRequest (URL url) {

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            return buffer.toString();

        } catch (IOException error) {

            Log.e(TAG, error.getMessage());
            return null;

        } finally {

            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try{
                    reader.close();
                }  catch (final IOException error) {
                    Log.e(TAG, error.getMessage());
                }
            }

        }

    }

    public String downloadAndSavePoster (String posterUrl) {

        if (posterUrl.equals(NA)) {
            return NA;
        }

        String[] urlParts = posterUrl.split("/");
        String fileName = POSTER_FOLDER + "/" + urlParts[urlParts.length - 1];

        FileOutputStream fileOutput = null;
        InputStream inputStream = null;

        try {

            URL url = new URL(posterUrl);
            Log.v(TAG, "Poster URL: " + posterUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            File sdCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();

            File posterFolder = new File(sdCardRoot, POSTER_FOLDER);
            if (!posterFolder.isDirectory()) {
                posterFolder.mkdir();
            }

            File file = new File(sdCardRoot, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            fileOutput = new FileOutputStream(file);
            inputStream = urlConnection.getInputStream();

            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.v(TAG, "Progress - downloaded Size:" + downloadedSize + "totalSize:" + totalSize);
            }

            if (downloadedSize == totalSize)
                return file.getPath();

        } catch (Exception error) {

            Log.e(TAG, error.getMessage());

        } finally {

            if (fileOutput != null) {
                try {
                    fileOutput.close();
                } catch (IOException error) {
                    Log.e(TAG, error.getMessage());
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException error) {
                    Log.e(TAG, error.getMessage());
                }
            }


        }

        return NA;

    }

    public List<Movie> parseSearch (JSONArray jsonMovies) throws JSONException {

        List<Movie> movies = new ArrayList<Movie>();
        int moviesCount = jsonMovies.length();

        for (int index = 0; index < moviesCount; index++) {

            Movie movie = new Movie();
            JSONObject jsonMovie = jsonMovies.getJSONObject(index);

            movie.title = jsonMovie.getString(DBContract.Movies.TITLE);
            movie.year = jsonMovie.getString(DBContract.Movies.YEAR);
            movie.imdbID = jsonMovie.getString(DBContract.Movies.IMDBID);
            movie.poster = jsonMovie.getString(DBContract.Movies.POSTER);

            movies.add(movie);

        }

        return movies;

    }

    public Movie parseMovie (JSONObject jsonMovie) throws JSONException {

        Movie movie = new Movie();

        movie.title = jsonMovie.getString(DBContract.Movies.TITLE);
        movie.year = jsonMovie.getString(DBContract.Movies.YEAR);
        movie.rated = jsonMovie.getString(DBContract.Movies.RATED);
        movie.released= jsonMovie.getString(DBContract.Movies.RELEASED);
        movie.runtime = jsonMovie.getString(DBContract.Movies.RUNTIME);
        movie.genre = jsonMovie.getString(DBContract.Movies.GENRE);
        movie.director = jsonMovie.getString(DBContract.Movies.DIRECTOR);
        movie.writer = jsonMovie.getString(DBContract.Movies.WRITER);
        movie.actors = jsonMovie.getString(DBContract.Movies.ACTORS);
        movie.plot = jsonMovie.getString(DBContract.Movies.PLOT);
        movie.language = jsonMovie.getString(DBContract.Movies.LANGUAGE);
        movie.country = jsonMovie.getString(DBContract.Movies.COUNTRY);
        movie.awards = jsonMovie.getString(DBContract.Movies.AWARDS);
        movie.poster = jsonMovie.getString(DBContract.Movies.POSTER);
        movie.metascore = jsonMovie.getString(DBContract.Movies.METASCORE);
        movie.imdbRating = jsonMovie.getString(DBContract.Movies.IMDBRATING);
        movie.imdbVotes = jsonMovie.getString(DBContract.Movies.IMDBVOTES);
        movie.imdbID = jsonMovie.getString(DBContract.Movies.IMDBID);

        return movie;

    }

}

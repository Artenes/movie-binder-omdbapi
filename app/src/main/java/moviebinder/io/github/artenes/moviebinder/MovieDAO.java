package moviebinder.io.github.artenes.moviebinder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artenes on 29/12/2015.
 */
public class MovieDAO {

    private SQLiteDatabase connection;

    public MovieDAO (SQLiteDatabase connection){
        this.connection = connection;
    }

    public boolean insertMovie (Movie movie) throws Exception {

        ContentValues values = new ContentValues();

        values.put(DBContract.Movies.TITLE, movie.title);
        values.put(DBContract.Movies.YEAR, movie.year);
        values.put(DBContract.Movies.RATED, movie.rated);
        values.put(DBContract.Movies.RELEASED, movie.released);
        values.put(DBContract.Movies.RUNTIME, movie.runtime);
        values.put(DBContract.Movies.GENRE, movie.genre);
        values.put(DBContract.Movies.DIRECTOR, movie.director);
        values.put(DBContract.Movies.WRITER, movie.writer);
        values.put(DBContract.Movies.ACTORS, movie.actors);
        values.put(DBContract.Movies.PLOT, movie.plot);
        values.put(DBContract.Movies.LANGUAGE, movie.language);
        values.put(DBContract.Movies.COUNTRY, movie.country);
        values.put(DBContract.Movies.AWARDS, movie.awards);
        values.put(DBContract.Movies.POSTER, movie.poster);
        values.put(DBContract.Movies.METASCORE, movie.metascore);
        values.put(DBContract.Movies.IMDBRATING, movie.imdbRating);
        values.put(DBContract.Movies.IMDBVOTES, movie.imdbVotes);
        values.put(DBContract.Movies.IMDBID, movie.imdbID);

        return connection.insert(DBContract.Movies.TABLE_NAME, null, values) > 0;

    }

    public List<Movie> getAllMovies () throws Exception {

        ArrayList<Movie> movies = new ArrayList<>();

        Cursor results = connection.query(DBContract.Movies.TABLE_NAME, null, null, null, null, null, DBContract.Movies._ID + " DESC");
        while (results.moveToNext()) {
            Movie movie = new Movie();
            movie._id = results.getString(results.getColumnIndex(DBContract.Movies._ID));
            movie.title = results.getString(results.getColumnIndex(DBContract.Movies.TITLE));
            movie.year = results.getString(results.getColumnIndex(DBContract.Movies.YEAR));
            movie.poster = results.getString(results.getColumnIndex(DBContract.Movies.POSTER));
            movies.add(movie);
        }

        return movies;

    }

    public boolean doesMovieExistsInDB (String imdbId) {

        Cursor result = connection.query(
                DBContract.Movies.TABLE_NAME,
                null,
                DBContract.Movies.IMDBID + " = ?",
                new String[] {imdbId}, null, null, null, "1");

        return result.moveToFirst();

    }

    public Movie getMovieById (String id) {

        String selection = DBContract.Movies._ID + " = ?";
        String[] selectionArgs = new String[]{id};

        return getMovie(selection, selectionArgs);
    }

    private Movie getMovie (String selection, String[] selectionArgs) {

        Movie movie = new Movie();

        Cursor result = connection.query(DBContract.Movies.TABLE_NAME, null, selection, selectionArgs, null, null, null, "1");

        if (result.moveToFirst()) {
            movie._id = result.getString(result.getColumnIndex(DBContract.Movies._ID));
            movie.title = result.getString(result.getColumnIndex(DBContract.Movies.TITLE));
            movie.year = result.getString(result.getColumnIndex(DBContract.Movies.YEAR));
            movie.rated = result.getString(result.getColumnIndex(DBContract.Movies.RATED));
            movie.released = result.getString(result.getColumnIndex(DBContract.Movies.RELEASED));
            movie.runtime = result.getString(result.getColumnIndex(DBContract.Movies.RUNTIME));
            movie.genre = result.getString(result.getColumnIndex(DBContract.Movies.GENRE));
            movie.director = result.getString(result.getColumnIndex(DBContract.Movies.DIRECTOR));
            movie.writer = result.getString(result.getColumnIndex(DBContract.Movies.WRITER));
            movie.actors = result.getString(result.getColumnIndex(DBContract.Movies.ACTORS));
            movie.plot = result.getString(result.getColumnIndex(DBContract.Movies.PLOT));
            movie.language = result.getString(result.getColumnIndex(DBContract.Movies.LANGUAGE));
            movie.country = result.getString(result.getColumnIndex(DBContract.Movies.COUNTRY));
            movie.awards = result.getString(result.getColumnIndex(DBContract.Movies.AWARDS));
            movie.poster = result.getString(result.getColumnIndex(DBContract.Movies.POSTER));
            movie.metascore = result.getString(result.getColumnIndex(DBContract.Movies.METASCORE));
            movie.imdbRating = result.getString(result.getColumnIndex(DBContract.Movies.IMDBRATING));
            movie.imdbVotes = result.getString(result.getColumnIndex(DBContract.Movies.IMDBVOTES));
            movie.imdbID = result.getString(result.getColumnIndex(DBContract.Movies.IMDBID));
        } else {
            return null;
        }

        return movie;

    }

}
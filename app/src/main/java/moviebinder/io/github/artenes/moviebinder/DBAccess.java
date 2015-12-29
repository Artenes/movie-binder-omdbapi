package moviebinder.io.github.artenes.moviebinder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Artenes on 28/12/2015.
 */
public class DBAccess extends SQLiteOpenHelper {

    private static final String SQL_CREATE_MOVIES =
            "CREATE TABLE " + DBContract.Movies.TABLE_NAME + " (" +
                    DBContract.Movies._ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBContract.Movies.TITLE      + " TEXT NOT NULL, " +
                    DBContract.Movies.YEAR       + " TEXT NOT NULL, " +
                    DBContract.Movies.RATED      + " TEXT NOT NULL, " +
                    DBContract.Movies.RELEASED   + " TEXT NOY NULL, " +
                    DBContract.Movies.RUNTIME    + " TEXT NOT NULL, " +
                    DBContract.Movies.GENRE      + " TEXT NOT NULL, " +
                    DBContract.Movies.DIRECTOR   + " TEXT NOT NULL, " +
                    DBContract.Movies.WRITER     + " TEXT NOT NULL, " +
                    DBContract.Movies.ACTORS     + " TEXT NOT NULL, " +
                    DBContract.Movies.PLOT       + " TEXT NOT NULL, " +
                    DBContract.Movies.LANGUAGE   + " TEXT NOT NULL, " +
                    DBContract.Movies.COUNTRY    + " TEXT NOT NULL, " +
                    DBContract.Movies.AWARDS     + " TEXT NOT NULL, " +
                    DBContract.Movies.POSTER     + " TEXT NOT NULL, " +
                    DBContract.Movies.METASCORE  + " TEXT NOT NULL, " +
                    DBContract.Movies.IMDBRATING + " TEXT NOT NULL, " +
                    DBContract.Movies.IMDBVOTES  + " TEXT NOT NULL, " +
                    DBContract.Movies.IMDBID     + " TEXT NOT NULL UNIQUE " +
                    " )";


    public DBAccess (Context context) {
        super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

}
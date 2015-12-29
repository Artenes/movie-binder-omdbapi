package moviebinder.io.github.artenes.moviebinder;

import android.provider.BaseColumns;

/**
 * Created by Artenes on 28/12/2015.
 */
public class DBContract {

    public static final String DB_NAME = "MOVIEBINDER.db";
    public static final int DB_VERSION = 1;

    public DBContract(){}

    public static abstract class Movies implements BaseColumns {
        public static final String TABLE_NAME = "Movies";
        public static final String TITLE      = "Title";
        public static final String YEAR       = "Year";
        public static final String RATED      = "Rated";
        public static final String RELEASED   = "Released";
        public static final String RUNTIME    = "Runtime";
        public static final String GENRE      = "Genre";
        public static final String DIRECTOR   = "Director";
        public static final String WRITER     = "Writer";
        public static final String ACTORS     = "Actors";
        public static final String PLOT       = "Plot";
        public static final String LANGUAGE   = "Language";
        public static final String COUNTRY    = "Country";
        public static final String AWARDS     = "Awards";
        public static final String POSTER     = "Poster";
        public static final String METASCORE  = "Metascore";
        public static final String IMDBRATING = "imdbRating";
        public static final String IMDBVOTES  = "imdbVotes";
        public static final String IMDBID     = "imdbID";
    }

}

package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ForecastHelper extends SQLiteOpenHelper {
    /*
     * Table Movie and column information
     */
    public static final String TABLE_Movie = "Movie";
    public static  String COLUMN_ID = "_ID";
    public static final String COLUMN_Movie_name = "name";
    public static final String COLUMN_poster_Url = "poster_url";
    public static final String COLUMN_Releasedate = "release_date";
    public static final String COLUMN_vote_average = "vote_average";
    public static final String COLUMN_overview = "overview";
    public static  String COLUMN_favorate = "favorate";
    /*
     * Database information
     */
    private static final String DB_NAME = "TheMovieApp.db";
    private static final int DB_VERSION = 1; // Must increment to trigger an upgrade
    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_Movie + " (" +
                    COLUMN_ID + " VARCHAR, " +
                    COLUMN_Movie_name   + " VARCHAR, " +
                    COLUMN_poster_Url   + " VARCHAR, " +
                    COLUMN_Releasedate  + " VARCHAR, " +
                    COLUMN_vote_average + " VARCHAR, " +
                    COLUMN_overview     + " VARCHAR, " +
                    COLUMN_favorate     + " VARCHAR)";



//    private static final String DB_ALTER =
//            "ALTER TABLE " + TABLE_TEMPERATURES + " ADD COLUMN " + COLUMN_TIME + " INTEGER";

    public ForecastHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    /*
     * This is triggered by incrementing DB_VERSION
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(DB_ALTER);
    }
}

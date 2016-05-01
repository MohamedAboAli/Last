package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ForecastDataSource {

    private SQLiteDatabase mDatabase;       // The actual DB!
    private ForecastHelper mForecastHelper; // Helper class for creating and opening the DB
    private Context mContext;

    public ForecastDataSource(Context context) {
        mContext = context;
        mForecastHelper = new ForecastHelper(mContext);
    }

    /*
     * Open the db. Will create if it doesn't exist
     */
    public void open() throws SQLException {
        mDatabase = mForecastHelper.getWritableDatabase();
    }

    /*
     * We always need to close our db connections
     */
    public void close() {
        mDatabase.close();
    }

    /*
     * CRUD operations!
     */

    /*
     * INSERT
     */
    public void insertForecast(String[] values) {
        String [] paramters={   ForecastHelper.COLUMN_Movie_name,
                                ForecastHelper.COLUMN_poster_Url,
                                ForecastHelper.COLUMN_Releasedate,
                                ForecastHelper.COLUMN_vote_average,
                                ForecastHelper.COLUMN_overview,
                                ForecastHelper.COLUMN_ID,
                                ForecastHelper.COLUMN_favorate};
        mDatabase.beginTransaction();
        try {
            ContentValues values2 = new ContentValues();
            for (int i =0;i<values.length;i++) {
                values2.put(paramters[i], values[i]);
            }
            mDatabase.insert(ForecastHelper.TABLE_Movie, null, values2);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();
        }
    }
    /*
     * SELECT ID For Exist
     */
    public Boolean CheckExist(String id) {
        Cursor cursor = mDatabase.query(
                ForecastHelper.TABLE_Movie, // table
                new String[] { ForecastHelper.COLUMN_ID}, // column names
                ForecastHelper.COLUMN_ID+"=?", // where clause
                new String[]{id}, // where params
                null, // groupby
                null, // having
                null  // orderby
        );
        if (cursor.moveToFirst()){
            cursor.close();
            return true;
        }
        return false;
    }
    /*
   * SELECT ID For Exist
   */
    public String CheckFavorate(String id) {
        Cursor cursor = mDatabase.query(
                ForecastHelper.TABLE_Movie, // table
                new String[] { ForecastHelper.COLUMN_favorate}, // column names
                ForecastHelper.COLUMN_ID+"=?", // where clause
                new String[]{id}, // where params
                null, // groupby
                null, // having
                null  // orderby
        );
        if (cursor.moveToFirst()){
            String data= cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_favorate));
            cursor.close();
            return data;
        }
        return null;
    }
    /*
     * SELECT ALL
     */
    public JSONObject SelectAllViewedMovie(String type) throws JSONException {
        Cursor cursor = mDatabase.query(
                ForecastHelper.TABLE_Movie, // table
                new String[] {  ForecastHelper.COLUMN_ID,
                                ForecastHelper.COLUMN_Movie_name,
                                ForecastHelper.COLUMN_overview,
                                ForecastHelper.COLUMN_poster_Url,
                                ForecastHelper.COLUMN_Releasedate,
                                ForecastHelper.COLUMN_vote_average}, // column names
                type+"=?", // where clause
                new String[]{"true"}, // where params
                null, // groupby
                null, // having
                null  // orderby
        );


        JSONObject outerobject=new JSONObject();
        JSONArray moviesArray =new JSONArray();
        if (cursor.moveToFirst()){
            do{
                JSONObject Movieobject=new JSONObject();
                //Log.v("data....", cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_ID)));
                Movieobject.put("id", cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_ID)));
                Movieobject.put("original_title", cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_Movie_name)));
                Movieobject.put("poster_path",cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_poster_Url)));
                Movieobject.put("overview",cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_overview)));
                Movieobject.put("release_date",cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_Releasedate)));
                Movieobject.put("vote_average",cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_vote_average)));
                moviesArray.put(Movieobject);
        }while(cursor.moveToNext());
            outerobject.putOpt("results", moviesArray);
            cursor.close();
            return outerobject;
        }

        return null;
    }

    /*
     * UPDATE
     */
    public int updateFavorate(String id,String value) {
        ContentValues values = new ContentValues();
        values.put(ForecastHelper.COLUMN_favorate, value);
        int rowsUpdated = mDatabase.update(
                ForecastHelper.TABLE_Movie, // table
                values, // values
                ForecastHelper.COLUMN_ID+"=?",   // where clause
                new String[]{id}// where params
        );
        Log.v("xxxxxxx", rowsUpdated+"");
        return rowsUpdated;
    }

}

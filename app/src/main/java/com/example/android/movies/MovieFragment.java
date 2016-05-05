package com.example.android.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import db.ForecastDataSource;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {
   public static boolean mTwopane=false;


    movieData gsonobject1= new movieData();
    private MovieAdapter myAdapter;
    GridView gridview;
    public MovieFragment() {
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridview = (GridView) rootView.findViewById(R.id.gridView_movies);
        Button popularButton = (Button) rootView.findViewById(R.id.popularButton);
        Button top_ratedButton = (Button) rootView.findViewById(R.id.ptoprateButton);
        Button favorateButton = (Button) rootView.findViewById(R.id.favorateList);


        popularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchMovieTask movieTask = new FetchMovieTask();
                movieTask.execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=17624c6e029d0d1f8473a63cb6135c03");
            }
        });
        top_ratedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchMovieTask bb = new FetchMovieTask();
                bb.execute("http://api.themoviedb.org/3/discover/movie?sort_by=highest_rated.desc&api_key=17624c6e029d0d1f8473a63cb6135c03");
            }
        });

        favorateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForecastDataSource database=new ForecastDataSource(getActivity());
                database.open();
                try {
                    JSONObject MoviesData= database.SelectAllViewedMovie("favorate");
                    if(MoviesData !=null){
                        Gson parser1 = new Gson();
                        gsonobject1 = parser1.fromJson(MoviesData.toString(), movieData.class);
                        myAdapter = new MovieAdapter(getActivity(), gsonobject1.getResults());
                        gridview.setAdapter(myAdapter);
                    }else{
                        Toast.makeText(getActivity(), "There `re Not Movies Favorate", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                database.close();
            }
        });
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=17624c6e029d0d1f8473a63cb6135c03");



        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mTwopane == true) { //Tablet

                    Bundle args = new Bundle();
                    args.putString("movieID", gsonobject1.SpecificMovieInfo(position).getId());
                    args.putString("movieTitle", gsonobject1.SpecificMovieInfo(position).getOriginal_title());
                    args.putString("moviePoster_path", gsonobject1.SpecificMovieInfo(position).getPoster_path());
                    args.putString("movieOverview", gsonobject1.SpecificMovieInfo(position).getOverview());
                    args.putString("movierate", gsonobject1.SpecificMovieInfo(position).getVote_average());
                    args.putString("movieRelease_data", gsonobject1.SpecificMovieInfo(position).getRelease_date());
                    MovieDetailFragment obj = new MovieDetailFragment();
                    obj.setArguments(args);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragmentDetail, obj)
                            .commit();
                }
                //phone "mTwopane==false"
                else {

                    Intent intent = new Intent(getActivity(), MovieDetail.class);
                    intent.putExtra("movieID", gsonobject1.SpecificMovieInfo(position).getId());
                    intent.putExtra("movieTitle", gsonobject1.SpecificMovieInfo(position).getOriginal_title());
                    intent.putExtra("moviePoster_path", gsonobject1.SpecificMovieInfo(position).getPoster_path());
                    intent.putExtra("movieOverview", gsonobject1.SpecificMovieInfo(position).getOverview());
                    intent.putExtra("movierate", gsonobject1.SpecificMovieInfo(position).getVote_average());
                    intent.putExtra("movieRelease_data", gsonobject1.SpecificMovieInfo(position).getRelease_date());
                    startActivity(intent);
                }//end else


            }
        });






        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String MoviesgsonStr = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at

                URL url = new URL(params[0]);
                // Create the request to OpenmoviesMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                MoviesgsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("MainActivityFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MainActivityFragment", "Error closing stream", e);
                    }
                }
            }

            return MoviesgsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!= null){
                Gson parser1 = new Gson();
                gsonobject1 = parser1.fromJson(s, movieData.class);
                myAdapter = new MovieAdapter(getActivity(), gsonobject1.getResults());
                gridview.setAdapter(myAdapter);
            }
            super.onPostExecute(s);
        }
    }


}
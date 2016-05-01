package com.example.android.movies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import db.ForecastDataSource;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    List<String> TrialsKeys;
    List<String> authors;
    List<String> contents;
    ListView List_trail_reviews;

    ForecastDataSource  database;
    String [] MovieInfo;
    String Is_Facorate="false";
    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("aaaaaaaasssss","asssssssssssssd");
        Toast.makeText(getActivity(),"aaaaaa",Toast.LENGTH_LONG).show();
        View view=  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        TextView title= (TextView) view.findViewById(R.id.OriginalTitle);
        ImageView poster= (ImageView) view.findViewById(R.id.poster);
        TextView Overview= (TextView) view.findViewById(R.id.Overview);
        RatingBar rate= (RatingBar) view.findViewById(R.id.ratingBar);
        TextView ReleaseDate= (TextView) view.findViewById(R.id.ReleaseDate);
        final ImageView favorateButton =(ImageView)view.findViewById(R.id.favorateButton);
        List_trail_reviews= (ListView) view.findViewById(R.id.List_trail_reviews);




        Bundle bundle =getArguments();
        if(bundle !=null ){
            Log.v("zzzzzzzzzaasasa",bundle.toString());
            MovieInfo=new String[7];
            MovieInfo[0]=bundle.getString("movieTitle");
            MovieInfo[1]=bundle.getString("moviePoster_path");
            MovieInfo[2]=bundle.getString("movieRelease_data");
            MovieInfo[3]=bundle.getString("movierate");
            MovieInfo[4]=bundle.getString("movieOverview");
            MovieInfo[5]=bundle.getString("movieID");
            MovieInfo[6] = Is_Facorate;
          final  String MovieID= bundle.getString("movieID");
            title.setText(bundle.getString("movieTitle"));
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w185" +bundle.getString("moviePoster_path")).into(poster);
            Overview.setText(bundle.getString("movieOverview"));
            rate.setRating(Float.parseFloat(bundle.getString("movierate")) / 2);
            ReleaseDate.setText(bundle.getString("movieRelease_data"));

            new FetchMovieTrails().execute("http://api.themoviedb.org/3/movie/" + MovieID + "/videos?api_key=17624c6e029d0d1f8473a63cb6135c03");
            new FetchMovieReviews().execute("http://api.themoviedb.org/3/movie/" + MovieID + "/reviews?api_key=17624c6e029d0d1f8473a63cb6135c03");

            database=new ForecastDataSource(getActivity());
            database.open();
            if(!database.CheckExist(bundle.getString("movieID"))){
                database.insertForecast(MovieInfo);
            }
            Is_Facorate = database.CheckFavorate(bundle.getString("movieID"));
            if(Is_Facorate.equals("true")){
                favorateButton.setImageResource(R.drawable.red_heart);
            }else{
                favorateButton.setImageResource(R.drawable.black_heart);
            }
            favorateButton .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Is_Facorate == "true") {
                        database.updateFavorate(MovieInfo[5], "false");
                        favorateButton.setImageResource(R.drawable.black_heart);
                        Is_Facorate = "false";
                    } else {
                        database.updateFavorate(MovieID, "true");
                        favorateButton.setImageResource(R.drawable.red_heart);
                        Is_Facorate = "true";
                    }
                }
            });

        }
        Bundle intent =getActivity().getIntent().getExtras();
        if(intent != null){

            final String MovieID= intent.getString("movieID");
            title.setText(intent.getString("movieTitle"));
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w185" +intent.getString("moviePoster_path" )).into(poster);
            Overview.setText(intent.getString("movieOverview"));
            rate.setRating(Float.parseFloat(intent.getString("movierate")) / 2);
            ReleaseDate.setText(intent.getString("movieRelease_data"));
            MovieInfo=new String[7];
            MovieInfo[0]=intent.getString("movieTitle");
            MovieInfo[1]=intent.getString("moviePoster_path");
            MovieInfo[2]=intent.getString("movieRelease_data");
            MovieInfo[3]=intent.getString("movierate");
            MovieInfo[4]=intent.getString("movieOverview");
            MovieInfo[5]=intent.getString("movieID");
            MovieInfo[6] = Is_Facorate;



            new FetchMovieTrails().execute("http://api.themoviedb.org/3/movie/" + MovieID + "/videos?api_key=17624c6e029d0d1f8473a63cb6135c03");
            new FetchMovieReviews().execute("http://api.themoviedb.org/3/movie/" + MovieID + "/reviews?api_key=17624c6e029d0d1f8473a63cb6135c03");

            database=new ForecastDataSource(getActivity());
            database.open();
            if(!database.CheckExist(intent.getString("movieID"))){
                database.insertForecast(MovieInfo);
            }
            Is_Facorate = database.CheckFavorate(intent.getString("movieID"));
            if(Is_Facorate.equals("true")){
                favorateButton.setImageResource(R.drawable.red_heart);
            }else{
                favorateButton.setImageResource(R.drawable.black_heart);
            }
            favorateButton .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Is_Facorate== "true") {
                        database.updateFavorate(MovieInfo[5], "false");
                        favorateButton.setImageResource(R.drawable.black_heart);
                        Is_Facorate = "false";
                    } else {
                        Log.v("fgfgfgfgfgfg",MovieID);
                        database.updateFavorate(MovieID, "true");
                        favorateButton.setImageResource(R.drawable.red_heart);
                        Is_Facorate = "true";
                    }
                }
            });
        }


        return view;


    }
    public class FetchMovieTrails extends AsyncTask<String, Void, List<String>> {

        private final String LOG_TAG = FetchMovieTrails.class.getSimpleName();

        public List<String> getMovieTrialsKeys(String Obj) throws JSONException {

            JSONObject TrialData=new JSONObject(Obj);
            JSONArray TrialDataArray= TrialData.getJSONArray("results");
            int len=TrialDataArray.length();
            List<String> Keys=new ArrayList<String>();
            for (int i = 0; i <len ; i++) {
               JSONObject TrialObj= (JSONObject) TrialDataArray.get(i);
                Keys.add(TrialObj.getString("key"));
            }
        return Keys;

        }

        @Override
        protected List<String> doInBackground(String... params) {
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

            try {
                return getMovieTrialsKeys(MoviesgsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> s) {
            if(s!= null){
                TrialsKeys=s;


            }
            super.onPostExecute(s);
        }
    }
    public class FetchMovieReviews extends AsyncTask<String, Void, List<String>> {

        private final String LOG_TAG = FetchMovieReviews.class.getSimpleName();

        public List<String> getMovieReviews(String obj) throws JSONException {
            JSONObject TrialData=new JSONObject(obj);
            JSONArray TrialDataArray= TrialData.getJSONArray("results");
            int len=TrialDataArray.length();
           authors=new ArrayList<String>();
            contents=new ArrayList<String>();
            for (int i = 0; i <len ; i++) {
                JSONObject TrialObj= (JSONObject) TrialDataArray.get(i);
                authors.add(TrialObj.getString("author"));
                contents.add(TrialObj.getString("content"));
            }
            return authors;
        }
        @Override
        protected List<String> doInBackground(String... params) {
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

            try {
                return getMovieReviews(MoviesgsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> s) {
            if(s!= null){
                 List_trail_reviews.setAdapter(new TrailReviewAdapters(getActivity(),TrialsKeys,authors,contents));
            }else{

            }

            super.onPostExecute(s);
        }
    }


}

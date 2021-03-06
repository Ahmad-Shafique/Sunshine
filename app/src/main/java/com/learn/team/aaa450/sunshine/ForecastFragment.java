package com.learn.team.aaa450.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ahmed on 8/22/2016.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

/*
    public void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
*/

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle SavedInstanceState) {

        ArrayList<String> fakedata = new ArrayList(6);
        fakedata.add("Today - Sunny - 88/63");
        fakedata.add("Tommorow - Foggy - 70/46");
        fakedata.add("Weds - Cloudy - 72/63");
        fakedata.add("Thurs - Rainy - 64/51");
        fakedata.add("Fry - Foggy - 70/46");
        fakedata.add("Sat - Sunny - 76/68");

        ArrayAdapter<String> fragmentAdapterOne = new ArrayAdapter(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, fakedata);



        //THIS
        //IS
        //THE PARAMETER
        //THAT CAUSED THE
        //ERROR
        View rootView = layoutInflater.inflate(R.layout.fragment_one, container,false);
        //THE SOLUTIO
        //WAS
        //TO
        //ADD THE PARAMETER "FALSE"
        //AT THE END



        //ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(fragmentAdapterOne);

        //return layoutInflater.inflate(R.layout.fragment_one,container,false);************************************************
        return rootView;
    }


    public class FetchWeatherTask extends AsyncTask<Void, Void, Void> {

            private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

            @Override
            protected Void doInBackground(Void... params) {
                // These two need to be declared outside the try/catch
                // so that they can be closed in the finally block.
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                // Will contain the raw JSON response as a string.
                String forecastJsonStr = null;

                try {
                    // Construct the URL for the OpenWeatherMap query
                    // Possible parameters are avaiable at OWM's forecast API page, at
                    // http://openweathermap.org/API#forecast
                    String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7";
                    String apiKey = "&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                    URL url = new URL(baseUrl.concat(apiKey));

                    // Create the request to OpenWeatherMap, and open the connection
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
                    forecastJsonStr = buffer.toString();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
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
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }
                return null;
            }
        }
    }


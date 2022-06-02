package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeatherAct extends AppCompatActivity {
    TextView show_forecast;
    private TextView city_name;
    private TextView dateTimeDisplay;

    class GetWeather extends AsyncTask<String,Void,String>
    {
        protected String doInBackground(String... urls){
            StringBuilder result=new StringBuilder();
            try {
                //Вот весь этот блок мне вообще непонятен, как оно взаимодействует,хуй знает.
                //Похоже весь этот блок читает json в бэкграунде и присваивает его переменной result.
                URL url=new URL(urls[0]);
                HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream =urlConnection.getInputStream();
                BufferedReader reader =new BufferedReader(new InputStreamReader(inputStream));
                String line="";
                while ((line=reader.readLine())!=null)
                {
                    result.append(line).append("\n");
                } return result.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String output = "";
            try {
                JSONObject json = new JSONObject(result);
                JSONArray weather_array_block= json.getJSONArray("weather");
                JSONObject weather_array_dict_0=weather_array_block.getJSONObject(0);
                String description=weather_array_dict_0.getString("description");
                JSONObject main_dict=json.getJSONObject("main");
                JSONObject wind_dict=json.getJSONObject("wind");
                double current_temperature=main_dict.getDouble("temp");
                int current_humidity=main_dict.getInt("humidity");
                double current_wind_speed=wind_dict.getDouble("speed");

                output+="Clouds:"+description+
                        "\nCurrent temperature:" + current_temperature +" Celcium"+
                        "\nCurrent wind speed:" + current_wind_speed +"m/s"+
                        "\nCurrent humidity:" + current_humidity +" %";


                show_forecast.setText(output);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        Date current_time=Calendar.getInstance().getTime();
        String date_from_calendar = DateFormat.getDateInstance(DateFormat.FULL).format(current_time.getTime());
        dateTimeDisplay = findViewById(R.id.current_date);
        city_name=findViewById(R.id.city_name);
        show_forecast=findViewById(R.id.forecast);

        GetWeather get_weather=new GetWeather();

        Intent start_this_activity = getIntent();
        String my_api_call = start_this_activity.getStringExtra("call");
        String my_city_name=start_this_activity.getStringExtra("name");
        city_name.setText(my_city_name);
        dateTimeDisplay.setText(date_from_calendar);

        get_weather.execute(my_api_call);
    }
}
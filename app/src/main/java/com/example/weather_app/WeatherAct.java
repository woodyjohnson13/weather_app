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
    TextView main_weather;
    private TextView city_name;
    private TextView main_humidity;
    private TextView main_wind_speed;
    private TextView main_clouds;
    private TextView air_pressure;



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
                String line;
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
            String output="";
            String main_temperature="";
            String humidity="";
            String wind="";
            String pressure="";


            try {
                JSONObject json = new JSONObject(result);
                JSONArray weather_array_block= json.getJSONArray("weather");
                JSONObject weather_array_dict_0=weather_array_block.getJSONObject(0);
                String description=weather_array_dict_0.getString("description");
                JSONObject main_dict=json.getJSONObject("main");
                JSONObject wind_dict=json.getJSONObject("wind");
                double current_temperature=main_dict.getDouble("temp");
                int current_temp_int=(int)current_temperature;
                int current_humidity=main_dict.getInt("humidity");
                double current_wind_speed=wind_dict.getDouble("speed");
                main_temperature+=current_temp_int + "°C";
                wind+=current_wind_speed + " m/s";
                humidity+=current_humidity + " mm";
                output+="Clouds:"+description+
                        "\nCurrent temperature:" + current_temperature +" Celcium"+
                        "\nCurrent wind speed:" + current_wind_speed +"m/s"+
                        "\nCurrent humidity:" + current_humidity +" %";

                main_humidity.setText(humidity);
                main_wind_speed.setText(wind);
                main_weather.setText(main_temperature);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_weather_layout);

        Date current_time=Calendar.getInstance().getTime();
        String date_from_calendar = DateFormat.getDateInstance(DateFormat.FULL).format(current_time.getTime());
        city_name=findViewById(R.id.main_city_name);
        main_weather=findViewById(R.id.main_temp);
        main_humidity=findViewById(R.id.humidity_level);
        main_wind_speed=findViewById(R.id.wind_speed);
        main_clouds=findViewById(R.id.main_clouds);

        GetWeather get_weather=new GetWeather();


        Intent back_to_city_list=new Intent(WeatherAct.this,MainActivity.class);

        Intent start_this_activity = getIntent();
        String my_api_call = start_this_activity.getStringExtra("call");
        String my_city_name=start_this_activity.getStringExtra("name");

        city_name.setText(my_city_name);

        get_weather.execute(my_api_call);


    }
}
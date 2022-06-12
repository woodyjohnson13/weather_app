package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeatherAct extends AppCompatActivity {
    TextView main_weather;
    private TextView city_name;
    private TextView main_humidity;
    private TextView main_wind_speed;
    private TextView main_clouds;
    private TextView main_air_pressure;
    private TextView main_date;
    private Button back_to_cites;
    private Button refresh_weather;


    //Cleared shared preferense ot avoid looping back to weather activity,method for "back to cities"
    //button
    public void clear_preferences() {
        SharedPreferences prefs;
        prefs = getSharedPreferences("SHARED_PREFS", this.MODE_PRIVATE);
        prefs.getBoolean("choice", false);
        prefs.edit().putBoolean("choice", false).apply();
    }


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
            String main_temperature="";
            String main_hudity="";

            try {
                //get whole object
                JSONObject json = new JSONObject(result);
                //get list with ourly forecast
                JSONArray weather_array_block= json.getJSONArray("list");
                //get first element
                JSONObject weather_array_dict_0=weather_array_block.getJSONObject(0);
                //get date
                String time=weather_array_dict_0.getString("dt_txt");
                Date sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                String main_time=new SimpleDateFormat("HH:mm").format(sdf);
                //getting temperature,wind and humidity
                JSONObject temp_dict=weather_array_dict_0.getJSONObject("main");
                double current_temperature=temp_dict.getDouble("temp");
                int integer_temp=(int)current_temperature;
                JSONObject wind_dict=weather_array_dict_0.getJSONObject("wind");

                main_temperature+=integer_temp + "°C";


                main_date.setText(main_time);
                main_air_pressure.setText(temp_dict.getDouble("pressure")+"mm");
                main_wind_speed.setText(wind_dict.getDouble("speed")+"m/s");
                main_weather.setText(main_temperature);
                main_humidity.setText((temp_dict.getDouble("humidity"))+"%");
            } catch (JSONException | ParseException e) {
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
        main_date=findViewById(R.id.main_date);
        main_air_pressure=findViewById(R.id.air_pressure);
        back_to_cites=findViewById(R.id.back_to_cities);
        refresh_weather=findViewById(R.id.refresh_button);



        GetWeather get_weather=new GetWeather();

        Intent back_to_city_list=new Intent(WeatherAct.this,MainActivity.class);

        Intent start_this_activity = getIntent();
        String my_api_call = start_this_activity.getStringExtra("call");
        String my_city_name=start_this_activity.getStringExtra("name");

        back_to_cites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_preferences();
                startActivity(back_to_city_list);
            }
        });

        refresh_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_weather.execute(my_api_call);
            }
        });

        city_name.setText(my_city_name);

        get_weather.execute(my_api_call);


    }
}
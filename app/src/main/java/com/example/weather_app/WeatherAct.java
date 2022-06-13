package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WeatherAct extends AppCompatActivity {
    //main weather textviews
    private TextView main_weather;
    private TextView city_name;
    private TextView main_humidity;
    private TextView main_wind_speed;
    private TextView main_clouds;
    private TextView main_air_pressure;
    private TextView main_date;
    //refresh and back to cities vuttons
    private Button back_to_cites;
    private Button refresh_weather;
    ///hourly weather and time textviews
    private TextView hourly_time_1;
    private TextView hourly_time_2;
    private TextView hourly_time_3;
    private TextView hourly_time_4;
    private TextView hourly_time_5;
    private TextView hourly_time_6;
    private TextView hourly_time_7;
    private TextView hourly_time_8;
    private TextView hourly_time_9;
    private TextView hourly_weather_1;
    private TextView hourly_weather_2;
    private TextView hourly_weather_3;
    private TextView hourly_weather_4;
    private TextView hourly_weather_5;
    private TextView hourly_weather_6;
    private TextView hourly_weather_7;
    private TextView hourly_weather_8;
    private TextView hourly_weather_9;
    SimpleDateFormat full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat day = new SimpleDateFormat("dd");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm");
    SimpleDateFormat day_of_the_week = new SimpleDateFormat("EEEE");

    RequestQueue mRequestQueue;

    private static final String testUrl = ""; //url, из которого мы будем брать JSON-объект



    //Cleared shared preferense ot avoid looping back to weather activity,method for "back to cities"
    //button
    public void clear_preferences() {
        SharedPreferences prefs;
        prefs = getSharedPreferences("SHARED_PREFS", this.MODE_PRIVATE);
        prefs.getBoolean("choice", false);
        prefs.edit().putBoolean("choice", false).apply();
    }


    private void getWeather(String url) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                url, null, new Response.Listener<JSONObject>() {
            Calendar cal = Calendar.getInstance();
            String dayOfWeekString = day_of_the_week.format(cal.getTime());
            String date_day = day.format((cal.getTime()));
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject weather = response.getJSONObject("main");
                    JSONObject wind = response.getJSONObject("wind");
                    main_weather.setText((int)weather.getDouble("temp")+" °C");
                    main_air_pressure.setText(weather.getInt("pressure")+" mm");
                    main_humidity.setText(weather.getInt("humidity")+" %");
                    main_wind_speed.setText(wind.getDouble("speed")+" m/s");
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
                    Date currentDate = calendar.getTime();
                    main_date.setText(dayOfWeekString+" "+date_day);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { // в случае возникновеня ошибки
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    private void getWeather_hourly_first_day(String url) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                url, null, new Response.Listener<JSONObject>() {
            List<Integer> hourly_temp_list = new ArrayList<>();
            List<String> hourly_time = new ArrayList<>();
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int i=0;

                    JSONArray days_array = response.getJSONArray("list");
                    for(i=0;i<=8;i++){
                        JSONObject object_0=days_array.getJSONObject(i);
                        JSONObject main_0=object_0.getJSONObject("main");
                        hourly_temp_list.add((int)main_0.getDouble("temp"));
                    }


                    for(i=0;i<=8;i++){
                        JSONObject day_object=days_array.getJSONObject(i);
                        Date time_date= full.parse(day_object.getString("dt_txt"));
                        hourly_time.add(time.format(time_date));
                    }

                    hourly_time_1.setText(hourly_time.get(0));
                    hourly_time_2.setText(hourly_time.get(1));
                    hourly_time_3.setText(hourly_time.get(2));
                    hourly_time_4.setText(hourly_time.get(3));
                    hourly_time_5.setText(hourly_time.get(4));
                    hourly_time_6.setText(hourly_time.get(5));
                    hourly_time_7.setText(hourly_time.get(6));
                    hourly_time_8.setText(hourly_time.get(7));
                    hourly_time_9.setText(hourly_time.get(8));

                    hourly_weather_1.setText(hourly_temp_list.get(0) +"°C");
                    hourly_weather_2.setText(hourly_temp_list.get(1) +"°C");
                    hourly_weather_3.setText(hourly_temp_list.get(2) +"°C");
                    hourly_weather_4.setText(hourly_temp_list.get(3) +"°C");
                    hourly_weather_5.setText(hourly_temp_list.get(4) +"°C");
                    hourly_weather_6.setText(hourly_temp_list.get(5) +"°C");
                    hourly_weather_7.setText(hourly_temp_list.get(6) +"°C");
                    hourly_weather_8.setText(hourly_temp_list.get(7) +"°C");
                    hourly_weather_9.setText(hourly_temp_list.get(8) +"°C");
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { // в случае возникновеня ошибки
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

//    class GetWeather extends AsyncTask<String,Void,String>
//    {
//        protected String doInBackground(String... urls){
//            StringBuilder result=new StringBuilder();
//            try {
//                //Вот весь этот блок мне вообще непонятен, как оно взаимодействует,хуй знает.
//                //Похоже весь этот блок читает json в бэкграунде и присваивает его переменной result.
//                URL url=new URL(urls[0]);
//                HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
//                urlConnection.connect();
//                InputStream inputStream =urlConnection.getInputStream();
//                BufferedReader reader =new BufferedReader(new InputStreamReader(inputStream));
//                String line;
//                while ((line=reader.readLine())!=null)
//                {
//                    result.append(line).append("\n");
//                } return result.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            try {
//                //get whole object
//                JSONObject json = new JSONObject(result);
//                //get list with ourly forecast
//                JSONArray weather_array_block= json.getJSONArray("list");
//                //get first element
//                JSONObject weather_array_dict_0=weather_array_block.getJSONObject(0);
//                //get date
//                String time=weather_array_dict_0.getString("dt_txt");
//                Date sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
//                String main_time=new SimpleDateFormat("HH:mm").format(sdf);
//                //getting temperature,wind and humidity
//                JSONObject temp_dict=weather_array_dict_0.getJSONObject("main");
//                double current_temperature=temp_dict.getDouble("temp");
//                int integer_temp=(int)current_temperature;
//                JSONObject wind_dict=weather_array_dict_0.getJSONObject("wind");
//
//                main_date.setText(main_time);
//                main_air_pressure.setText(temp_dict.getDouble("pressure")+"mm");
//                main_wind_speed.setText(wind_dict.getDouble("speed")+"m/s");
//                main_weather.setText(integer_temp+"°C");
//                main_humidity.setText((temp_dict.getDouble("humidity"))+"%");
//            } catch (JSONException | ParseException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_weather_layout);

        //main weather textviews info connecting
        city_name=findViewById(R.id.main_city_name);
        main_weather=findViewById(R.id.main_temp);
        main_humidity=findViewById(R.id.humidity_level);
        main_wind_speed=findViewById(R.id.wind_speed);
        main_clouds=findViewById(R.id.main_clouds);
        main_date=findViewById(R.id.main_date);
        main_air_pressure=findViewById(R.id.air_pressure);
        //hourly textviews connecting
        hourly_weather_1=findViewById(R.id.hourly_weather_1);
        hourly_weather_2=findViewById(R.id.hourly_weather_2);
        hourly_weather_3=findViewById(R.id.hourly_weather_3);
        hourly_weather_4=findViewById(R.id.hourly_weather_4);
        hourly_weather_5=findViewById(R.id.hourly_weather_5);
        hourly_weather_6=findViewById(R.id.hourly_weather_6);
        hourly_weather_7=findViewById(R.id.hourly_weather_7);
        hourly_weather_8=findViewById(R.id.hourly_weather_8);
        hourly_weather_9=findViewById(R.id.hourly_weather_9);
        hourly_time_1=findViewById(R.id.hourly_time_1);
        hourly_time_2=findViewById(R.id.hourly_time_2);
        hourly_time_3=findViewById(R.id.hourly_time_3);
        hourly_time_4=findViewById(R.id.hourly_time_4);
        hourly_time_5=findViewById(R.id.hourly_time_5);
        hourly_time_6=findViewById(R.id.hourly_time_6);
        hourly_time_7=findViewById(R.id.hourly_time_7);
        hourly_time_8=findViewById(R.id.hourly_time_8);
        hourly_time_9=findViewById(R.id.hourly_time_9);

        //butons connecting
        back_to_cites=findViewById(R.id.back_to_cities);
        refresh_weather=findViewById(R.id.refresh_button);



        //GetWeather get_weather=new GetWeather();

        Intent back_to_city_list=new Intent(WeatherAct.this,MainActivity.class);

        Intent start_this_activity = getIntent();
        String my_api_call_hourly = start_this_activity.getStringExtra("call_current_hourly");
        String my_api_call_current = start_this_activity.getStringExtra("current_weather_call_extra");
        String my_city_name=start_this_activity.getStringExtra("name");
        String testUrl = my_api_call_current;

        mRequestQueue= Volley.newRequestQueue(this);


        //back to cities list vutton,works
        back_to_cites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_preferences();
                startActivity(back_to_city_list);
            }
        });


        //refresh weather button, still doesnt work
//        refresh_weather.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                get_weather.execute(my_api_call);
//            }
//        });


        getWeather(my_api_call_current);
        city_name.setText(my_city_name);
        getWeather_hourly_first_day(my_api_call_hourly);




    }
}
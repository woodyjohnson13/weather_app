package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WeatherAct extends AppCompatActivity {
    //main weather textview
    private TextView main_weather;
    private TextView city_name;
    private TextView main_humidity;
    private TextView main_wind_speed;
    private TextView main_clouds;
    private TextView main_air_pressure;
    private TextView main_date;
    //refresh and back to cities buttons
    private Button back_to_cites;
    private Button refresh_weather;
    ///hourly weather and time textview
    private TextView hourly_time_0;
    private TextView hourly_time_1;
    private TextView hourly_time_2;
    private TextView hourly_time_3;
    private TextView hourly_time_4;
    private TextView hourly_time_5;
    private TextView hourly_time_6;
    private TextView hourly_time_7;
    private TextView hourly_time_8;
    private TextView hourly_weather_0;
    private TextView hourly_weather_1;
    private TextView hourly_weather_2;
    private TextView hourly_weather_3;
    private TextView hourly_weather_4;
    private TextView hourly_weather_5;
    private TextView hourly_weather_6;
    private TextView hourly_weather_7;
    private TextView hourly_weather_8;
    private TextView weekly_weather_max_0;
    private TextView weekly_weather_max_1;
    private TextView weekly_weather_max_2;
    private TextView weekly_weather_max_3;
    private TextView weekly_weather_max_4;
    private TextView weekly_weather_min_0;
    private TextView weekly_weather_min_1;
    private TextView weekly_weather_min_2;
    private TextView weekly_weather_min_3;
    private TextView weekly_weather_min_4;
    private TextView day_of_the_week_0;
    private TextView day_of_the_week_1;
    private TextView day_of_the_week_2;
    private TextView day_of_the_week_3;
    private TextView day_of_the_week_4;
    public int certain_day_trigger;



    SimpleDateFormat full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat date_without_time = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat day = new SimpleDateFormat("dd");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm");
    SimpleDateFormat day_of_the_week = new SimpleDateFormat("EEEE");

    //request queue object for volley
    RequestQueue mRequestQueue;



    //Cleared shared preferense ot avoid looping back to weather activity,method for "back to cities"
    //button
    public void clear_preferences() {
        SharedPreferences prefs;
        prefs = getSharedPreferences("SHARED_PREFS", this.MODE_PRIVATE);
        prefs.getBoolean("choice", false);
        prefs.edit().putBoolean("choice", false).apply();
    }

    //assign Json data to main,top weather varibales
    private void getWeather(String url) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                url, null, new Response.Listener<JSONObject>() {
            final Calendar cal = Calendar.getInstance();
            final String dayOfWeekString = day_of_the_week.format(cal.getTime());
            final String date_day = day.format((cal.getTime()));
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main = response.getJSONObject("main");
                    JSONObject wind = response.getJSONObject("wind");
                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject object_for_clouds = weather.getJSONObject(0);
                    String clouds_lower=object_for_clouds.getString("description");
                    main_clouds.setText(clouds_lower.substring(0,1).toUpperCase()+clouds_lower.substring(1));
                    main_weather.setText((int)main.getDouble("temp")+" °C");
                    main_air_pressure.setText(main.getInt("pressure")+" mm");
                    main_humidity.setText(main.getInt("humidity")+" %");
                    main_wind_speed.setText(wind.getDouble("speed")+" m/s");
                    city_name.setText(response.getString("name"));
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
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

    //assign Json data to hourly weather and weekly weather
    private void getWeather_hourly_and_weekly(String url) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                url, null, new Response.Listener<JSONObject>() {
            final List<Integer> hourly_temperature_list = new ArrayList<>();
            final List<String> hourly_time_list = new ArrayList<>();
            final List<TextView> hourly_time_textview_list = new ArrayList<>();
            final List<TextView> hourly_temperature_textview_list = new ArrayList<>();

            @Override
            public void onResponse(JSONObject response) {
                try {
                    int i;
                    JSONArray days_array = response.getJSONArray("list");
                    for(i=0;i<=8;i++){
                        JSONObject object_0=days_array.getJSONObject(i);
                        JSONObject main_0=object_0.getJSONObject("main");
                        hourly_temperature_list.add((int)main_0.getDouble("temp"));
                    }


                    for(i=0;i<=8;i++){
                        JSONObject day_object=days_array.getJSONObject(i);
                        Date time_date= full.parse(day_object.getString("dt_txt"));
                        hourly_time_list.add(time.format(time_date));
                    }

                    Collections.addAll(hourly_time_textview_list,hourly_time_0,hourly_time_1,hourly_time_2,
                            hourly_time_3,hourly_time_4,hourly_time_5,hourly_time_6,hourly_time_7,
                            hourly_time_8);

                    Collections.addAll(hourly_temperature_textview_list,hourly_weather_0,hourly_weather_1,
                            hourly_weather_2,hourly_weather_3,hourly_weather_4,hourly_weather_5,
                            hourly_weather_6,hourly_weather_7,hourly_weather_8);

                    for (i=0; i< hourly_time_textview_list.toArray().length; i++){
                        hourly_time_textview_list.get(i).setText(hourly_time_list.get(i));
                        hourly_temperature_textview_list.get(i).setText(hourly_temperature_list.get(i)+"°C");
                    }


                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                try {
                    List<Double> daily_max = new ArrayList<>();
                    List<Double> daily_min = new ArrayList<>();
                    List<Integer> max_day_temperatures = new ArrayList<>();
                    List<Integer> min_day_temperatures = new ArrayList<>();
                    List<String> days_of_the_week = new ArrayList<>();

                    JSONArray days_array = response.getJSONArray("list");
                    JSONObject first_object=days_array.getJSONObject(0);
                    String  date_1=first_object.getString("dt_txt");
                    Date full_date=full.parse(date_1);
                    String first_day_in_forecast_final=day_of_the_week.format(full_date);
                    days_of_the_week.add(first_day_in_forecast_final);
                    String reference_date=date_without_time.format(full_date);

                    int z;
                    for(z=0;z<=39;z++){
                        JSONObject current_object=days_array.getJSONObject(z);
                        String date_2=current_object.getString("dt_txt");
                        JSONObject main=current_object.getJSONObject("main");
                        Date full_date_to_check=full.parse(date_2);
                        String need_to_chek_date=date_without_time.format(full_date_to_check);
                        if(need_to_chek_date.equals(reference_date)) {
                            daily_max.add(main.getDouble("temp_max"));
                            daily_min.add(main.getDouble("temp_min"));
                        } else if (!need_to_chek_date.equals(reference_date)){

                            String day_of_the_week_json=current_object.getString("dt_txt");
                            Date day_of_the_week_full=full.parse(day_of_the_week_json);
                            String day_of_the_week_final=day_of_the_week.format(day_of_the_week_full);
                            days_of_the_week.add(day_of_the_week_final);


                            reference_date=need_to_chek_date;

                            double sum_max = 0;
                            double sum_min = 0;
                            for (int x=0;x<daily_max.size();x++){
                                sum_max+=daily_max.get(x);
                                sum_min+=daily_min.get(x);
                            }

                            max_day_temperatures.add((int)(sum_max/daily_max.size()));
                            min_day_temperatures.add((int)(sum_min/daily_min.size()));
                            daily_max.clear();
                            daily_min.clear();
                            daily_max.add(main.getDouble("temp_max"));
                            daily_min.add(main.getDouble("temp_min"));


                        }

                    }
                    weekly_weather_max_0.setText(max_day_temperatures.get(0).toString()+"°C");
                    weekly_weather_max_1.setText(max_day_temperatures.get(1).toString()+"°C");
                    weekly_weather_max_2.setText(max_day_temperatures.get(2).toString()+"°C");
                    weekly_weather_max_3.setText(max_day_temperatures.get(3).toString()+"°C");
                    weekly_weather_max_4.setText(max_day_temperatures.get(4).toString()+"°C");
                    weekly_weather_min_0.setText(min_day_temperatures.get(0).toString()+"°C");
                    weekly_weather_min_1.setText(min_day_temperatures.get(1).toString()+"°C");
                    weekly_weather_min_2.setText(min_day_temperatures.get(2).toString()+"°C");
                    weekly_weather_min_3.setText(min_day_temperatures.get(3).toString()+"°C");
                    weekly_weather_min_4.setText(min_day_temperatures.get(4).toString()+"°C");
                    day_of_the_week_0.setText(days_of_the_week.get(0));
                    day_of_the_week_1.setText(days_of_the_week.get(1));
                    day_of_the_week_2.setText(days_of_the_week.get(2));
                    day_of_the_week_3.setText(days_of_the_week.get(3));
                    day_of_the_week_4.setText(days_of_the_week.get(4));
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

    private void getWeather_on_certain_day(String url) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    List<Double> daily_max = new ArrayList<>();
                    List<Double> daily_min = new ArrayList<>();
                    List<Integer> max_day_temperatures = new ArrayList<>();
                    List<Integer> min_day_temperatures = new ArrayList<>();
                    List<String> days_of_the_week = new ArrayList<>();

                    JSONArray days_array = response.getJSONArray("list");
                    JSONObject city=response.getJSONObject("city");
                    JSONObject first_object=days_array.getJSONObject(0);
                    String  date_1=first_object.getString("dt_txt");
                    Date full_date=full.parse(date_1);
                    String first_day_in_forecast_final=day_of_the_week.format(full_date);
                    days_of_the_week.add(first_day_in_forecast_final);
                    String reference_date=date_without_time.format(full_date);

                    int z;
                    for(z=0;z<=39;z++){
                        JSONObject current_object=days_array.getJSONObject(z);
                        String date_2=current_object.getString("dt_txt");
                        JSONObject main=current_object.getJSONObject("main");
                        Date full_date_to_check=full.parse(date_2);
                        String need_to_chek_date=date_without_time.format(full_date_to_check);
                        if(need_to_chek_date.equals(reference_date)) {
                            daily_max.add(main.getDouble("temp_max"));
                            daily_min.add(main.getDouble("temp_min"));
                        } else if (!need_to_chek_date.equals(reference_date)){

                            String day_of_the_week_json=current_object.getString("dt_txt");
                            Date day_of_the_week_full=full.parse(day_of_the_week_json);
                            String day_of_the_week_final=day_of_the_week.format(day_of_the_week_full);
                            days_of_the_week.add(day_of_the_week_final);


                            reference_date=need_to_chek_date;

                            double sum_max = 0;
                            double sum_min = 0;
                            for (int x=0;x<daily_max.size();x++){
                                sum_max+=daily_max.get(x);
                            }
                            for (int x=0;x<daily_min.size();x++){
                                sum_min+=daily_min.get(x);
                            }
                            max_day_temperatures.add((int)(sum_max/daily_max.size()));
                            min_day_temperatures.add((int)(sum_min/daily_min.size()));
                            daily_max.clear();
                            daily_min.clear();
                            daily_max.add(main.getDouble("temp_max"));
                            daily_min.add(main.getDouble("temp_min"));


                        }

                    }
                    city_name.setText(city.getString("name"));
                    // assingmetn statement, dont like it at all, wont be good for bigger
                    //data input,will try to change later
                    if (certain_day_trigger==0) {
                        main_weather.setText(max_day_temperatures.get(0).toString());
                    } else if (certain_day_trigger==1){
                        main_weather.setText(max_day_temperatures.get(1).toString());
                    }else if(certain_day_trigger==2){
                        main_weather.setText(max_day_temperatures.get(2).toString());
                    }else if(certain_day_trigger==3){
                        main_weather.setText(max_day_temperatures.get(3).toString());
                    }else if(certain_day_trigger==4){
                        main_weather.setText(max_day_temperatures.get(4).toString());
                    }
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
        hourly_weather_0=findViewById(R.id.hourly_weather_1);
        hourly_weather_1=findViewById(R.id.hourly_weather_2);
        hourly_weather_2=findViewById(R.id.hourly_weather_3);
        hourly_weather_3=findViewById(R.id.hourly_weather_4);
        hourly_weather_4=findViewById(R.id.hourly_weather_5);
        hourly_weather_5=findViewById(R.id.hourly_weather_6);
        hourly_weather_6=findViewById(R.id.hourly_weather_7);
        hourly_weather_7=findViewById(R.id.hourly_weather_8);
        hourly_weather_8=findViewById(R.id.hourly_weather_9);
        hourly_time_0=findViewById(R.id.hourly_time_1);
        hourly_time_1=findViewById(R.id.hourly_time_2);
        hourly_time_2=findViewById(R.id.hourly_time_3);
        hourly_time_3=findViewById(R.id.hourly_time_4);
        hourly_time_4=findViewById(R.id.hourly_time_5);
        hourly_time_5=findViewById(R.id.hourly_time_6);
        hourly_time_6=findViewById(R.id.hourly_time_7);
        hourly_time_7=findViewById(R.id.hourly_time_8);
        hourly_time_8=findViewById(R.id.hourly_time_9);
        //weekly max/min temp connecting
        weekly_weather_max_0 =findViewById(R.id.weekly_weahter_max_0);
        weekly_weather_max_1 =findViewById(R.id.weekly_weahter_max_1);
        weekly_weather_max_2=findViewById(R.id.weekly_weahter_max_2);
        weekly_weather_max_3 =findViewById(R.id.weekly_weahter_max_3);
        weekly_weather_max_4 =findViewById(R.id.weekly_weahter_max_4);
        weekly_weather_min_0 =findViewById(R.id.weekly_weahter_min_0);
        weekly_weather_min_1 =findViewById(R.id.weekly_weahter_min_1);
        weekly_weather_min_2 =findViewById(R.id.weekly_weahter_min_2);
        weekly_weather_min_3 =findViewById(R.id.weekly_weahter_min_3);
        weekly_weather_min_4 =findViewById(R.id.weekly_weahter_min_4);
        //days of the week connecting
        day_of_the_week_0=findViewById(R.id.day_of_the_week_0);
        day_of_the_week_1=findViewById(R.id.day_of_the_week_1);
        day_of_the_week_2=findViewById(R.id.day_of_the_week_2);
        day_of_the_week_3=findViewById(R.id.day_of_the_week_3);
        day_of_the_week_4=findViewById(R.id.day_of_the_week_4);
        //butons connecting
        back_to_cites=findViewById(R.id.back_to_cities);
        refresh_weather=findViewById(R.id.refresh_button);


        //Intents and extra data from previous activity
        Intent back_to_city_list=new Intent(WeatherAct.this,MainActivity.class);
        Intent start_this_activity = getIntent();
        String my_api_call_hourly = start_this_activity.getStringExtra("call_current_hourly");
        String my_api_call_current = start_this_activity.getStringExtra("current_weather_call_extra");


        mRequestQueue= Volley.newRequestQueue(this);


        //back to cities list button
        back_to_cites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_preferences();
                startActivity(back_to_city_list);
            }
        });


        //refresh weather button
        refresh_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeather(my_api_call_current);
                getWeather_hourly_and_weekly(my_api_call_hourly);
                Toast.makeText(WeatherAct.this,"Weather refreshed",Toast.LENGTH_LONG).show();
            }
        });

        View.OnClickListener certain_day_weather_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.day_of_the_week_0:
                        certain_day_trigger = 0;;
                        break;
                    case R.id.day_of_the_week_1:
                        certain_day_trigger = 1;
                        break;
                    case R.id.day_of_the_week_2:
                        certain_day_trigger = 2;;
                        break;
                    case R.id.day_of_the_week_3:
                        certain_day_trigger = 3;

                        break;
                    case R.id.day_of_the_week_4:
                        certain_day_trigger = 4;
                        break;
                    default:
                        break;
                }
                getWeather_on_certain_day(my_api_call_hourly);
            }
        };
        day_of_the_week_0.setOnClickListener(certain_day_weather_listener);
        day_of_the_week_1.setOnClickListener(certain_day_weather_listener);
        day_of_the_week_2.setOnClickListener(certain_day_weather_listener);
        day_of_the_week_3.setOnClickListener(certain_day_weather_listener);
        day_of_the_week_4.setOnClickListener(certain_day_weather_listener);
        getWeather(my_api_call_current);
        getWeather_hourly_and_weekly(my_api_call_hourly);




    }
}
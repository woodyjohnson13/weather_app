package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WeatherAct extends AppCompatActivity {
    //main weather textview and main weather icon
    private TextView main_weather,city_name,country_name,main_humidity,main_wind_speed,main_clouds,
            main_air_pressure,main_date;
    private  ImageView main_weather_icon;
    //refresh and back to cities buttons
    private ImageView back_to_cites;
    private ImageView refresh_weather;
    ///hourly weather and time textview for horizontal scrollview
    private TextView hourly_time_0,hourly_time_1,hourly_time_2,hourly_time_3,hourly_time_4,
            hourly_time_5,hourly_time_6,hourly_time_7,hourly_time_8;
    private TextView hourly_weather_0,hourly_weather_1,hourly_weather_2,hourly_weather_3,
            hourly_weather_4,hourly_weather_5,hourly_weather_6,hourly_weather_7,hourly_weather_8;
    //weekly max and min temperatures for vertica scrollview
    private TextView weekly_weather_max_0,weekly_weather_max_1,weekly_weather_max_2,weekly_weather_max_3,
            weekly_weather_max_4;
    private TextView weekly_weather_min_0,weekly_weather_min_1,weekly_weather_min_2,weekly_weather_min_3,
            weekly_weather_min_4;
    //day of the weeks for vertical scrollview
    private TextView day_of_the_week_0,day_of_the_week_1,day_of_the_week_2,day_of_the_week_3,
            day_of_the_week_4;
    //icons for horizontal scrollview
    private ImageView scroll_weather_icon_0,scroll_weather_icon_1,scroll_weather_icon_2,
            scroll_weather_icon_3,scroll_weather_icon_4,scroll_weather_icon_5,
            scroll_weather_icon_6,scroll_weather_icon_7,scroll_weather_icon_8;
    //icon for main weather on top



    //date formats that will be used
    SimpleDateFormat full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat date_without_time = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat day = new SimpleDateFormat("dd");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm");
    SimpleDateFormat day_of_the_week = new SimpleDateFormat("EEEE");

    //request queue object for volley
    RequestQueue main_request;



    //Cleared shared preference to avoid looping back to weather activity,method for "back to cities"
    //button
    public void clear_preferences() {
        SharedPreferences prefs;
        prefs = getSharedPreferences("SHARED_PREFS", this.MODE_PRIVATE);
        prefs.getBoolean("choice", false);
        prefs.edit().putBoolean("choice", false).apply();
    }

    //assign Json data to main,top weather variables
    private void getWeather(String url) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                url, null, new Response.Listener<JSONObject>() {
            final Calendar cal = Calendar.getInstance();
            final String dayOfWeekString = day_of_the_week.format(cal.getTime());
            final String date_day = day.format((cal.getTime()));
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //getting strings and ints from json response
                    JSONObject main = response.getJSONObject("main");
                    JSONObject wind = response.getJSONObject("wind");
                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject clouds = weather.getJSONObject(0);
                    String clouds_lower=clouds.getString("description");
                    //assign retrieved json data to variables
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

        main_request.add(request);
    }

    //assign Json data to hourly weather and weekly weather
    private void getWeather_hourly_and_weekly(String url) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                url, null, new Response.Listener<JSONObject>() {
            //Lists of data for more comfortable assignment
            final List<Integer> hourly_temperature_list = new ArrayList<>();
            final List<String> hourly_time_list = new ArrayList<>();
            final List<TextView> hourly_time_textview_list = new ArrayList<>();
            final List<TextView> hourly_temperature_textview_list = new ArrayList<>();
            final List<ImageView> scroll_weather_icons_list=new ArrayList<>();

            @Override
            public void onResponse(JSONObject response) {
                //whole try block sets time and temperature values to scroll bar
                try {
                    int i;
                    //adding all daily temperatures to list
                    JSONArray days_array = response.getJSONArray("list");
                    for(i=0;i<=8;i++){
                        JSONObject object_0=days_array.getJSONObject(i);
                        JSONObject main_0=object_0.getJSONObject("main");
                        hourly_temperature_list.add((int)main_0.getDouble("temp"));
                    }

                    //adding time to list
                    for(i=0;i<=8;i++){
                        JSONObject day_object=days_array.getJSONObject(i);
                        Date time_date= full.parse(day_object.getString("dt_txt"));
                        hourly_time_list.add(time.format(time_date));
                    }

                    //adding time textview to created list for more comfortable assignment
                    Collections.addAll(hourly_time_textview_list,hourly_time_0,hourly_time_1,hourly_time_2,
                            hourly_time_3,hourly_time_4,hourly_time_5,hourly_time_6,hourly_time_7,
                            hourly_time_8);

                    //adding temperature textview to created list for more comfortable assignment
                    Collections.addAll(hourly_temperature_textview_list,hourly_weather_0,hourly_weather_1,
                            hourly_weather_2,hourly_weather_3,hourly_weather_4,hourly_weather_5,
                            hourly_weather_6,hourly_weather_7,hourly_weather_8);

                    //weather icons for scroll view collection
                    Collections.addAll(scroll_weather_icons_list,scroll_weather_icon_0,scroll_weather_icon_1,scroll_weather_icon_2,
                            scroll_weather_icon_3,scroll_weather_icon_4,scroll_weather_icon_5,
                            scroll_weather_icon_6,scroll_weather_icon_7,scroll_weather_icon_8);


                    //simultaneously setting text for time and temperature from corresponding lists
                    //to text views from corresponding lists
                    for (i=0; i< hourly_time_textview_list.toArray().length; i++){
                        hourly_time_textview_list.get(i).setText(hourly_time_list.get(i));
                        hourly_temperature_textview_list.get(i).setText(hourly_temperature_list.get(i)+"°C");
                        //getting weather description from json
                        JSONObject main_object=days_array.getJSONObject(i);
                        JSONArray weather_array=main_object.getJSONArray("weather");
                        JSONObject condition_object=weather_array.getJSONObject(0);
                        String description=condition_object.getString("description");

                        //decides which icon to out on hourly weather
                        switch (description) {
                            case "clear sky":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.clear_sky_icon);
                                break;
                            case "few clouds":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.few_cloud_icon);
                                break;
                            case "scattered clouds":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.scatered_clouds_icon);
                                break;
                            case "broken clouds":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.broken_clouds_iocn);
                                break;
                            case "shower rain":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.shower_rain_icon);
                                break;
                            case "rain":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.rain_icon);
                                break;
                            case "thunderstorm":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.thunderstorm_icon);
                                break;
                            case "snow":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.snow_icon);
                                break;
                            case "mist":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.mist_icon);
                                break;
                            case "overcast clouds":
                                scroll_weather_icons_list.get(i).setImageResource(R.drawable.overcast_clouds_icon);
                                break;
                        }


                    }

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                //whole try block sets values for weekly temperature block
                try {
                    List<Double> one_day_max_temperatures = new ArrayList<>();
                    List<Double> one_day_min_temperatures = new ArrayList<>();
                    List<Integer> max_day_temperatures = new ArrayList<>();
                    List<Integer> min_day_temperatures = new ArrayList<>();
                    List<String> days_of_the_week = new ArrayList<>();
                    final List<TextView> weekly_temperature_textview_max_list = new ArrayList<>();
                    final List<TextView> weekly_temperature_textview_min_list = new ArrayList<>();
                    final List<TextView> days_of_the_week_textview_list = new ArrayList<>();

                    Collections.addAll(weekly_temperature_textview_max_list,weekly_weather_max_0,
                            weekly_weather_max_1,weekly_weather_max_2,weekly_weather_max_3,
                            weekly_weather_max_4);
                    Collections.addAll(weekly_temperature_textview_min_list,weekly_weather_min_0,
                            weekly_weather_min_1,weekly_weather_min_2,weekly_weather_min_3,
                            weekly_weather_min_4);
                    Collections.addAll(days_of_the_week_textview_list,day_of_the_week_0,
                            day_of_the_week_1,day_of_the_week_2,day_of_the_week_3,day_of_the_week_4);

                    //Sets current day as fist day in the weekly forecast
                    //creates reference date for looping through json
                    JSONArray days_array = response.getJSONArray("list");
                    JSONObject first_object=days_array.getJSONObject(0);
                    String  date_1=first_object.getString("dt_txt");
                    Date full_date=full.parse(date_1);
                    String first_day_in_forecast_final=day_of_the_week.format(full_date);
                    days_of_the_week.add(first_day_in_forecast_final);
                    String reference_date=date_without_time.format(full_date);

                    int z;
                    for(z=0;z<=39;z++){
                        //gets date from current object of list and compare it to reference date
                        //doing this to get all temperature values on certain data
                        JSONObject current_object=days_array.getJSONObject(z);
                        String date_2=current_object.getString("dt_txt");
                        JSONObject main=current_object.getJSONObject("main");
                        Date full_date_to_check=full.parse(date_2);
                        String need_to_chek_date=date_without_time.format(full_date_to_check);
                        if(need_to_chek_date.equals(reference_date)) {
                            //if current date equals reference date adding min and max temps
                            //to the corresponding list
                            one_day_max_temperatures.add(main.getDouble("temp_max"));
                            one_day_min_temperatures.add(main.getDouble("temp_min"));
                        } else if (!need_to_chek_date.equals(reference_date)){
                            //if current date is different from reference date it means that
                            //new day started, now we need to set new reference date and compare
                            //next dates to it
                            String day_of_the_week_json=current_object.getString("dt_txt");
                            Date day_of_the_week_full=full.parse(day_of_the_week_json);
                            String day_of_the_week_final=day_of_the_week.format(day_of_the_week_full);
                            days_of_the_week.add(day_of_the_week_final);

                            reference_date=need_to_chek_date;
                            //when new day starts calculates median value of max and min
                            //temperatures for previous day and add it to corresponding list
                            //also clears list so we can add values for new day
                            double sum_max = 0;
                            double sum_min = 0;
                            for (int x=0;x<one_day_max_temperatures.size();x++){
                                sum_max+=one_day_max_temperatures.get(x);
                                sum_min+=one_day_min_temperatures.get(x);
                            }

                            max_day_temperatures.add((int)(sum_max/one_day_max_temperatures.size()));
                            min_day_temperatures.add((int)(sum_min/one_day_min_temperatures.size()));
                            one_day_max_temperatures.clear();
                            one_day_min_temperatures.clear();
                            one_day_max_temperatures.add(main.getDouble("temp_max"));
                            one_day_min_temperatures.add(main.getDouble("temp_min"));


                        }

                    }

                    for (z=0;z<weekly_temperature_textview_max_list.toArray().length;z++){
                        weekly_temperature_textview_max_list.get(z).setText(max_day_temperatures.get(z).toString()+"°C");
                        weekly_temperature_textview_min_list.get(z).setText(min_day_temperatures.get(z).toString()+"°C");
                        days_of_the_week_textview_list.get(z).setText(days_of_the_week.get(z));
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

        main_request.add(request);
    }

    //not sure if i need this method,it is pointless on current api,maybe will delete later
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
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        main_request.add(request);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directed_weather_activity);

        //main weather textviews info connecting
        city_name=findViewById(R.id.main_city_name);
        country_name=findViewById(R.id.main_country_name);
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
        day_of_the_week_0=findViewById(R.id.day_of_the_week_1);
        day_of_the_week_1=findViewById(R.id.day_of_the_week_1);
        day_of_the_week_2=findViewById(R.id.weekly_weather_condtition_2);
        day_of_the_week_3=findViewById(R.id.day_of_the_week_3);
        day_of_the_week_4=findViewById(R.id.day_of_the_week_4);
        //buttons connecting
        back_to_cites=findViewById(R.id.back_to_cites);
        refresh_weather=findViewById(R.id.refresh_button);
        //image views connection
        main_weather_icon=findViewById(R.id.main_weather_icon);
        scroll_weather_icon_0=findViewById(R.id.scroll_weather_icon_0);
        scroll_weather_icon_1=findViewById(R.id.scroll_weather_icon_1);
        scroll_weather_icon_2=findViewById(R.id.scroll_weather_icon_2);
        scroll_weather_icon_3=findViewById(R.id.scroll_weather_icon_3);
        scroll_weather_icon_4=findViewById(R.id.scroll_weather_icon_5);
        scroll_weather_icon_5=findViewById(R.id.scroll_weather_icon_5);
        scroll_weather_icon_6=findViewById(R.id.scroll_weather_icon_6);
        scroll_weather_icon_7=findViewById(R.id.scroll_weather_icon_7);
        scroll_weather_icon_8=findViewById(R.id.scroll_weather_icon_8);


        //Intents and api calls data data from previous activity
        Intent back_to_city_list=new Intent(WeatherAct.this,MainActivity.class);
        Intent start_this_activity = getIntent();
        String my_api_call_hourly = start_this_activity.getStringExtra("call_current_hourly");
        String my_api_call_current = start_this_activity.getStringExtra("current_weather_call_extra");

        // volley request
        main_request = Volley.newRequestQueue(this);


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
            }
        });


        getWeather(my_api_call_current);
        getWeather_hourly_and_weekly(my_api_call_hourly);

    }
}
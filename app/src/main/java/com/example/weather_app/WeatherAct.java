package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class WeatherAct extends AppCompatActivity {
    //main weather textview and main weather icon
    private TextView main_weather, city_and_country_name,main_humidity,main_wind_speed, main_condtions,
            main_air_pressure,main_date;
    private  ImageView main_weather_icon;
    //refresh and back to cities buttons
    private ImageView back_to_cites;
    private ImageView refresh_weather;
    ///hourly weather and time textview for horizontal scrollview
    private TextView hourly_time_0,hourly_time_1,hourly_time_2,hourly_time_3,hourly_time_4,
            hourly_time_5,hourly_time_6,hourly_time_7;
    private TextView hourly_weather_0,hourly_weather_1,hourly_weather_2,hourly_weather_3,
            hourly_weather_4,hourly_weather_5,hourly_weather_6,hourly_weather_7;
    //icons for horizontal scrollview
    private ImageView scroll_weather_icon_0,scroll_weather_icon_1,scroll_weather_icon_2,
            scroll_weather_icon_3,scroll_weather_icon_4,scroll_weather_icon_5,
            scroll_weather_icon_6,scroll_weather_icon_7;
    //weekly max and min temperatures for vertica scrollview
    private TextView weekly_weather_max_0,weekly_weather_max_1,weekly_weather_max_2,weekly_weather_max_3,
            weekly_weather_max_4;
    private TextView weekly_weather_min_0,weekly_weather_min_1,weekly_weather_min_2,weekly_weather_min_3,
            weekly_weather_min_4;
    //days of the weeks for vertical scrollview
    private TextView day_of_the_week_0,day_of_the_week_1,day_of_the_week_2,day_of_the_week_3,
            day_of_the_week_4;
    //weekly weather conditions for vertical scrollview
    private TextView weekly_weather_condition_0,weekly_weather_condition_1,weekly_weather_condition_2,
            weekly_weather_condition_3,weekly_weather_condition_4;
    //icons for vertical scrollview
    private ImageView weekly_weather_icon_0,weekly_weather_icon_1,weekly_weather_icon_2,
            weekly_weather_icon_3,weekly_weather_icon_4;



    //date formats that will be used
    SimpleDateFormat full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat date_without_time = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat day = new SimpleDateFormat("dd");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm");
    SimpleDateFormat day_of_the_week = new SimpleDateFormat("EEEE");
    SimpleDateFormat month = new SimpleDateFormat("MMM");
    SimpleDateFormat year = new SimpleDateFormat("yyyy");

    //request queue object for volley
    RequestQueue main_request;



    //Cleared shared preference to avoid looping back to weather activity,method for "back to cities"
    //button
    private void clear_preferences() {
        SharedPreferences prefs;
        prefs = getSharedPreferences("SHARED_PREFS", this.MODE_PRIVATE);
        prefs.getBoolean("choice", false);
        prefs.edit().putBoolean("choice", false).apply();
    }

    //assign Json data to main,top weather variables
    private void getWeather(String url) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            //Calendar instance for getting time and date divided into various string
            //for usage flexibility
            final Calendar cal = Calendar.getInstance();
            final String day_of_the_week_string = day_of_the_week.format(cal.getTime());
            final String day_of_the_month_string = day.format((cal.getTime()));
            final String month_string = month.format((cal.getTime()));
            final String year_string = year.format((cal.getTime()));
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //getting strings and ints from json response
                    JSONObject main = response.getJSONObject("main");
                    JSONObject wind = response.getJSONObject("wind");
                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject clouds = weather.getJSONObject(0);
                    String main_weather_conditions=clouds.getString("main");
                    //assign retrieved json data to variables
                    main_condtions.setText(main_weather_conditions);
                    main_weather.setText((int)main.getDouble("temp")+" °C");
                    main_air_pressure.setText(main.getInt("pressure")+"mm");
                    main_humidity.setText(main.getInt("humidity")+" %");
                    main_wind_speed.setText(wind.getDouble("speed")+"m/s");
                    city_and_country_name.setText(response.getString("name"));
                    main_date.setText(day_of_the_week_string + ","+ month_string + " " + day_of_the_month_string
                    + "," + year_string);

                    //assign weekly weather conditions and weekly weather icon here, because
                    //first element in weekly weather will use conditions and icon for current weather
                    weekly_weather_condition_0.setText(main_weather_conditions);


                    //assign corresponding weather icon to main weather icon
                    switch (main_weather_conditions) {
                        case "Clear":
                            main_weather_icon.setImageResource(R.drawable.clear_sky);
                            weekly_weather_icon_0.setImageResource(R.drawable.clear_sky);
                            break;
                        case "Clouds":
                            main_weather_icon.setImageResource(R.drawable.few_clouds);
                            weekly_weather_icon_0.setImageResource(R.drawable.few_clouds);
                            break;
                        case "Rain":
                            main_weather_icon.setImageResource(R.drawable.shower_rain);
                            weekly_weather_icon_0.setImageResource(R.drawable.shower_rain);
                            break;
                        case "Drizzle":
                            main_weather_icon.setImageResource(R.drawable.rain);
                            weekly_weather_icon_0.setImageResource(R.drawable.rain);
                            break;
                        case "Thunderstorm":
                            main_weather_icon.setImageResource(R.drawable.thunderstorm);
                            weekly_weather_icon_0.setImageResource(R.drawable.thunderstorm);
                            break;
                        case "Snow":
                            main_weather_icon.setImageResource(R.drawable.snow);
                            weekly_weather_icon_0.setImageResource(R.drawable.snow);
                            break;
                        case "Atmosphere":
                            main_weather_icon.setImageResource(R.drawable.mist);
                            weekly_weather_icon_0.setImageResource(R.drawable.mist);
                            break;

                    }

                } catch (JSONException e) {
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

    //assign Json data to hourly weather and weekly weather
    private void getWeather_hourly_and_weekly(String url) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                url, null, new Response.Listener<JSONObject>() {
            //Lists of data for more comfortable assignment
            final List<Integer> hourly_temperature_list = new ArrayList<>();
            final List<String> hourly_time_list = new ArrayList<>();
            final List<TextView> hourly_time_textview_list = new ArrayList<>();
            final List<TextView> hourly_temperature_textview_list = new ArrayList<>();
            final List<ImageView> scroll_weather_imageviews_list =new ArrayList<>();
            final List<ImageView> weekly_weather_imageviews_list=new ArrayList<>();
            final List<TextView> weekly_weather_conditions_list=new ArrayList<>();

            @Override

            public void onResponse(JSONObject response) {
                //whole try block sets time and temperature values to horizontal scroll bar
                try {
                    //adding all daily temperatures to list
                    JSONArray days_array = response.getJSONArray("list");
                    int i;
                    for(i=0;i<8;i++){
                        JSONObject current_object=days_array.getJSONObject(i);
                        Date time_date= full.parse(current_object.getString("dt_txt"));
                        hourly_time_list.add(time.format(time_date));
                        JSONObject current_main_object=current_object.getJSONObject("main");
                        hourly_temperature_list.add((int)current_main_object.getDouble("temp"));
                    }


                    //adding time textview to created list for more comfortable assignment
                    Collections.addAll(hourly_time_textview_list,hourly_time_0,hourly_time_1,hourly_time_2,
                            hourly_time_3,hourly_time_4,hourly_time_5,hourly_time_6,hourly_time_7);

                    //adding temperature textview to created list for more comfortable assignment
                    Collections.addAll(hourly_temperature_textview_list,hourly_weather_0,hourly_weather_1,
                            hourly_weather_2,hourly_weather_3,hourly_weather_4,hourly_weather_5,
                            hourly_weather_6,hourly_weather_7);

                    //weather imageviews for  more comfortable assignment
                    Collections.addAll(scroll_weather_imageviews_list,scroll_weather_icon_0,scroll_weather_icon_1,scroll_weather_icon_2,
                            scroll_weather_icon_3,scroll_weather_icon_4,scroll_weather_icon_5,
                            scroll_weather_icon_6,scroll_weather_icon_7);

                    //weather conditions for vertical layout
                    Collections.addAll(weekly_weather_conditions_list,weekly_weather_condition_1,
                            weekly_weather_condition_2,weekly_weather_condition_3,weekly_weather_condition_4);


                    //simultaneously setting text for time and temperature from corresponding lists
                    //to text views from corresponding lists,also decides which icon is suitable
                    //for current weather conditions
                    for (i=0; i< hourly_time_textview_list.toArray().length; i++){
                        hourly_time_textview_list.get(i).setText(hourly_time_list.get(i));
                        hourly_temperature_textview_list.get(i).setText(hourly_temperature_list.get(i)+"°C");
                        //getting weather description from json
                        JSONObject main_array=days_array.getJSONObject(i);
                        JSONArray weather_array=main_array.getJSONArray("weather");
                        JSONObject condition_object=weather_array.getJSONObject(0);
                        String weather_conditions_for_hourly_forecast=condition_object.getString("main");

                        //assign corresponding weather icon to weekly horizontal scrollview
                        switch (weather_conditions_for_hourly_forecast) {
                            case "Clear":
                                scroll_weather_imageviews_list.get(i).setImageResource(R.drawable.clear_sky);
                                break;
                            case "Clouds":
                                scroll_weather_imageviews_list.get(i).setImageResource(R.drawable.few_clouds);
                                break;
                            case "Rain":
                                scroll_weather_imageviews_list.get(i).setImageResource(R.drawable.shower_rain);
                                break;
                            case "Drizzle":
                                scroll_weather_imageviews_list.get(i).setImageResource(R.drawable.rain);
                                break;
                            case "Thunderstorm":
                                scroll_weather_imageviews_list.get(i).setImageResource(R.drawable.thunderstorm);
                                break;
                            case "Snow` ":
                                scroll_weather_imageviews_list.get(i).setImageResource(R.drawable.snow);
                                break;
                            case "Atmosphere":
                                scroll_weather_imageviews_list.get(i).setImageResource(R.drawable.mist);
                                break;
                        }


                    }

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                //whole try block sets values for weekly temperature vertical scrollview
                try {
                    //array for one day max and min temperatures
                    //average temperature will be calculated using data inside this arrays
                    //and then it will be cleared for next day
                    List<Double> one_day_max_temperatures = new ArrayList<>();
                    List<Double> one_day_min_temperatures = new ArrayList<>();
                    //arrays for average temperatures,that will be calculated from one day arrays
                    List<Integer> max_day_temperatures = new ArrayList<>();
                    List<Integer> min_day_temperatures = new ArrayList<>();
                    //arrays for gathered days of the week names and weather descriptions
                    List<String> days_of_the_week = new ArrayList<>();
                    List<String> weather_description = new ArrayList<>();
                    //arrays for corresponding textviews for vertical scrollview
                    List<TextView> weekly_temperature_textview_max_list = new ArrayList<>();
                    List<TextView> weekly_temperature_textview_min_list = new ArrayList<>();
                    List<TextView> days_of_the_week_textview_list = new ArrayList<>();

                    //adding weekly max temperatures textviews to array for comfortable assingment
                    Collections.addAll(weekly_temperature_textview_max_list,weekly_weather_max_0,
                            weekly_weather_max_1,weekly_weather_max_2,weekly_weather_max_3,
                            weekly_weather_max_4);
                    //adding weekly min temperatures textviews to array for comfortable assingment
                    Collections.addAll(weekly_temperature_textview_min_list,weekly_weather_min_0,
                            weekly_weather_min_1,weekly_weather_min_2,weekly_weather_min_3,
                            weekly_weather_min_4);
                    //adding day of the weeks textviews to array for comfortable assingment
                    Collections.addAll(days_of_the_week_textview_list,day_of_the_week_0,
                            day_of_the_week_1,day_of_the_week_2,day_of_the_week_3,day_of_the_week_4);
                    //adding weekly image views to array for comfortable assingment
                    Collections.addAll(weekly_weather_imageviews_list,weekly_weather_icon_1,
                            weekly_weather_icon_2,weekly_weather_icon_3,weekly_weather_icon_4);

                    //Sets current day as fist day in the weekly forecast
                    //for getting reference dates and first day in the weekly forecast
                    JSONArray days_array = response.getJSONArray("list");
                    JSONObject first_object=days_array.getJSONObject(0);
                    String  first_unformed_date=first_object.getString("dt_txt");
                    Date full_date=full.parse(first_unformed_date);
                    String first_day_in_forecast_final=day_of_the_week.format(full_date);
                    days_of_the_week.add(first_day_in_forecast_final);
                    //reference date will be dynamically changed at the start of each new day
                    //to gather data for one day and wont take data from next day
                    String reference_date=date_without_time.format(full_date);
                    //reference date for week will stay static to avoid getting day name and conditions
                    //for the first day in the forecast,we did gather this info in get weather method
                    //no point to to do it here
                    String reference_date_for_week=date_without_time.format(full_date);

                    //this block will loop through all objects in Json main list
                    //using date as reference,collect data,calculate and assign
                    //to corresponding lists
                    int z;
                    for(z=0;z<=39;z++){
                        //gets date from current object of list and compare it to reference date
                        //doing this to get all temperature values on certain data
                        JSONObject current_object=days_array.getJSONObject(z);
                        JSONArray weather_array=current_object.getJSONArray("weather");
                        //this object iz always "0" in weather array
                        JSONObject zero=weather_array.getJSONObject(0);
                        String current_unformed_date=current_object.getString("dt_txt");
                        JSONObject current_main=current_object.getJSONObject("main");
                        Date full_date_to_check=full.parse(current_unformed_date);
                        //formatted date is dynamic and will be compare to reference date
                        String formatted_date_to_chek=date_without_time.format(full_date_to_check);
                        //time to check will be used to get mid day weather conditions
                        //for each day,excluding first day (condtitions for the first day
                        //was gathered at get weather method
                        String time_to_check=time.format(full_date_to_check);
                        if(formatted_date_to_chek.equals(reference_date)) {
                            //if current date equals reference date adding min and max temps
                            //to the corresponding list
                            one_day_max_temperatures.add(current_main.getDouble("temp_max"));
                            one_day_min_temperatures.add(current_main.getDouble("temp_min"));
                        } else if (!formatted_date_to_chek.equals(reference_date)){
                            //if current date is different from reference date it means that
                            //new day started, now we need to set new reference date and compare
                            //next dates to it
                            String new_day_of_the_week_unformatted=current_object.getString("dt_txt");
                            Date day_of_the_week_full=full.parse(new_day_of_the_week_unformatted);
                            String day_of_the_week_final=day_of_the_week.format(day_of_the_week_full);
                            days_of_the_week.add(day_of_the_week_final);
                            reference_date=formatted_date_to_chek;

                            //when new day starts code calculates average value of max and min
                            //temperatures for previous day and add it to corresponding list
                            //also clears list so we can add values for new day
                            double sum_max = 0;
                            double sum_min = 0;
                            for (int x=0;x<one_day_max_temperatures.size();x++){
                                sum_max+=one_day_max_temperatures.get(x);
                                sum_min+=one_day_min_temperatures.get(x);
                            }
                            max_day_temperatures.add((int)(Math.ceil(sum_max/one_day_max_temperatures.size())));
                            min_day_temperatures.add((int)(Math.floor(sum_min/one_day_min_temperatures.size())));
                            one_day_max_temperatures.clear();
                            one_day_min_temperatures.clear();
                            one_day_max_temperatures.add(current_main.getDouble("temp_max"));
                            one_day_min_temperatures.add(current_main.getDouble("temp_min"));


                        }
                        //this statement gather mid day weather condition information for mid day
                        //except first day using reference day and time decision point
                        if (!formatted_date_to_chek.equals(reference_date_for_week) && time_to_check.equals("12:00")){
                            weather_description.add(zero.getString("main"));
                        }

                    }

                    //assign corresponding weather icon to weekly vertical scrollview
                    for (z=0;z<weekly_weather_conditions_list.toArray().length;z++){
                        weekly_weather_conditions_list.get(z).setText(weather_description.get(z));

                        switch (weather_description.get(z)) {
                            case "Clear":
                                weekly_weather_imageviews_list.get(z).setImageResource(R.drawable.clear_sky);
                                break;
                            case "Clouds":
                                weekly_weather_imageviews_list.get(z).setImageResource(R.drawable.few_clouds);
                                break;
                            case "Rain":
                                weekly_weather_imageviews_list.get(z).setImageResource(R.drawable.shower_rain);
                                break;
                            case "Drizzle":
                                weekly_weather_imageviews_list.get(z).setImageResource(R.drawable.rain);
                                break;
                            case "Thunderstorm":
                                weekly_weather_imageviews_list.get(z).setImageResource(R.drawable.thunderstorm);
                                break;
                            case "Snow":
                                weekly_weather_imageviews_list.get(z).setImageResource(R.drawable.snow);
                                break;
                            case "Atmosphere":
                                weekly_weather_imageviews_list.get(z).setImageResource(R.drawable.mist);
                                break;

                        }


                    }
                    //assign max/min temperature and days of the week values to corresponding views
                    for (z=0;z<weekly_temperature_textview_max_list.toArray().length;z++){
                        weekly_temperature_textview_max_list.get(z).setText(max_day_temperatures.get(z).toString()+"°");
                        weekly_temperature_textview_min_list.get(z).setText(min_day_temperatures.get(z).toString()+"°");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directed_weather_activity);

        //main weather textviews info connecting
        city_and_country_name =findViewById(R.id.main_city_name);
        //country_name=findViewById(R.id.main_country_name);
        main_weather=findViewById(R.id.main_temp);
        main_humidity=findViewById(R.id.humidity_level);
        main_wind_speed=findViewById(R.id.wind_speed);
        main_condtions =findViewById(R.id.main_clouds);
        main_date=findViewById(R.id.main_date);
        main_air_pressure=findViewById(R.id.air_pressure);
        //hourly textviews connecting
        hourly_weather_0=findViewById(R.id.hourly_weather_0);
        hourly_weather_1=findViewById(R.id.hourly_weather_1);
        hourly_weather_2=findViewById(R.id.hourly_weather_2);
        hourly_weather_3=findViewById(R.id.hourly_weather_3);
        hourly_weather_4=findViewById(R.id.hourly_weather_4);
        hourly_weather_5=findViewById(R.id.hourly_weather_5);
        hourly_weather_6=findViewById(R.id.hourly_weather_6);
        hourly_weather_7=findViewById(R.id.hourly_weather_7);
        hourly_time_0=findViewById(R.id.hourly_time_0);
        hourly_time_1=findViewById(R.id.hourly_time_1);
        hourly_time_2=findViewById(R.id.hourly_time_2);
        hourly_time_3=findViewById(R.id.hourly_time_3);
        hourly_time_4=findViewById(R.id.hourly_time_4);
        hourly_time_5=findViewById(R.id.hourly_time_5);
        hourly_time_6=findViewById(R.id.hourly_time_6);
        hourly_time_7=findViewById(R.id.hourly_time_7);
        //weekly max/min temp connecting
        weekly_weather_max_0=findViewById(R.id.weekly_weahter_max_0);
        weekly_weather_max_1=findViewById(R.id.weekly_weahter_max_1);
        weekly_weather_max_2=findViewById(R.id.weekly_weahter_max_2);
        weekly_weather_max_3 =findViewById(R.id.weekly_weahter_max_3);
        weekly_weather_max_4 =findViewById(R.id.weekly_weahter_max_4);
        weekly_weather_min_0 =findViewById(R.id.weekly_weahter_min_0);
        weekly_weather_min_1 =findViewById(R.id.weekly_weahter_min_1);
        weekly_weather_min_2 =findViewById(R.id.weekly_weahter_min_2);
        weekly_weather_min_3 =findViewById(R.id.weekly_weahter_min_3);
        weekly_weather_min_4 =findViewById(R.id.weekly_weahter_min_4);
        //weekly weather conditions for vertical scrollview
        weekly_weather_condition_0=findViewById(R.id.weekly_weather_condition_0);
        weekly_weather_condition_1=findViewById(R.id.weekly_weather_condition_1);
        weekly_weather_condition_2=findViewById(R.id.weekly_weather_condition_2);
        weekly_weather_condition_3=findViewById(R.id.weekly_weather_condition_3);
        weekly_weather_condition_4=findViewById(R.id.weekly_weather_condition_4);
        //weekly weather condition icons for vertical scroll view
        weekly_weather_icon_0=findViewById(R.id.weekly_weather_icon_0);
        weekly_weather_icon_1=findViewById(R.id.weekly_weather_icon_1);
        weekly_weather_icon_2=findViewById(R.id.weekly_weather_icon_2);
        weekly_weather_icon_3=findViewById(R.id.weekly_weather_icon_3);
        weekly_weather_icon_4=findViewById(R.id.weekly_weather_icon_4);
        //days of the week connecting
        day_of_the_week_0=findViewById(R.id.day_of_the_week_0);
        day_of_the_week_1=findViewById(R.id.day_of_the_week_1);
        day_of_the_week_2=findViewById(R.id.day_of_the_week_2);
        day_of_the_week_3=findViewById(R.id.day_of_the_week_3);
        day_of_the_week_4=findViewById(R.id.day_of_the_week_4);
        //buttons connecting
        back_to_cites=findViewById(R.id.back_to_cites);
        refresh_weather=findViewById(R.id.refresh_button);
        //image views connection for main weather and horizontal scrollview
        main_weather_icon=findViewById(R.id.main_weather_icon);
        scroll_weather_icon_0=findViewById(R.id.scroll_weather_icon_0);
        scroll_weather_icon_1=findViewById(R.id.scroll_weather_icon_1);
        scroll_weather_icon_2=findViewById(R.id.scroll_weather_icon_2);
        scroll_weather_icon_3=findViewById(R.id.scroll_weather_icon_3);
        scroll_weather_icon_4=findViewById(R.id.scroll_weather_icon_4);
        scroll_weather_icon_5=findViewById(R.id.scroll_weather_icon_5);
        scroll_weather_icon_6=findViewById(R.id.scroll_weather_icon_6);
        scroll_weather_icon_7=findViewById(R.id.scroll_weather_icon_7);


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

        //main methods call using chosen api
        getWeather(my_api_call_current);
        getWeather_hourly_and_weekly(my_api_call_hourly);

    }
}
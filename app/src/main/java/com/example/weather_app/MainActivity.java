package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button get_info_rnd;
    Button get_info_msc;
    String city_api_call_hourly="";
    String city_current_weather_call="";
    String city_name="";
    EditText search_for_city;
    public Boolean weather_activity_trigger ;



    public void get_pref () {
        SharedPreferences weather_cativity_shared=getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        SharedPreferences.Editor editor = weather_cativity_shared.edit();
        editor.putString("city_current_weather_pref",city_current_weather_call);
        editor.putString("city_hourly_weather_pref",city_api_call_hourly);
        weather_activity_trigger=true;
        editor.putBoolean("choice",weather_activity_trigger);
        editor.apply();
        Toast.makeText(this,"Data saved",Toast.LENGTH_LONG).show();
    }

    public void loadData () {
        SharedPreferences my_shared_prefs=getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        city_current_weather_call=my_shared_prefs.getString("city_current_weather_pref","");
        city_api_call_hourly=my_shared_prefs.getString("city_hourly_weather_pref","");
        city_name=my_shared_prefs.getString("city_name","");
        weather_activity_trigger= my_shared_prefs.getBoolean("choice", false);
        Toast.makeText(this,"Data loaded " + weather_activity_trigger.toString(),Toast.LENGTH_LONG).show();

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_info_rnd=findViewById(R.id.getBtRnd);
        get_info_msc=findViewById(R.id.getBtMoscow);
        search_for_city=findViewById(R.id.search_city);
        Intent go_to_weather=new Intent(MainActivity.this,WeatherAct.class);
        loadData();


        if(weather_activity_trigger) {
            //go_to_weather.putExtra("call",city_api_call);
            go_to_weather.putExtra("name",city_name);
            go_to_weather.putExtra("current_weather_call_extra",city_current_weather_call);
            go_to_weather.putExtra("call_current_hourly",city_api_call_hourly);

            startActivity(go_to_weather);
           }


        View.OnClickListener my_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.getBtRnd:
                        city_api_call_hourly="https://api.openweathermap.org/data/2.5/forecast?q=Rostov-on-Don&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                        city_current_weather_call="http://api.openweathermap.org/data/2.5/weather?q=Rostov-on-Don&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                        get_pref();
                        break;
                    case R.id.getBtMoscow:
                        city_api_call_hourly="https://api.openweathermap.org/data/2.5/forecast?q=Moscow&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                        city_current_weather_call="http://api.openweathermap.org/data/2.5/weather?q=Moscow&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                        get_pref();
                        break;
                    case R.id.search_city:
                        city_api_call_hourly="https://api.openweathermap.org/data/2.5/forecast?q="+search_for_city.getText().toString()+"&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                        city_current_weather_call="http://api.openweathermap.org/data/2.5/weather?q="+search_for_city.getText().toString()+"&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                        get_pref();
                        break;
                    default:
                        break;
                }

                go_to_weather.putExtra("current_weather_call_extra",city_current_weather_call);
                go_to_weather.putExtra("call_current_hourly",city_api_call_hourly);
                startActivity(go_to_weather);


            }
        };

        get_info_rnd.setOnClickListener(my_listener);
        get_info_msc.setOnClickListener(my_listener);
        search_for_city.setOnClickListener(my_listener);



    }

}
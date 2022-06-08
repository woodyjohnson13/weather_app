package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button get_info_rnd;
    Button get_info_msc;
    String check_call ="";
    String city_call="";
    String city_name="";
    public static final String SHARED_PREFS="sharedPrefs";
    public static final String TEXT="text";
    public static final String CITY="text";
    public Boolean my_choice_is_done ;
    TextView fuckit;
    TextView fuckit2;


    public void get_pref () {
        SharedPreferences my_shared_prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = my_shared_prefs.edit();
        editor.putString("city_name",city_name);
        editor.putString("city_api_call",city_call);
        my_choice_is_done=true;
        editor.putBoolean("choice",my_choice_is_done);
        editor.apply();
        Toast.makeText(this,"Data saved",Toast.LENGTH_LONG).show();
    }

    public void loadData () {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        //check_call=sharedPreferences.getString(SWITCH,"GO FOR");
        city_call=sharedPreferences.getString("city_api_call","");
        city_name=sharedPreferences.getString("city_name","");
        my_choice_is_done= sharedPreferences.getBoolean("choice", false);
        fuckit2.setText(city_call);
        fuckit.setText(city_name);
        Toast.makeText(this,"Data loaded",Toast.LENGTH_LONG).show();

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_info_rnd=findViewById(R.id.getBtRnd);
        get_info_msc=findViewById(R.id.getBtMoscow);
        fuckit=findViewById(R.id.fuckyoutextview);
        fuckit2=findViewById(R.id.textView23);
        Intent go_to_weather=new Intent(MainActivity.this,WeatherAct.class);
        loadData();


        if(my_choice_is_done) {
            go_to_weather.putExtra("call",city_call);
            go_to_weather.putExtra("name",city_name);
            startActivity(go_to_weather);
           }

        View.OnClickListener my_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.getBtRnd:
                        city_call="https://api.openweathermap.org/data/2.5/forecast?q=Rostov&appid=32879a100afc9b16435463591d9e99c9";
                        city_name="Rostov";
                        get_pref();
                        break;
                    case R.id.getBtMoscow:
                        city_call="http://api.openweathermap.org/data/2.5/weather?q=Moscow&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                        city_name="Moscow";
                        get_pref();
                        break;
                    default:
                        break;
                }

                go_to_weather.putExtra("call",city_call);
                go_to_weather.putExtra("name",city_name);
                startActivity(go_to_weather);


            }
        };

        get_info_rnd.setOnClickListener(my_listener);
        get_info_msc.setOnClickListener(my_listener);



    }

}
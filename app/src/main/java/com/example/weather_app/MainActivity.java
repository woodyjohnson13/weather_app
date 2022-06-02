package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    Button get_info_rnd;
    Button get_info_msc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_info_rnd=findViewById(R.id.getBtRnd);
        get_info_msc=findViewById(R.id.getBtMoscow);


        View.OnClickListener my_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city_call="";
                String city_name="";
                switch (v.getId()) {
                    case R.id.getBtRnd:
                        city_call="http://api.openweathermap.org/data/2.5/weather?q=Rostov-on-Don&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                        city_name="Rostov";
                        break;
                    case R.id.getBtMoscow:
                        city_call="http://api.openweathermap.org/data/2.5/weather?q=Moscow&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                        city_name="Moscow";
                        break;
                    default:
                        break;
                }

                Intent go_to_weather=new Intent(MainActivity.this,WeatherAct.class);
                go_to_weather.putExtra("call",city_call);
                go_to_weather.putExtra("name",city_name);
                startActivity(go_to_weather);
            }
        };

        get_info_rnd.setOnClickListener(my_listener);
        get_info_msc.setOnClickListener(my_listener);

    }

}
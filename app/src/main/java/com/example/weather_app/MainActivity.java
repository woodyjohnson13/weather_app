package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView get_info_rnd;
    TextView get_info_msc;
    String city_hourly_weather_call ="";
    String city_current_weather_call="";
    EditText search_for_city_et;
    ImageView search_city_button;
    public Boolean weather_activity_trigger ;


    //saving API call in shared preference to access it right in app start
    public void get_pref () {
        SharedPreferences weather_activity_shared=getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        SharedPreferences.Editor editor = weather_activity_shared.edit();
        editor.putString("city_current_weather_pref",city_current_weather_call);
        editor.putString("city_hourly_weather_pref", city_hourly_weather_call);
        //trigger will be used in statement which defines if app need to intent to weather activity using preferred city
        weather_activity_trigger=true;
        editor.putBoolean("choice",weather_activity_trigger);
        editor.apply();
    }

    //loading shared preference on app start
    public void loadData () {
        SharedPreferences my_shared_prefs=getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        city_current_weather_call=my_shared_prefs.getString("city_current_weather_pref","");
        city_hourly_weather_call =my_shared_prefs.getString("city_hourly_weather_pref","");
        weather_activity_trigger= my_shared_prefs.getBoolean("choice", false);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directed_main_activity);
        get_info_rnd=findViewById(R.id.rosotv_find);
        get_info_msc=findViewById(R.id.moscow_find);
        search_for_city_et=findViewById(R.id.search_city);
        search_city_button=findViewById(R.id.search_city_button);
        Intent go_to_weather=new Intent(MainActivity.this,WeatherAct.class);
        //getting shared preference data to define if need to go right to weather activity
        loadData();

        //statement defines if we need to intent to weather activity right on app launch and passes
        //some variables with intent
        if(weather_activity_trigger) {
            go_to_weather.putExtra("current_weather_call_extra",city_current_weather_call);
            go_to_weather.putExtra("call_current_hourly", city_hourly_weather_call);
            startActivity(go_to_weather);
           }


        //main on click listener,api differs for each button
        @SuppressLint("NonConstantResourceId") View.OnClickListener my_listener = v -> {

            switch (v.getId()) {
                case R.id.rosotv_find:
                    city_hourly_weather_call ="https://api.openweathermap.org/data/2.5/forecast?q=Rostov-on-Don&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                    city_current_weather_call="http://api.openweathermap.org/data/2.5/weather?q=Rostov-on-Don&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                    get_pref();
                    break;
                case R.id.moscow_find:
                    city_hourly_weather_call ="https://api.openweathermap.org/data/2.5/forecast?q=Moscow&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                    city_current_weather_call="http://api.openweathermap.org/data/2.5/weather?q=Moscow&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                    get_pref();
                    break;
                case R.id.search_city_button:
                    city_hourly_weather_call ="https://api.openweathermap.org/data/2.5/forecast?q="+ search_for_city_et.getText().toString()+"&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                    city_current_weather_call="http://api.openweathermap.org/data/2.5/weather?q="+ search_for_city_et.getText().toString()+"&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                    get_pref();
                    break;
                default:
                    break;
            }

            //passes extra data to weather activity to make corresponding api call
            go_to_weather.putExtra("current_weather_call_extra",city_current_weather_call);
            go_to_weather.putExtra("call_current_hourly", city_hourly_weather_call);
            startActivity(go_to_weather);


        };

        get_info_rnd.setOnClickListener(my_listener);
        get_info_msc.setOnClickListener(my_listener);
        search_city_button.setOnClickListener(my_listener);



    }

}
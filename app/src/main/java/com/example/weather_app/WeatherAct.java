package com.example.weather_app;
import com.example.weather_app.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WeatherAct extends AppCompatActivity {
    TextView weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        weather=findViewById(R.id.weather_update);
        Intent transfer_intent=getIntent();
        if(transfer_intent.hasExtra(Intent.EXTRA_TEXT)){
            String current_weather= transfer_intent.getStringExtra(Intent.EXTRA_TEXT);
            weather.setText(current_weather);
        } else {
            weather.setText("Не,нихуя");
        }




    }
}
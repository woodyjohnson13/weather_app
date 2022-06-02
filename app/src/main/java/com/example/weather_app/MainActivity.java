package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    Button get_info;
    TextView show;
    TextView showme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_info=findViewById(R.id.getBt);
        show=findViewById(R.id.show_result);
        showme=findViewById(R.id.showing);




        get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent go_to_weather=new Intent(MainActivity.this,WeatherAct.class);
            startActivity(go_to_weather);


            }
        });
    }

}
package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //EditText city_name,country_name;
    Button get_info;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_info=findViewById(R.id.getBt);
        result=findViewById(R.id.show_result);

        class GetWeather extends AsyncTask<String,Void,String>
        {
            protected String doInBackground(String... urls){
                StringBuilder result=new StringBuilder();
                try {
                    URL url=new URL(urls[0]);
                    HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream inputStream =urlConnection.getInputStream()
                } catch (IOException e) {
                    e.printStackTrace();
                } result=
            }
        }

        get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Pressed",Toast.LENGTH_LONG).show();

                String url ="http://api.openweathermap.org/data/2.5/weather?id=501175&appid=32879a100afc9b16435463591d9e99c9";
                GetWeather get_weather=new GetWeather();
                get_weather.execute();
            }
        });
    }

    public void getWeatherDetails(View view) {
    }
}
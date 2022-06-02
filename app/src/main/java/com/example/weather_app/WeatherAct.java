package com.example.weather_app;
import com.example.weather_app.MainActivity;

import androidx.appcompat.app.AppCompatActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAct extends AppCompatActivity {
    TextView showme;

    class GetWeather extends AsyncTask<String,Void,String>
    {
        protected String doInBackground(String... urls){
            StringBuilder result=new StringBuilder();
            try {
                //Вот весь этот блок мне вообще непонятен, как оно взаимодействует,хуй знает.
                //Похоже весь этот блок читает json в бэкграунде и присваивает его переменной result.
                URL url=new URL(urls[0]);
                HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream =urlConnection.getInputStream();
                BufferedReader reader =new BufferedReader(new InputStreamReader(inputStream));
                String line="";
                while ((line=reader.readLine())!=null)
                {
                    result.append(line).append("\n");
                } return result.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String output = "";
            try {
                JSONObject json = new JSONObject(result);
                JSONArray weather_array_block= json.getJSONArray("weather");
                JSONObject weather_array_dict_0=weather_array_block.getJSONObject(0);
                String description=weather_array_dict_0.getString("description");
                JSONObject main_dict=json.getJSONObject("main");
                double current_temperature=main_dict.getDouble("temp");

                output+="Clouds:"+description+
                        "\nCurrent temperature:" + current_temperature +" Celcium";

                showme.setText(output);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        showme=findViewById(R.id.weather_update);
        GetWeather get_weather=new GetWeather();
        String url ="http://api.openweathermap.org/data/2.5/weather?q=Rostov-on-Don&units=metric&appid=32879a100afc9b16435463591d9e99c9";
        Class destination=WeatherAct.class;
        get_weather.execute(url);





//        weather=findViewById(R.id.weather_update);
//        Intent transfer_intent=getIntent();
//        if(transfer_intent.hasExtra(Intent.EXTRA_TEXT)){
//            String current_weather= transfer_intent.getStringExtra(Intent.EXTRA_TEXT);
//            weather.setText(current_weather);
//        } else {
//            weather.setText("Не,нихуя");
//        }




    }
}
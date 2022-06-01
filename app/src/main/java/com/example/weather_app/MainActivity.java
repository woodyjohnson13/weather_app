package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //EditText city_name,country_name;
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
                    String line="123";
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

        get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Pressed",Toast.LENGTH_LONG).show();

                String url ="http://api.openweathermap.org/data/2.5/weather?q=Rostov-on-Don&units=metric&appid=32879a100afc9b16435463591d9e99c9";
                GetWeather get_weather=new GetWeather();
                get_weather.execute(url);
            }
        });
    }

    public void getWeatherDetails(View view) {
    }
}
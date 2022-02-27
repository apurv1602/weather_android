package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    
    EditText etCity, etCountry;
    TextView tvResult, temps,feels,humiditys ,descriptions, winds, cloud, pressures;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid ="f5104bfc84337a602495f556b1d99328";
    DecimalFormat df = new DecimalFormat( "#.##");
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
        temps = findViewById(R.id.temps);
        feels = findViewById(R.id.feels);
        humiditys = findViewById(R.id.humiditys);
        descriptions = findViewById(R.id.descriptions);
        winds = findViewById(R.id.winds);
        cloud = findViewById(R.id.cloud);
        pressures = findViewById(R.id.pressures);
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if (city.equals("")){
            tvResult.setText("city field can not be empty");
        }else{
            if(country.equals("")){
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
            }else{
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }
            StringRequest stringRequest= new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // Log.d("response", response);
                    String output = "";
                    String output1 = "";
                    String output2 = "";
                    String output3 = "";
                    String output4 = "";
                    String output5 = "";
                    String output6 = "";
                    String output7 = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") -273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");

                        output +=  " Current weather of " +cityName + "(" + countryName +")";
                        output1 += "\n Temp: " + df.format(temp) +"°C";
                        output2 += "\n Feels like: " +df.format(feelsLike) + "°C";
                        output3 += "\n Humidity: " + humidity+ "%";
                        output4 += "\n Description: " +description;
                        output5 +="\n Wind speed: " +wind +"m/s ";
                        output6 +="\n Cloudiness: " +clouds+"%";
                        output7 += "\n Pressure: " + pressure +"hPa";
                        tvResult.setText(output);
                        temps.setText(output1);
                        feels.setText(output2);
                        humiditys.setText(output3);
                        descriptions.setText(output4);
                        winds.setText(output5);
                        cloud.setText(output6);
                        pressures.setText(output7);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
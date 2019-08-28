package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView explainWeather;

    public void findweather(View view){

        Log.i("cityname=",cityName.getText().toString());
        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        try {
            String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            DownloadWeather task=new DownloadWeather();
            task.execute("https://samples.openweathermap.org/data/2.5/weather?q="+encodedCityName);

        }
        catch(UnsupportedEncodingException e){
            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);

        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(EditText) findViewById(R.id.cityName);
        explainWeather=(TextView) findViewById(R.id.explainWeather);

    }

        public class DownloadWeather extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... urls) {
                String result="";
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(urls[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();
                    while (data != -1) {
                        char current = (char) data;
                        result += current;
                        data = reader.read();

                    }
                    return result;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
                }


                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try
                {
                    String message="";
                    JSONObject jsonObject= new JSONObject(result);
                    String weatherinfo=jsonObject.getString("weather");
                    Log.i("data:",weatherinfo);
                    JSONArray arr=new JSONArray(weatherinfo);
                    for(int i=0;i<arr.length();i++){
                        JSONObject jsonpart=arr.getJSONObject(i);
                        Log.i("main",jsonpart.getString("main"));
                        Log.i("description", jsonpart.getString("description"));
                        String main="";
                        String description="";
                        main=jsonpart.getString("main");
                        description=jsonpart.getString("description");
                        if(main!="" && description!=""){
                            message=main+":  "+"\r\n" + description;
                            explainWeather.setText(message);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
                        }
                    }


                }
                catch (JSONException e){
                    Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
                }
            }
        }


}

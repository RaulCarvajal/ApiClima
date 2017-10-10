package com.example.raulrcg.apiclima;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView contenido;
    WServices http;
    String GET_TODO = "http://api.openweathermap.org/data/2.5/weather?q=Tepic,mx&APPID=0906362826d2cfea265ed029381a7e31";
    String json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contenido = (TextView)findViewById(R.id.contenido);

        http = new WServices();
        http.execute(GET_TODO, "1");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Actualizando información clima", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                http = new WServices();
                http.execute(GET_TODO, "1");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class WServices extends AsyncTask<String, Void, String> {
        URL url;
        @Override
        protected String doInBackground(String... params) {
            String cadena = "";
            if (params[1]== "1"){
                try {
                    url = new URL(GET_TODO);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    int respuesta = 0;
                    respuesta = connection.getResponseCode();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        while ((json_string = bufferedReader.readLine()) != null) {
                            stringBuilder.append(json_string + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        connection.disconnect();
                        String temporal = stringBuilder.toString();
                        JSONObject jsonObj = new JSONObject(temporal);
                        JSONArray clima = jsonObj.getJSONArray("weather");
                        JSONObject main = jsonObj.getJSONObject("main");

                        cadena +=main.getDouble("temp")-273;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return  cadena.substring(0,4)+"° C";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            contenido.setText(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}

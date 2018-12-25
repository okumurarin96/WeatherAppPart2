package com.example.rin.weatherapppart2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.rin.weatherapppart2.Common.Common;
import com.example.rin.weatherapppart2.Helper.Helper;
import com.example.rin.weatherapppart2.Model.OpenWeatherMap;
import com.example.rin.weatherapppart2.Model.ToaDo;
import com.example.rin.weatherapppart2.Model.WeatherInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String provider;
    static double lat, lng;
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();

    int MY_PERMISSION = 0;

    private DatabaseReference mDatabase;
    WeatherInfo weatherInfo;

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            Log.e("TAG", "No Location");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mDatabase = FirebaseDatabase.getInstance().getReference("ToaDo");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ToaDo toaDo = new ToaDo();
                toaDo = dataSnapshot.getValue(ToaDo.class);
                lat = Double.parseDouble(toaDo.getViDo());
                lng = Double.parseDouble(toaDo.getKinhDo());
                new GetWeather().execute(Common.apiRequest(String.valueOf(lat),String.valueOf(lng)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private class GetWeather extends AsyncTask<String,Void,String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];
            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("Error: Not found city")){
                pd.dismiss();
                return;
            }
            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>(){}.getType();
            openWeatherMap = gson.fromJson(s,mType);
            pd.dismiss();

            mDatabase = FirebaseDatabase.getInstance().getReference();
            weatherInfo = new WeatherInfo();
            weatherInfo.setCountry(String.format("%s",openWeatherMap.getSys().getCountry()));
            weatherInfo.setCity(String.format("%s",openWeatherMap.getName()));
            weatherInfo.setLastUpdate(String.format("%s", Common.getDateNow()));
            weatherInfo.setDescription(String.format("%s",openWeatherMap.getWeather().get(0).getDescription()));
            weatherInfo.setHumidity(String.format("%s",openWeatherMap.getMain().getHumidity()));
            weatherInfo.setCelsius(String.format("%s",openWeatherMap.getMain().getTemp()));
            weatherInfo.setCloud(String.format("%s",openWeatherMap.getCloud().getAll()));
            weatherInfo.setWild(String.format("%s",openWeatherMap.getWind().getSpeed()));
            weatherInfo.setWeatherIcon(String.format("%s",openWeatherMap.getWeather().get(0).getIcon()));
            weatherInfo.setId(String.format("%s",openWeatherMap.getId()));
            mDatabase.child("WeatherInfo").setValue(weatherInfo);

            CheckInfo();
        }
    }

    private void CheckInfo() {
        txt = (TextView) findViewById(R.id.txt);
        mDatabase = FirebaseDatabase.getInstance().getReference("WeatherInfo");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WeatherInfo weatherInfo1 = new WeatherInfo();
                weatherInfo1 = dataSnapshot.getValue(WeatherInfo.class);
                if ((weatherInfo.getId()).equals(weatherInfo1.getId())){
                    txt.setText("Send data successfully");
                } else {txt.setText("Send data failed");}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
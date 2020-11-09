package com.lab10.task3j;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Context;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.location.LocationManager.GPS_PROVIDER;

public class MainActivity extends AppCompatActivity {

    private TextView tvOut;
    private TextView tvLon;
    private TextView tvLat;
    private TextView tvAlt;
    private TextView time;
    private boolean started = false;
    private Long startTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvOut = findViewById(R.id.startValue);
        tvLat = findViewById(R.id.latitudeValue);
        tvLon = findViewById(R.id.longitudeValue);
        tvAlt = findViewById(R.id.altitudeValue);
        time = findViewById(R.id.timeValue);

        LocationBroadcastReceiver locationReceiver = new LocationBroadcastReceiver();
        registerReceiver(locationReceiver, new IntentFilter(
                LocationManager.KEY_LOCATION_CHANGED));

        Intent intent = new Intent(LocationManager.KEY_LOCATION_CHANGED);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ACCESS_FINE_LOCATION},0);
        }
        mlocManager.requestLocationUpdates(GPS_PROVIDER, 0L, 0f,
                pendingIntent);
        if (mlocManager.isProviderEnabled(GPS_PROVIDER)) {
            tvOut.append( getResources().getString(R.string.turned_on));
        } else {
            tvOut.append(getResources().getString(R.string.turned_off));
        }
    }

    void initData(Location location){
        if(location !=null)
        {
            if(started) {
                tvLat.setText("" + location.getLatitude());
                tvLon.setText("" + location.getLongitude());
                tvAlt.setText("" + location.getAltitude());
            }
        }
    }

    class LocationBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Location location = (Location) intent.getExtras().get(android.location.LocationManager.KEY_LOCATION_CHANGED);
                initData(location);
            }
        }
    }

    public void onClickStart(View v) {
        started = true;
        startTime = System.currentTimeMillis();
        time.setText("");
    }

    public void onClickStop(View v) {
        started = false;
        time.setText("" + (System.currentTimeMillis() - startTime) + " ms");
    }
}
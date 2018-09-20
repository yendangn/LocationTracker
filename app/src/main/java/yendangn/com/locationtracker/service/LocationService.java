package yendangn.com.locationtracker.service;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.util.Calendar;

import yendangn.com.locationtracker.R;
import yendangn.com.locationtracker.db.LocationDbHelper;
import yendangn.com.locationtracker.entity.MyLocation;
import yendangn.com.locationtracker.helper.LocationHelper;

public class LocationService extends Service {


    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        buildNotification();
    }

    @Override
    public void onDestroy() {
        Log.d("Location", "onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        requestLocationUpdates();

        return START_STICKY;
    }

    private void requestLocationUpdates() {

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            LocationHelper.getLocationServices(getApplicationContext()).requestLocationUpdates(LocationHelper.createLocationRequest(), new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        saveLocationToDB(location);
                        Log.d("Location", location.getLatitude() + "----" + location.getLongitude());
                    }
                }
            }, null);
        }
    }

    private void saveLocationToDB(final Location location) {

        MyLocation myLocation = new MyLocation();
        myLocation.setLatitude(location.getLatitude());
        myLocation.setLongitude(location.getLongitude());
        myLocation.setTime(Calendar.getInstance().getTimeInMillis());

        LocationDbHelper.saveLocation(getApplicationContext(), myLocation);

    }

    private void buildNotification() {

        String stopServiceIntent = "yendangn.com.locationtracker.service.stop";

        registerReceiver(stopReceiver, new IntentFilter(stopServiceIntent));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stopServiceIntent), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
        startForeground(1, builder.build());
    }

    BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };
}

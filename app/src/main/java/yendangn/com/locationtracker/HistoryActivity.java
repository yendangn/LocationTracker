package yendangn.com.locationtracker;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import yendangn.com.locationtracker.db.LocationDbHelper;
import yendangn.com.locationtracker.db.LocationRoomDatabase;
import yendangn.com.locationtracker.entity.MyLocation;

public class HistoryActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LocationDbHelper.getHistory(getApplicationContext(), new LocationDbHelper.GetLocationAsyncTaskCallback() {
            @Override
            public void onCompleted(List<MyLocation> myLocations) {
                if (!myLocations.isEmpty()) {


                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.width(5);
                    polylineOptions.color(Color.RED);

                    for (MyLocation location : myLocations) {

                        LatLng latLngs = new LatLng(location.getLatitude(), location.getLongitude());
                        polylineOptions.add(latLngs);

                    }

                    mMap.addPolyline(polylineOptions);

                    LatLng sydney = new LatLng(myLocations.get(0).getLatitude(), myLocations.get(0).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Start"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20));

                }
            }
        });

    }


}

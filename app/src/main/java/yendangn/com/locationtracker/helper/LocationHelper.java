package yendangn.com.locationtracker.helper;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    public static LocationRequest createLocationRequest() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return request;
    }

    public static FusedLocationProviderClient getLocationServices(Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }
}

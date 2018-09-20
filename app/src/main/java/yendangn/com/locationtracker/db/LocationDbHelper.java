package yendangn.com.locationtracker.db;

import android.content.Context;

import java.util.List;

import yendangn.com.locationtracker.entity.MyLocation;

public class LocationDbHelper {


    public interface GetLocationAsyncTaskCallback {
        void onCompleted(List<MyLocation> myLocations);
    }


    public static void getHistory(Context context, final GetLocationAsyncTaskCallback callback) {

        final AppExecutors appExecutors = new AppExecutors();
        final LocationRoomDatabase db = LocationRoomDatabase.getDatabase(context);

        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final List<MyLocation> locations = db.locationDao().loadLocations();

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCompleted(locations);
                    }
                });
            }
        });
    }

    public static void saveLocation(Context context, final MyLocation myLocation) {
        final AppExecutors appExecutors = new AppExecutors();
        final LocationRoomDatabase db = LocationRoomDatabase.getDatabase(context);

        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.locationDao().insert(myLocation);

            }
        });
    }

}

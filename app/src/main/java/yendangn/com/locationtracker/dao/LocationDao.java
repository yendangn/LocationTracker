package yendangn.com.locationtracker.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import yendangn.com.locationtracker.entity.MyLocation;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM mylocation")
    public List<MyLocation> loadLocations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(MyLocation... t);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<MyLocation> tList);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long createIfNotExists(MyLocation t);

    @Delete
    public void delete(MyLocation... t);
}

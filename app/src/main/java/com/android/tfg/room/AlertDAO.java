package com.android.tfg.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.android.tfg.model.AlertModel;

import java.util.List;

@Dao
public interface AlertDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AlertModel alert);

    @Update
    void update(AlertModel alert);

    @Delete
    void delete(AlertModel alert);

    @Query("SELECT * FROM alerts")
    LiveData<List<AlertModel>> getUserAlerts();

    @Query("SELECT * FROM alerts WHERE deviceID = :deviceID")
    AlertModel getDeviceAlert(String deviceID);

    @Query("DELETE FROM alerts")
    void clear();

}

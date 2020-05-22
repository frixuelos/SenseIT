package com.android.tfg.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.android.tfg.model.AlertModel;

@Database(entities = {AlertModel.class}, version = 1)
public abstract class AlertDatabase extends RoomDatabase {

    public abstract AlertDAO alertDAO();

}

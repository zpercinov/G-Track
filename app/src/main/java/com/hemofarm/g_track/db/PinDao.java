package com.hemofarm.g_track.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public interface PinDao {

    @Query("SELECT vrednost FROM Pin WHERE id = 1")
    String getPin();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPin(Pin pin);

    @Query("UPDATE Pin SET vrednost = :noviPin WHERE id = 1")
    void updatePin(String noviPin);

}

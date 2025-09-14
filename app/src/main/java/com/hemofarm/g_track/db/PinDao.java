package com.hemofarm.g_track.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public interface PinDao {

    @Query("SELECT vrednost FROM Pin WHERE id = 1")
    String dohvatiPin();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void unesiPin(Pin pin);

    @Query("UPDATE Pin SET vrednost = :noviPin WHERE id = 1")
    void azurirajPin(String noviPin);

}

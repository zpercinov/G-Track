package com.hemofarm.g_track.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ZapisDao {

@Insert(onConflict = OnConflictStrategy.ABORT)
 long unosZapisa (Zapis hpclLog);



    @Query("SELECT * FROM Zapis_koriscenja ORDER BY ime_korisnika ASC, datum_unosa DESC")
    List<Zapis> dohvatiSve();


   @Query("SELECT * FROM Zapis_koriscenja WHERE datum_unosa BETWEEN :start AND :end ORDER BY ime_korisnika ASC")
   List<Zapis> dohvatiSvePoDatumu(long start, long end);



    @Query("DELETE FROM Zapis_koriscenja WHERE id = :id")
    void obrisiPoId(int id);


}



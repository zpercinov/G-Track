package com.hemofarm.g_track.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;
@Dao
public interface LogDao {

@Insert(onConflict = OnConflictStrategy.ABORT)
 long insert (Log hpclLog);

    @Query("SELECT COUNT(*) FROM Log_koriscenja WHERE oznaka = :oznaka")
    int existsByOznaka(String oznaka);

    @Query("SELECT * FROM Log_koriscenja ORDER BY ime_korisnika ASC, datum_unosa DESC")
    List<Log> getAll();


   @Query("SELECT * FROM Log_koriscenja WHERE datum_unosa BETWEEN :start AND :end ORDER BY ime_korisnika ASC")
   List<Log> getAllByDate(long start, long end);



    @Query("DELETE FROM Log_koriscenja WHERE id = :id")
    void deleteById(int id);


}



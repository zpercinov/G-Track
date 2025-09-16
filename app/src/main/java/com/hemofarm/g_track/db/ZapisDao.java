package com.hemofarm.g_track.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hemofarm.g_track.ui.main.StatistikaKorisnika;

import java.util.List;
@Dao
public interface ZapisDao {

@Insert(onConflict = OnConflictStrategy.ABORT)
 long unosZapisa (Zapis zapis);



    @Query("SELECT * FROM Zapis ORDER BY ime_korisnika ASC, datum_unosa DESC")
    List<Zapis> dohvatiSveZapise();


   @Query("SELECT * FROM Zapis WHERE datum_unosa BETWEEN :start AND :end ORDER BY ime_korisnika ASC")
   List<Zapis> dohvatiSveZapiseNaDan(long start, long end);



    @Query("DELETE FROM Zapis WHERE ZapisID = :id")
    void obrisiPoId(int id);

    @Query("SELECT COUNT(*) FROM Zapis")
    long prikaziStatistikuZapisa();

    @Query("SELECT ime_korisnika AS ime_korisnika, COUNT(*) AS broj FROM Zapis GROUP BY ime_korisnika ORDER BY broj DESC")
    List<StatistikaKorisnika> prikaziStatistikuKorisnika();



    @Query("SELECT COUNT(*) FROM Zapis WHERE datum_unosa BETWEEN :start AND :end")
    long  prikaziStatistikuZapisaNaDan(long start, long end);


    @Query("SELECT ime_korisnika, COUNT(*) AS broj FROM Zapis WHERE datum_unosa BETWEEN :start AND :end ORDER BY broj DESC")
    List<StatistikaKorisnika>  prikaziStatistikuKorisnikaNaDan(long start, long end);



}



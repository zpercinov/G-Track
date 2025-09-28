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



    @Query("SELECT Zapis.* FROM Zapis " +
            "INNER JOIN Korisnik ON Zapis.korisnikID = Korisnik.korisnikID " +
            "ORDER BY Korisnik.ime_korisnika ASC, Zapis.datum_unosa DESC")
    List<Zapis> ucitajSveZapise();



    @Query ("SELECT Korisnik.ime_korisnika FROM Korisnik "+
            "INNER JOIN Zapis ON  Korisnik.korisnikID = Zapis.korisnikID "+
            "WHERE Zapis.korisnikID = :id LIMIT 1")
    String ucitajImeKorsnika(long id);


    @Query("SELECT Zapis.* FROM Zapis " +
            "INNER JOIN Korisnik ON Zapis.korisnikID = Korisnik.korisnikID " +
            "WHERE Zapis.datum_unosa BETWEEN :start AND :end " +
            "ORDER BY Korisnik.ime_korisnika ASC")
   List<Zapis> ucitajSveZapiseNaDan(long start, long end);


    @Query("DELETE FROM Zapis WHERE ZapisID = :id")
    void obrisiZapis(int id);

    @Query("SELECT COUNT(*) FROM Zapis")
    long prikaziStatistikuZapisa();

    @Query("SELECT Korisnik.ime_korisnika AS ime_korisnika, COUNT(Zapis.zapisID) AS broj " +
            "FROM Zapis " +
            "INNER JOIN Korisnik ON Zapis.korisnikID = Korisnik.korisnikID " +
            "GROUP BY Korisnik.ime_korisnika " +
            "ORDER BY broj DESC")
    List<StatistikaKorisnika> prikaziStatistikuKorisnika();

    @Query("SELECT COUNT(*) FROM Zapis WHERE datum_unosa BETWEEN :start AND :end")
    long  prikaziStatistikuZapisaNaDan(long start, long end);


    @Query("SELECT Korisnik.ime_korisnika AS ime_korisnika, COUNT(Zapis.zapisID) AS broj " +
            "FROM Zapis " +
            "INNER JOIN Korisnik ON Zapis.korisnikID = Korisnik.korisnikID " +
            "WHERE Zapis.datum_unosa BETWEEN :start AND :end " +
            "GROUP BY Korisnik.ime_korisnika " +
            "ORDER BY broj DESC")
    List<StatistikaKorisnika>  prikaziStatistikuKorisnikaNaDan(long start, long end);

}



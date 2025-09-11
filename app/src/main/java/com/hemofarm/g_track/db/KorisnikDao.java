package com.hemofarm.g_track.db;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface  KorisnikDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert (Korisnik korisnik);

    @Query("SELECT * FROM Korisnici WHERE ime_korisnika = :ime")
    Korisnik getKorisnik(String ime);

    @Query("DELETE FROM Korisnici WHERE ime_korisnika = :ime")
    void deleteByIme(String ime);

    @Query("SELECT * FROM Korisnici WHERE ime_korisnika = :ime AND lozinka = :lozinka LIMIT 1")
    Korisnik login(String ime, String lozinka);

    @Query("SELECT * FROM Korisnici ORDER BY ime_korisnika ASC")
    List<Korisnik> getAll();

    @Query("SELECT COUNT(*) FROM Korisnici WHERE ime_korisnika = :ime")
    int existsByIme(String ime);

}

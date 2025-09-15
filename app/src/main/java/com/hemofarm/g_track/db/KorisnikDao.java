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
    long kreirajNalog (Korisnik korisnik);

    @Query("SELECT * FROM Korisnik WHERE ime_korisnika = :ime")
    Korisnik dohvatiKorisnika(String ime);

    @Query("DELETE FROM Korisnik WHERE ime_korisnika = :ime")
    int brisanjeNalogaPoImenu(String ime);

    @Query("SELECT * FROM Korisnik WHERE ime_korisnika = :ime AND lozinka = :lozinka LIMIT 1")
    Korisnik login(String ime, String lozinka);

    @Query("SELECT * FROM Korisnik ORDER BY ime_korisnika ASC")
    List<Korisnik> dohvatiSveKorisnike();

    @Query("SELECT COUNT(*) FROM Korisnik WHERE ime_korisnika = :ime")
    int existsByIme(String ime);

}

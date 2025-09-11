package com.hemofarm.g_track.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "Korisnici",
 indices = {@Index(value = "ime_korisnika", unique = true)})
public class Korisnik {
    @PrimaryKey(autoGenerate = true)
    public int idKorisnika;

    @ColumnInfo ( name = "ime_korisnika")
    public String ime;
    @ColumnInfo ( name = "lozinka")
    public String lozinka;

    @ColumnInfo ( name = "email")
    public String email;

    public Korisnik(String ime, String lozinka, String email) {
        this.ime = ime;
        this.lozinka = lozinka;
        this.email = email;
    }

    public String getIme() {
        return ime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public String getEmail() {
        return email;
    }
}

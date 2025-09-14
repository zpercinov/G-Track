package com.hemofarm.g_track.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Zapis_koriscenja"
)
public class Zapis {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo( name = "oznaka")
    public String oznaka;

    @ColumnInfo( name = "ime_korisnika")
    public String korisnik;
    @ColumnInfo( name = "datum_unosa")
    public long datumUnosa;

    @ColumnInfo (name = "opis_stavke")
    public String opisStavke;


    public Zapis(String oznaka, String korisnik, long datumUnosa, String opisStavke) {
        this.oznaka = oznaka;
        this.korisnik = korisnik;
        this.datumUnosa = datumUnosa;
        this.opisStavke = opisStavke;
    }


}

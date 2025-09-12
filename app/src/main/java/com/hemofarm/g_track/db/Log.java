package com.hemofarm.g_track.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Log_koriscenja"
)
public class Log {

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


    public Log(String oznaka, String korisnik, long datumUnosa, String opisStavke) {
        this.oznaka = oznaka;
        this.korisnik = korisnik;
        this.datumUnosa = datumUnosa;
        this.opisStavke = opisStavke;
    }


}

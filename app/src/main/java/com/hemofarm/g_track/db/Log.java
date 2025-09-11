package com.hemofarm.g_track.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Log_koriscenja",
        indices = {@Index(value = "oznaka", unique = true)}
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


    public Log(String oznaka, String korisnik, long datumUnosa) {
        this.oznaka = oznaka;
        this.korisnik = korisnik;
        this.datumUnosa = datumUnosa;
    }


}

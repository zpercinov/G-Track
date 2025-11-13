package com.hemofarm.g_track.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Zapis",
        foreignKeys = @ForeignKey(
                entity = Korisnik.class,
                parentColumns = "korisnikID",
                childColumns = "korisnikID",
                onDelete = ForeignKey.CASCADE
        )
)
public class Zapis {
    @ColumnInfo(name = "korisnikID", index = true) // index за брже претраге
    public long korisnikID;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "zapisID")
    public int ZapisID;
    @ColumnInfo( name = "oznaka")
    public String oznaka;

    @ColumnInfo( name = "datum_unosa")
    public long datumUnosa;

    @ColumnInfo (name = "opis_kolone")
    public String opisKolone;

    @ColumnInfo (name = "opis_stavke")
    public String opisStavke;

    public Zapis(String oznaka, long korisnikID, long datumUnosa, String opisStavke, String opisKolone) {
        this.oznaka = oznaka;
        this.korisnikID = korisnikID;
        this.datumUnosa = datumUnosa;
        this.opisStavke = opisStavke;
        this.opisKolone = opisKolone;
    }

}

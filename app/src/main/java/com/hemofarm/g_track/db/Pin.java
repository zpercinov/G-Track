package com.hemofarm.g_track.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Pin {

    public Pin(int id, String vrednost) {
        this.id = id;
        this.vrednost = vrednost;
    }

    @PrimaryKey
    public  int id = 1;  // uvek samo jedan red (globalni PIN)

    @ColumnInfo(name = "vrednost")
    public String vrednost;
}

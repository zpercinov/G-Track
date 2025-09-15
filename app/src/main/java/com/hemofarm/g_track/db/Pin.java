package com.hemofarm.g_track.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Pin")
public class Pin {

    public Pin(int PinID, String vrednost) {
        this.PinID = PinID;
        this.vrednost = vrednost;
    }

    @PrimaryKey
    public  int PinID = 1;  // uvek samo jedan red (globalni PIN)

    @ColumnInfo(name = "vrednost")
    public String vrednost;
}

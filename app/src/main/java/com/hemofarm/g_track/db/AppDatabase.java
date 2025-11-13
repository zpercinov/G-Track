package com.hemofarm.g_track.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Korisnik.class, Zapis.class}, version = 7)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract KorisnikDao KorisnikDao();
    public abstract ZapisDao ZapisDao();


    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "gtrack_baza"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // samo za test, u produkciji ne
                    .build();

        }
        return INSTANCE;
}

}
package com.hemofarm.g_track.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Korisnik.class, Log.class, Pin.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract KorisnikDao KorisnikDao();
    public abstract LogDao LogDao();
    public abstract PinDao PinDao();

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

            // Inicijalizuj PIN odmah nakon što je baza spremna
            if (INSTANCE.PinDao().getPin() == null) {
                INSTANCE.PinDao().insertPin(new Pin(1, "1389"));
            }
        }
        return INSTANCE;
}
}
package com.hemofarm.g_track.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Korisnik.class, Log.class, Pin.class}, version = 1)
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
                    .fallbackToDestructiveMigration() // OVDE: briše staru bazu ako se promeni schema
                    .allowMainThreadQueries() // za testiranje
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            // Ubacivanje inicijalnog PIN-a u background thread
                            new Thread(() -> {
                                if (INSTANCE.PinDao().getPin() == null) {
                                    INSTANCE.PinDao().insertPin(new Pin(1, "1389")); // id=1
                                }
                            }).start();
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }
}


/*Primer Singleton
private static AppDatabase INSTANCE; → čuva jedinu instancu.
synchronized → garantuje da dve niti ne naprave istovremeno novu instancu.
context.getApplicationContext() → sprečava curenje memorije ako se koristi Activity context.
Uvek koristiš AppDatabase.getInstance(context) umesto new AppDatabase().
Tako osiguravaš da aplikacija ima samo jednu instancu baze i štediš resurse.
* */
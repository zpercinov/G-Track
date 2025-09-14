package com.hemofarm.g_track.ui.main;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hemofarm.g_track.R;
import com.hemofarm.g_track.db.AppDatabase;
import com.hemofarm.g_track.db.Zapis;
import com.hemofarm.g_track.util.Osluskivac;


import java.util.Calendar;

import java.util.List;


public class LogViewActivity extends AppCompatActivity {

    RecyclerView recyclerLog;
    LogAdapter adapter;
    ImageButton bIzlaz;
    CalendarView calendarView;
    private String izabraniDatum;
    private String preuzmiPin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.log_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerLog = findViewById(R.id.recyclerLog);
        recyclerLog.setLayoutManager(new LinearLayoutManager(this));
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new OsluskivacKalendara(this));

        // Učitaj podatke iz baze
        preuzmiPin =  AppDatabase.getInstance(this).PinDao().dohvatiPin();
        ucitajPodatke();




        bIzlaz = findViewById(R.id.btnIzlazLog);
        bIzlaz.setOnClickListener(new Osluskivac(this));

    }

    public void ucitajPodatke() {

        AppDatabase db = AppDatabase.getInstance(this);
        List<Zapis> logovi = db.LogDao().dohvatiSve();
        adapter = new LogAdapter(logovi);
        recyclerLog.setAdapter(adapter);



        // Postavljanje listener-a
        adapter.setOnItemClickListener(log -> {
            showPinDialog(log); // ime korisnika dolazi iz adaptera
        });
    }


    public void showPinDialog(Zapis log) {
        final EditText pinInput = new EditText(this);
        pinInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        pinInput.setHint("Unesite PIN");

        String datumFormat = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss", java.util.Locale.getDefault())
                .format(new java.util.Date(log.datumUnosa));

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Potvrda brisanja")
                .setMessage("Da li želite da obrišete stavku: " + log.oznaka + " " + log.opisStavke + " " + log.korisnik + " " + datumFormat + "? Unesite PIN da potvrdite.")
                .setView(pinInput)
                .setPositiveButton("Potvrdi", (dialog, which) -> {
                    String enteredPin = pinInput.getText().toString().trim();;
                    if (enteredPin.equals(preuzmiPin)) {
                        AppDatabase.getInstance(LogViewActivity.this)
                                .LogDao()
                                .obrisiPoId(log.id);
                        ucitajPodatke(); // osveži RecyclerView
                        android.widget.Toast.makeText(LogViewActivity.this,
                                "Podaci su uspešno obrisani!", android.widget.Toast.LENGTH_SHORT).show();
                    } else {
                        android.widget.Toast.makeText(LogViewActivity.this,
                                "Pogrešan PIN", android.widget.Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Otkaži", null)
                .show();
    }


    class OsluskivacKalendara implements CalendarView.OnDateChangeListener {
        LogViewActivity lva;

        public OsluskivacKalendara(LogViewActivity lva) {
            this.lva = lva;
        }

        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            // Izračunavanje početka dana
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth, 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long startOfDay = cal.getTimeInMillis();

            // Izračunavanje kraja dana
            cal.set(year, month, dayOfMonth, 23, 59, 59);
            cal.set(Calendar.MILLISECOND, 999);
            long endOfDay = cal.getTimeInMillis();

            // filtriraj logove po datumu
            List<Zapis> logovi = AppDatabase.getInstance(lva)
                    .LogDao()
                    .dohvatiSvePoDatumu(startOfDay, endOfDay);

            lva.adapter = new LogAdapter(logovi);
            lva.recyclerLog.setAdapter(lva.adapter);

            // Ponovo postavi klik listener
            lva.adapter.setOnItemClickListener(log -> {
                lva.showPinDialog(log);
            });
        }
    }
}
package com.hemofarm.g_track.ui.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.hemofarm.g_track.R;
import com.hemofarm.g_track.db.AppDatabase;
import com.hemofarm.g_track.db.Zapis;
import com.hemofarm.g_track.db.ZapisDao;
import com.hemofarm.g_track.util.Osluskivac;


import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class LogViewActivity extends AppCompatActivity {

    private LogAdapter adapterLog;
    private StatistikaAdapter adapterZaposlen;
    private ImageButton bIzlaz;
    private CalendarView calendarView;

    private String preuzmiPin;
    private TextView lBrojac;

    private RecyclerView recyclerLog, recyclerZaposlen;
    private TabLayout tabLayout;

    // Čuvanje selektovanog datuma
    private long trenutniStartOfDay = -1;
    private long trenutniEndOfDay = -1;

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

        // 🔹 inicijalizacija view-ova
        lBrojac = findViewById(R.id.tvBrojac);
        tabLayout = findViewById(R.id.tabLayout);
        recyclerLog = findViewById(R.id.recyclerLog);
        recyclerZaposlen = findViewById(R.id.recyclerZaposlen);
        calendarView = findViewById(R.id.calendarView);
        bIzlaz = findViewById(R.id.btnIzlazLog);

        recyclerLog.setLayoutManager(new LinearLayoutManager(this));
        recyclerZaposlen.setLayoutManager(new LinearLayoutManager(this));

        // 🔹 Postavljanje tabova i listenera
        postaviTaboveIRecycler(tabLayout, recyclerLog, recyclerZaposlen);

        // 🔹 Popuni podatke po defaultu
        PrikaziPodatkeTab1();
        PrikaziPodatkeTab2();
        prikaziBrojac(null, null);

        // 🔹 Preuzmi PIN iz baze
        preuzmiPin = AppDatabase.getInstance(this).PinDao().dohvatiPin();

        // 🔹 Klik na izlaz dugme
        bIzlaz.setOnClickListener(new Osluskivac(this));

        // 🔹 Kalendar listener
        calendarView.setOnDateChangeListener(new OsluskivacKalendara(this));



    }

    private void prikaziBrojac(final Long startOfDay, final Long endOfDay) {
        AppDatabase db = AppDatabase.getInstance(this);

        Executors.newSingleThreadExecutor().execute(() -> {
            final long brojac;
            if (startOfDay == null || endOfDay == null) {
                brojac = db.ZapisDao().prikaziStatistikuZapisa(); // ukupno
            } else {
                brojac = db.ZapisDao().prikaziStatistikuZapisaNaDan(startOfDay, endOfDay);
            }

            runOnUiThread(() -> {
                if (startOfDay == null || endOfDay == null) {
                    lBrojac.setText("Ukupno: " + brojac);
                } else {
                    lBrojac.setText("Na izabrani dan: " + brojac);
                }
            });
        });
    }

    private void PrikaziPodatkeTab1() {
        AppDatabase db = AppDatabase.getInstance(this);
        List<Zapis> logovi = db.ZapisDao().dohvatiSveZapise();
        adapterLog = new LogAdapter(this, logovi);
        recyclerLog.setAdapter(adapterLog);

        adapterLog.setOnItemClickListener(this::showPinDialog);
    }

    private void PrikaziPodatkeTab2() {
        AppDatabase db = AppDatabase.getInstance(this);
        List<StatistikaKorisnika> statistika = db.ZapisDao().prikaziStatistikuKorisnika();
        adapterZaposlen = new StatistikaAdapter(statistika);
        recyclerZaposlen.setAdapter(adapterZaposlen);
    }

    private void showPinDialog(Zapis log) {



        final EditText pinInput = new EditText(this);
        pinInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        pinInput.setHint("Unesite PIN");

        AppDatabase db = AppDatabase.getInstance(this);
        ZapisDao zapisDao = db.ZapisDao();
        String imeKorisnika = zapisDao.dohvatiImeKorsnika(log.korisnikID);

        String datumFormat = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss", java.util.Locale.getDefault())
                .format(new java.util.Date(log.datumUnosa));

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Potvrda brisanja")
                .setMessage("Da li želite da obrišete stavku: " + log.oznaka + " " + log.opisStavke + " " + imeKorisnika + " " + datumFormat + "? Unesite PIN da potvrdite.")
                .setView(pinInput)
                .setPositiveButton("Potvrdi", (dialog, which) -> {
                    String enteredPin = pinInput.getText().toString().trim();
                    if (enteredPin.equals(preuzmiPin)) {
                        AppDatabase.getInstance(LogViewActivity.this)
                                .ZapisDao()
                                .obrisiPoId(log.ZapisID);
                        Toast.makeText(this, "Stavka je uspešno obrisana", Toast.LENGTH_SHORT).show();
                        PrikaziPodatkeTab1();
                        filtrirajLog(trenutniStartOfDay, trenutniEndOfDay);
                        prikaziBrojac(trenutniStartOfDay,trenutniEndOfDay);
                    } else {

                        Toast.makeText(this, "Pogrešan PIN", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Otkaži", null)
                .show();
    }

    private void postaviTaboveIRecycler(TabLayout tabLayout, RecyclerView recyclerLog, RecyclerView recyclerZaposlen) {
        tabLayout.addTab(tabLayout.newTab().setText("Lista unosa"));
        tabLayout.addTab(tabLayout.newTab().setText("Statistika zaposlenih"));

        postaviFontIBojuZaTabove(tabLayout, 16, getResources().getColor(R.color.narandzasta));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    recyclerLog.setVisibility(View.VISIBLE);
                    recyclerZaposlen.setVisibility(View.GONE);
                    if (trenutniStartOfDay != -1) filtrirajLog(trenutniStartOfDay, trenutniEndOfDay);
                } else {
                    recyclerLog.setVisibility(View.GONE);
                    recyclerZaposlen.setVisibility(View.VISIBLE);
                    if (trenutniStartOfDay != -1) filtrirajStatistiku(trenutniStartOfDay, trenutniEndOfDay);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void postaviFontIBojuZaTabove(TabLayout tabLayout, float velicinaSp, int boja) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                TextView tv = new TextView(this);
                tv.setText(tab.getText());
                tv.setTextSize(velicinaSp);
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv.setTextColor(boja);
                tv.setGravity(Gravity.CENTER);
                tab.setCustomView(tv);
            }
        }
    }

    private void filtrirajLog(long start, long end) {
        List<Zapis> logovi = AppDatabase.getInstance(this)
                .ZapisDao()
                .dohvatiSveZapiseNaDan(start, end);
        adapterLog = new LogAdapter(this, logovi);
        recyclerLog.setAdapter(adapterLog);
        adapterLog.setOnItemClickListener(this::showPinDialog);
    }

    private void filtrirajStatistiku(long start, long end) {
        List<StatistikaKorisnika> statistika = AppDatabase.getInstance(this)
                .ZapisDao()
                .prikaziStatistikuKorisnikaNaDan(start, end);
        adapterZaposlen = new StatistikaAdapter(statistika);
        recyclerZaposlen.setAdapter(adapterZaposlen);
    }

    // unutar LogViewActivity klase
    class OsluskivacKalendara implements CalendarView.OnDateChangeListener {
        LogViewActivity lva;

        public OsluskivacKalendara(LogViewActivity lva) {
            this.lva = lva;
        }

        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            // Postavljanje početka i kraja dana
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth, 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);
            lva.trenutniStartOfDay = cal.getTimeInMillis();

            cal.set(year, month, dayOfMonth, 23, 59, 59);
            cal.set(Calendar.MILLISECOND, 999);
            lva.trenutniEndOfDay = cal.getTimeInMillis();

            int aktivanTab = lva.tabLayout.getSelectedTabPosition();

            if (aktivanTab == 0) {
                lva.filtrirajLog(lva.trenutniStartOfDay, lva.trenutniEndOfDay);
            } else {
                lva.filtrirajStatistiku(lva.trenutniStartOfDay, lva.trenutniEndOfDay);
            }

            lva.prikaziBrojac(lva.trenutniStartOfDay, lva.trenutniEndOfDay);
        }
    }

}

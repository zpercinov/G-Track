package com.hemofarm.g_track.ui.main;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hemofarm.g_track.R;
import com.hemofarm.g_track.db.AppDatabase;
import com.hemofarm.g_track.db.Log;
import com.hemofarm.g_track.ui.account.AccountActivity;
import com.hemofarm.g_track.ui.login.mail.AsinhroniZadatak;
import com.hemofarm.g_track.util.Osluskivac;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public TextView tKorisnik;

    public int lastInsertedId = -1;
    ImageButton bLogOut;
     public EditText eOznaka;
     ImageButton bQr;
     Button bSave;

     LinearLayout lUnos;

     Button bIzvoz;
     Button bPrikaz;
     Button bAttEmail;


    public TextView tPrikazOznaka;
    public TextView tPrikazUser;
    public TextView tPrikazDatum;
    public TextView tPrikazOpisStavke;

    public EditText eOpisStavke;
     public ActivityResultLauncher<ScanOptions> barcodeLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eOpisStavke = findViewById(R.id.edtOpisStavke);

        tPrikazOznaka = findViewById(R.id.tvOznaka);
        tPrikazUser = findViewById(R.id.tvUser);
        tPrikazDatum = findViewById(R.id.tvDatum);
        tPrikazOpisStavke = findViewById(R.id.tvOpisStavke);
        bAttEmail = findViewById(R.id.btnAttMail);

        bPrikaz = findViewById(R.id.btnPrikaz);
        bPrikaz.setOnClickListener(new Osluskivac(this));

        bAttEmail.setOnClickListener(new Osluskivac(this));



        bIzvoz = findViewById(R.id.btnIzvoz);
        bIzvoz.setOnClickListener(new Osluskivac(this));

        lUnos = findViewById(R.id.llUnos);
        lUnos.setOnClickListener(new Osluskivac(this));

        bSave = findViewById(R.id.btnSave);
        bSave.setOnClickListener(new Osluskivac(this));


        tKorisnik = findViewById(R.id.txtUser);

        bLogOut = findViewById(R.id.btnLogOut);
        eOznaka = findViewById(R.id.edtOznaka);
        bQr = findViewById(R.id.btnQr);


        bLogOut.setOnClickListener(new Osluskivac(this));
        bQr.setOnClickListener(new Osluskivac(this));
        Intent intent = getIntent();
        tKorisnik.setText(intent.getStringExtra("ime_korisnika"));

// Aktivnost za skeniranje QR koda
        barcodeLauncher = registerForActivityResult(
                new ScanContract(),
                result -> {
                    if (result.getContents() != null) {
                        eOznaka.setText(result.getContents());
                    }
                }
        );

        /*
// Aktivnost za skeniranje QR koda bez lambda izraza
barcodeLauncher = registerForActivityResult(
        new ScanContract(),
        new ActivityResultCallback<ScanIntentResult>() {
            @Override
            public void onActivityResult(ScanIntentResult result) {
                if (result.getContents() != null) {
                    eOznaka.setText(result.getContents());
                }
            }
        }
);

        */

    }
    public void izveziLogoveUCSV(Context context) {
        // Dobavi sve logove iz baze
        AppDatabase db = AppDatabase.getInstance(context);
        List<Log> sviLogovi = db.LogDao().getAll();

        if (sviLogovi.isEmpty()) {
            Toast.makeText(context, "Nema podataka za izvoz", Toast.LENGTH_SHORT).show();
            return;
        }

        // Napravi CSV sadržaj
        StringBuilder data = new StringBuilder();
        data.append("Oznaka,Korisnik,Datum\n"); // header

        for (Log log : sviLogovi) {
            String datumFormat = android.text.format.DateFormat.format("dd.MM.yyyy HH:mm", new Date(log.datumUnosa)).toString();
            data.append(log.oznaka).append(",")
                    .append(log.korisnik).append(",")
                    .append(datumFormat).append(",")
                    .append(log.opisStavke)
                    .append("\n");
        }

        // Kreiraj fajl
        try {
            // Samo za Android 10+ (API 29+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                String trenutnoVreme = android.text.format.DateFormat.format("dd.MM.yyyy HH_mm", new java.util.Date()).toString();
                String nazivFajla ="logovi_"+trenutnoVreme+".csv";

                values.put(MediaStore.Downloads.DISPLAY_NAME, nazivFajla);
                values.put(MediaStore.Downloads.MIME_TYPE, "text/csv");
                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

                    if (uri != null) {
                        OutputStream os = context.getContentResolver().openOutputStream(uri);
                        os.write(data.toString().getBytes());
                        os.close();
                        Toast.makeText(context, "Izvezeno u Downloads folder!", Toast.LENGTH_LONG).show();
                            } else {
                                    Toast.makeText(context, "Greška pri kreiranju fajla!", Toast.LENGTH_SHORT).show();
                                    }
            } else {
                Toast.makeText(context, "Ova funkcija radi samo na Android 10+", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Greška- izvoz nije uspeo!", Toast.LENGTH_SHORT).show();
        }
    }


    public void posaljiLogove(Context context, String recipient, String sender) {
        // Dobavi sve logove iz baze
        AppDatabase db = AppDatabase.getInstance(context);
        List<Log> sviLogovi = db.LogDao().getAll();

        if (sviLogovi.isEmpty()) {
            Toast.makeText(context, "Nema podataka za slanje.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Napravi CSV sadržaj
        StringBuilder data = new StringBuilder();
        data.append("Oznaka,Korisnik,Datum\n"); // header

        for (Log log : sviLogovi) {
            String datumFormat = android.text.format.DateFormat.format("dd.MM.yyyy HH:mm", new Date(log.datumUnosa)).toString();
            data.append(log.oznaka).append(",")
                    .append(log.korisnik).append(",")
                    .append(datumFormat)
                    .append("\n");
        }

        String trenutnoVreme = android.text.format.DateFormat.format("dd.MM.yyyy HH:mm", new java.util.Date()).toString();
        // Predmet i telo mejla
        String subject = "Izveštaj logova na dan: "+trenutnoVreme;
        String body = "Poštovani.\n\nu prilogu emaila se nalaze  fajl sa podacima.\n\nSrdačan pozdrav,\nG-Track admin";
        byte[] csvData = data.toString().getBytes();

        // Pokreni asinhroni zadatak za slanje mejla
        AsinhroniZadatak task = new AsinhroniZadatak(subject, body, recipient, sender, context, csvData);
        task.execute();
    }


    public void OtvoriViewFormu() {

        Intent intent = new Intent(this, LogViewActivity.class);
        this.startActivity(intent);


    }

}
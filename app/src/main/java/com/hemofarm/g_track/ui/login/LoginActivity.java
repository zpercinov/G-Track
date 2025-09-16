package com.hemofarm.g_track.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hemofarm.g_track.R;
import com.hemofarm.g_track.db.AppDatabase;
import com.hemofarm.g_track.db.Korisnik;
import com.hemofarm.g_track.ui.login.mail.AsinhroniZadatak;
import com.hemofarm.g_track.ui.main.MainActivity;
import com.hemofarm.g_track.ui.account.AccountActivity;
import com.hemofarm.g_track.util.Osluskivac;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

     public Spinner korIme;
     public EditText eLozinka;
    TextView tKreirajNalog;
    Button bPrijava;

    ImageButton bIzlaz;
    TextView tZaboravljenaLozinka;

    public static String sender = "gtrack.info.noreply@gmail.com";
    public static String password = "jffn tuvm iffv ctiv"; // Uneti vas password



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        korIme = findViewById(R.id.cbKorIme);
        eLozinka = findViewById(R.id.edtLozinka);
        bPrijava = findViewById(R.id.btnPrijava);
        tKreirajNalog = findViewById(R.id.txtKreirajNalog);
        inicijalizacijaSpinera();

        bPrijava.setOnClickListener(new Osluskivac(this));
        tKreirajNalog.setOnClickListener(new Osluskivac(this));
        bIzlaz = findViewById(R.id.btnIzlazApp);
        bIzlaz.setOnClickListener(new Osluskivac(this));

        tZaboravljenaLozinka = findViewById(R.id.txtZaboravljenaLozinka);
        tZaboravljenaLozinka.setOnClickListener(new Osluskivac(this));



    }

    public void OtvoriMainFormu() {

        Intent intent = new Intent(this, MainActivity.class);
        String korisnik = korIme.getSelectedItem().toString();
        intent.putExtra("ime_korisnika", korisnik);
        this.startActivity(intent);


    }


    public void OtvoriAccountFormu() {

        Intent intent = new Intent(this, AccountActivity.class);

        this.startActivity(intent);


    }

    @Override
    protected void onResume() {
        super.onResume();

    inicijalizacijaSpinera();
    eLozinka.setText("");

    }



    public void inicijalizacijaSpinera () {
        List<String> korisnici = new ArrayList<>();
        korisnici.add("Izaberi korisnika"); // hint na početku

        // Učitavanje korisnika iz baze
        AppDatabase db = AppDatabase.getInstance(this);
        List<Korisnik> listaKorisnika = db.KorisnikDao().dohvatiSveKorisnike(); // pretpostavljamo da vraća List<Korisnik>
        for (Korisnik k : listaKorisnika) {
            korisnici.add(k.getIme()); // dodaje ime korisnika u listu
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_items, korisnici) {
            @Override
            public boolean isEnabled(int position) {
                // Prvi element (hint) se ne može odabrati
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Hint u sivoj boji u dropdownu
                    tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.crna));
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_items);
        korIme.setAdapter(adapter);
    }

    public void obradiZaboravljenuLozinku() {
        String imeTemp = "";
        if (korIme.getSelectedItem() != null) {
            imeTemp = korIme.getSelectedItem().toString();
        }

        if (imeTemp.isEmpty()) {
            Toast.makeText(this, "Odaberite korisničko ime!", Toast.LENGTH_SHORT).show();
            return;
        }
        final String ime = imeTemp;

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(LoginActivity.this);
            Korisnik k = db.KorisnikDao().dohvatiKorisnika(ime);

            if (k != null) {
                String teloPoruke = "Poštovani " + k.getIme() + ",\n\nVaša lozinka je: "
                        + k.getLozinka() + "\n\nSrdačan pozdrav,\nG-Track admin";

                new AsinhroniZadatak(
                        "Dostavljanje zaboravljene lozinke",
                        teloPoruke,
                        k.getEmail(),
                        sender,
                        LoginActivity.this
                ).execute();

            } else {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Korisnik nije pronađen!", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    public void prijavaKorisnika() {
        int position = korIme.getSelectedItemPosition();
        String pass = eLozinka.getText().toString().trim();

        if (position == 0 || pass.isEmpty()) {
            Toast.makeText(this, "Izaberite korisnika i unesite lozinku", Toast.LENGTH_SHORT).show();
            return;
        }

        String korisnikIme = this.korIme.getSelectedItem().toString();

        // Provera korisnika i lozinke u bazi
        AppDatabase db = AppDatabase.getInstance(this);
        Korisnik k = db.KorisnikDao().login(korisnikIme, pass);

        if (k != null) {
            Toast.makeText(this, "Prijava uspešna za " + korisnikIme, Toast.LENGTH_SHORT).show();
            this.OtvoriMainFormu();
        } else {
            Toast.makeText(this, "Pogrešna lozinka!", Toast.LENGTH_SHORT).show();
        }
    }

}


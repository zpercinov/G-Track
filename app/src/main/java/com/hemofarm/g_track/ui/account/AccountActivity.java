package com.hemofarm.g_track.ui.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hemofarm.g_track.R;
import com.hemofarm.g_track.db.AppDatabase;
import com.hemofarm.g_track.db.Korisnik;
import com.hemofarm.g_track.util.Osluskivac;

import java.util.List;

public class AccountActivity extends AppCompatActivity {
    ImageButton bIzlaz;
    Button bRegistracija;
    public EditText eKorIme;
    public EditText eLozinka;
    public EditText eLozinkaCheck;
    public AutoCompleteTextView eEmail;
    String preuzmiPin;


    RecyclerView recyclerUsers;
    UserAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user_management), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eKorIme = findViewById(R.id.edtKorIme);
        eLozinka = findViewById(R.id.edtLozinka);
        eLozinkaCheck = findViewById(R.id.edtLozinkaCheck);

        eEmail = findViewById(R.id.edtEmail);





        eEmail.addTextChangedListener(new TextWatcher() {
            boolean isUpdating = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String text = s.toString();

                // Dodaj predlog samo ako polje NIJE prazno i ne sadrži "@"
                if (!text.isEmpty() && !text.contains("@")) {
                    isUpdating = true;
                    eEmail.setText(text + "@hemofarm.com");
                    eEmail.setSelection(text.length()); // kursor pre @
                    isUpdating = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });



        bIzlaz = findViewById(R.id.btnIzlazApp);
        bIzlaz.setOnClickListener(new Osluskivac(this));

        bRegistracija = findViewById(R.id.btnRegistracija);
        bRegistracija.setOnClickListener(new Osluskivac(this));


        recyclerUsers = findViewById(R.id.recyclerUsers);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));

        preuzmiPin =  AppDatabase.getInstance(this).PinDao().dohvatiPin();
        ucitajKorisnike();



    }

    public void ucitajKorisnike() {
        AppDatabase db = AppDatabase.getInstance(this);
        List<Korisnik> lista = db.KorisnikDao().dohvatiSveKorisnike();
        adapter = new UserAdapter(lista);
        recyclerUsers.setAdapter(adapter);


        // Postavljanje listener-a
        adapter.setOnItemClickListener(korisnikIme -> {
            DohvatiPrikaziPinDialog(korisnikIme); // ime korisnika dolazi iz adaptera
        });
        }




    public void DohvatiPrikaziPinDialog(String korisnik) {
        final EditText pinDohvatiUnos = new EditText(this);
        pinDohvatiUnos.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        pinDohvatiUnos.setHint("Unesite PIN");

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Potvrda brisanja")
                .setMessage("Da li želite da obrišete nalog? " + korisnik + "? Unesite PIN da potvrdite.")
                .setView(pinDohvatiUnos)
                .setPositiveButton("Potvrdi", (dialog, which) -> {
                    String enteredPin = pinDohvatiUnos.getText().toString();

                    if (enteredPin.equals(preuzmiPin)) {
                        int korObrisan = AppDatabase.getInstance(AccountActivity.this)
                                .KorisnikDao()
                                .brisanjeNalogaPoImenu(korisnik);

                        if (korObrisan > 0) {
                            ucitajKorisnike(); // osveži RecyclerView
                            android.widget.Toast.makeText(AccountActivity.this,
                                    "Nalog je obrisan!", android.widget.Toast.LENGTH_SHORT).show();
                        } else {
                            android.widget.Toast.makeText(AccountActivity.this,
                                    "Greška - nalog nije obrisan", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        android.widget.Toast.makeText(AccountActivity.this,
                                "Pogrešan PIN", android.widget.Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Otkaži", null)
                .show();
    }

}
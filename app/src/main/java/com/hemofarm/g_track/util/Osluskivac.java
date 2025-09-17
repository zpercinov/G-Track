package com.hemofarm.g_track.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hemofarm.g_track.db.AppDatabase;
import com.hemofarm.g_track.db.Korisnik;
import com.hemofarm.g_track.db.Zapis;
import com.hemofarm.g_track.ui.account.AccountActivity;
import com.hemofarm.g_track.ui.login.LoginActivity;
import com.hemofarm.g_track.ui.login.mail.AsinhroniZadatak;
import com.hemofarm.g_track.ui.main.LogViewActivity;
import com.hemofarm.g_track.ui.main.MainActivity;
import com.hemofarm.g_track.R;
import com.journeyapps.barcodescanner.ScanOptions;
import java.util.Date;
import java.util.List;

public class Osluskivac implements View.OnClickListener {
    private MainActivity ma;
    private LoginActivity la;
    private AccountActivity aa;

    private LogViewActivity lva;

    // Konstruktor za MainActivity
    public Osluskivac(MainActivity ma) {
        this.ma = ma;
    }

    // Konstruktor za LoginActivity
    public Osluskivac(LoginActivity la) {
        this.la = la;
    }
    // Konstruktor za AccountActivity
    public Osluskivac(AccountActivity aa) {
        this.aa = aa;
    }

    public Osluskivac(LogViewActivity lva) {
        this.lva = lva;
    }

    @Override
    public void onClick(View v) {
        if (ma != null) {
            // Ovde su akcije vezane za MainActivity

            if (v.getId() == R.id.btnAttMail) {

                if (imaInternet(ma)) {
                    String ime = ma.tKorisnik.getText().toString();
                    AppDatabase db = AppDatabase.getInstance(ma);
                    Korisnik k = db.KorisnikDao().dohvatiKorisnika(ime);

                    ma.posaljiZapise(ma, k.getEmail(), LoginActivity.sender);
                } else {
                    Toast.makeText(ma, "Nema internet konekcije", Toast.LENGTH_SHORT).show();
                }
            }



            if (v.getId() == R.id.btnLogOut) {
                //Intent intent = new Intent(ma, LoginActivity.class);
                //ma.startActivity(intent);
                ma.finish();
                Toast.makeText(ma, "Uspešno ste se odjavili!", Toast.LENGTH_SHORT).show();
            }


            if (v.getId() == R.id.btnIzvoz) {
                ma.izveziZapiseUCSV(ma);

            }


            if (v.getId() == R.id.btnPrikaz)  {

                new Thread(() -> {
                    AppDatabase db = AppDatabase.getInstance(ma);
                    List<Zapis> listaZapisa = db.ZapisDao().dohvatiSveZapise();
                    ma.runOnUiThread(() -> {
                        if (listaZapisa != null && !listaZapisa.isEmpty())
                            ma.OtvoriViewFormu();
                        else
                            Toast.makeText(ma, "Nema podataka za prikaz", Toast.LENGTH_SHORT).show();
                    });
                }).start();
            }



            if (v.getId() == R.id.llUnos) {

                ma.obrisiPoslednjiZapis();
            }


            if (v.getId() == R.id.btnQr) {

                ma.pokreniQrSkeniranje();
            }


            if (v.getId() == R.id.btnSave) {

                ma.sacuvajZapis();

        }
        }

        if (la != null) {
            // Ovde su akcije vezane za LoginActivity

            if (v.getId() == R.id.txtZaboravljenaLozinka) {

                    if (imaInternet(la))
                        la.obradiZaboravljenuLozinku();
                    else
                        Toast.makeText(la, "Nema internet konekcije", Toast.LENGTH_SHORT).show();

            }

            if (v.getId() == R.id.btnIzlazApp) {

                la.finish();

            }

            if (v.getId() == R.id.btnPrijava) {

                la.prijavaKorisnika();
            }


            if (v.getId() == R.id.txtKreirajNalog) {
                la.OtvoriAccountFormu();

            }
        }

        if (aa != null) {
            // Ovde su akcije vezane za AccountActivity




        if (v.getId() == R.id.btnIzlazApp) {
            aa.finish();

        }


            if (v.getId() == R.id.btnRegistracija ) {

                aa.kreirajNalog();


            }
        }

        if (lva != null) {

            if (v.getId() == R.id.btnIzlazLog) {
                lva.finish();

            }

        }
    }

    private boolean imaInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return nc != null &&
                    (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        }
        return false;
    }


}

package com.hemofarm.g_track.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
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

                String ime = ma.tKorisnik.getText().toString();
                AppDatabase db = AppDatabase.getInstance(ma);
                Korisnik k = db.KorisnikDao().dohvatiKorisnika(ime);

                ma.posaljiLogove(ma, k.getEmail(), LoginActivity.sender);

            }


            if (v.getId() == R.id.btnLogOut) {
                //Intent intent = new Intent(ma, LoginActivity.class);
                //ma.startActivity(intent);
                ma.finish();
                Toast.makeText(ma, "Uspešno ste se odjavili!", Toast.LENGTH_SHORT).show();
            }


            if (v.getId() == R.id.btnIzvoz) {
                ma.izveziLogoveUCSV(ma);

            }


            if (v.getId() == R.id.btnPrikaz)  {

                ma.OtvoriViewFormu();


            }



            if (v.getId() == R.id.llUnos) {

                final String oznaka = ma.tPrikazOznaka.getText().toString().trim();
                final String user = ma.tPrikazUser.getText().toString().trim();
                final String datum = ma.tPrikazDatum.getText().toString().trim();
                final String opisStavke = ma.tPrikazOpisStavke.getText().toString().trim();

                if (!oznaka.isEmpty()) {

                    // prvo proveri da li postoji u bazi

                    if (ma.lastInsertedId != -1) {
                        // postoji → pokaži dijalog za potvrdu brisanja
                        new AlertDialog.Builder(ma)
                                .setTitle("Potvrda brisanja")
                                .setMessage("Da li ste sigurni da želite da obrišete unete podatke: " + oznaka + " "+ opisStavke +" "+ user + " " + datum + "?")
                                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AppDatabase.getInstance(ma).LogDao().obrisiPoId(ma.lastInsertedId);
                                        ma.tPrikazOznaka.setText("Oznaka");
                                        ma.tPrikazOpisStavke.setText("Opis");
                                        ma.tPrikazUser.setText("Korisnik");
                                        ma.tPrikazDatum.setText("Datum unosa");// očisti prikaz
                                        Toast.makeText(ma, "Uneti podaci obrisani iz baze!", Toast.LENGTH_SHORT).show();
                                        ma.lastInsertedId = -1; // reset
                                    }
                                })
                                .setNegativeButton("Ne", null)
                                .show();
                    } else {
                        // ne postoji u bazi
                        Toast.makeText(ma, "Podaci ne postoje u bazi!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ma, "Greška-podaci nisu obrisani!", Toast.LENGTH_SHORT).show();
                }
            }


            if (v.getId() == R.id.btnQr) {
                ScanOptions options = new ScanOptions();
                options.setPrompt("Skeniraj QR kod");
                options.setBeepEnabled(true);
                options.setCaptureActivity(PortraitCaptureActivity.class);
                options.setOrientationLocked(true);

                ma.barcodeLauncher.launch(options);
            }


            if (v.getId() == R.id.btnSave) {
                String oznaka = ma.eOznaka.getText().toString().trim();
                String korisnik = ma.tKorisnik.getText().toString().trim();
                String opisStavke = ma.eOpisStavke.getText().toString().trim();
                long datum = System.currentTimeMillis();

                if (oznaka.isEmpty()) {
                    Toast.makeText(ma, "Unesite oznaku!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (opisStavke.isEmpty()) {
                    Toast.makeText(ma, "Unesite opis stavke!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Zapis unos = new Zapis(oznaka, korisnik, datum, opisStavke);
                AppDatabase db = AppDatabase.getInstance(ma);

                long newId = db.LogDao().unosZapisa(unos); // vratiće ID unosa

                if (newId != -1) { // uspešno sačuvan
                    ma.lastInsertedId = (int) newId;

                    ma.tPrikazOznaka.setText(oznaka);
                    ma.tPrikazUser.setText(korisnik);
                    ma.tPrikazDatum.setText(android.text.format.DateFormat.format(
                            "dd.MM.yyyy HH:mm:ss", new Date(datum)));
                    ma.tPrikazOpisStavke.setText(opisStavke);

                    ToneGenerator toneGenOk = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    toneGenOk.startTone(ToneGenerator.TONE_PROP_BEEP, 150); // standardni beep

                    Toast.makeText(ma, "Zapis je uspešno sačuvan!", Toast.LENGTH_SHORT).show();
                } else { // neuspešan unos
                    Toast.makeText(ma, "Greška - zapis nije sačuvan", Toast.LENGTH_SHORT).show();
                }
            }

        }


        if (la != null) {
            // Ovde su akcije vezane za LoginActivity

            if (v.getId() == R.id.txtZaboravljenaLozinka) {
                String imeTemp = "";
                if (la.korIme.getSelectedItem() != null) {
                    imeTemp = la.korIme.getSelectedItem().toString();
                }

                if (imeTemp.isEmpty()) {
                    Toast.makeText(la, "Odaberite korisničko ime!", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String ime = imeTemp; // final kopija za anonimnu klasu

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(la);
                        Korisnik k = db.KorisnikDao().dohvatiKorisnika(ime);

                        if (k != null) {

                            String teloPoruke = "Poštovani "+k.getIme()+",\n\nVaša lozinka je: " + k.getLozinka() + "\n\nSrdačan pozdrav,\nG-Track admin";
                            new AsinhroniZadatak(
                                    "Dostavljanje zaboravljene lozinke",
                                    teloPoruke,
                                    k.getEmail(),
                                    la.sender,
                                    la
                            ).execute();


                        } else {
                            la.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(la, "Korisnik nije pronađen!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();

            }

            if (v.getId() == R.id.btnIzlazApp) {

                la.finish();

            }

            if (v.getId() == R.id.btnPrijava) {
                int position = la.korIme.getSelectedItemPosition();
                String pass = la.eLozinka.getText().toString().trim();

                if (position == 0 || pass.isEmpty()) {
                    Toast.makeText(la, "Izaberite korisnika i unesite lozinku", Toast.LENGTH_SHORT).show();
                } else {
                    String korisnikIme = la.korIme.getSelectedItem().toString();

                    // Provera korisnika i lozinke u bazi
                    AppDatabase db = AppDatabase.getInstance(la);
                    Korisnik k = db.KorisnikDao().login(korisnikIme, pass);

                    if (k != null) {
                        Toast.makeText(la, "Prijava uspešna za " + korisnikIme, Toast.LENGTH_SHORT).show();
                        la.OtvoriMainFormu();
                    } else {
                        Toast.makeText(la, "Pogrešna lozinka!", Toast.LENGTH_SHORT).show();
                    }
                }
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

                String ime = aa.eKorIme.getText().toString().trim();
                String lozinka = aa.eLozinka.getText().toString().trim();
                String lozinkaCheck = aa.eLozinkaCheck.getText().toString().trim();
                String mail = aa.eEmail.getText().toString().trim();

                if (ime.isEmpty() || lozinka.isEmpty() || mail.isEmpty()) {
                    Toast.makeText(aa, "Greška-nisu uneta sva polja!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lozinka.equals(lozinkaCheck)==false) {
                    Toast.makeText(aa, "Greška-lozinka i potvrda lozinke se ne poklapaju!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    Toast.makeText(aa, "Greška-unesite validnu email adresu", Toast.LENGTH_SHORT).show();
                    return;
                }


                AppDatabase db = AppDatabase.getInstance(aa);


                long result =  db.KorisnikDao().KreirajNalog(new Korisnik(ime, lozinka, mail));
                if (result == -1) {
                    Toast.makeText(aa, "Greška-korisnik:  '" + ime + "' već postoji!", Toast.LENGTH_LONG).show();
                    ToneGenerator toneGenError = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    toneGenError.startTone(ToneGenerator.TONE_PROP_NACK, 200); // kratki “error” beep
                }
                    else {

                    Toast.makeText(aa, "Nalog je uspešno kreiran!", Toast.LENGTH_SHORT).show();


                    // očisti polja
                    aa.eKorIme.setText("");
                    aa.eLozinka.setText("");
                    aa.eEmail.setText("");
                    aa.ucitajKorisnike();
                    aa.eLozinkaCheck.setText("");

                }



            }

        }

        if (lva != null) {

            if (v.getId() == R.id.btnIzlazLog) {
                lva.finish();

            }

        }
    }



}

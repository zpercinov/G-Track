package com.hemofarm.g_track.ui.login.mail;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsinhroniZadatak extends AsyncTask<String, Void, String> {

    String subject;
    String body;
    String recipient;
    String sender;
    byte[] csvData; // null znači plain text
    Context context;

    public String getRecipient() {
        return recipient;
    }

    // 🔹 Konstruktor za plain text
    public AsinhroniZadatak(String subject, String body, String recipient, String sender, Context context) {
        this.subject = subject;
        this.body = body;
        this.recipient = recipient;
        this.sender = sender;
        this.context = context;
        this.csvData = null; // ovde nema attachment-a
    }

    // 🔹 Konstruktor za CSV attachment
    public AsinhroniZadatak(String subject, String body, String recipient, String sender, Context context, byte[] csvData) {
        this.subject = subject;
        this.body = body;
        this.recipient = recipient;
        this.sender = sender;
        this.context = context;
        this.csvData = csvData;
    }

    @Override

    protected String doInBackground(String... strings) {
        try {
            GMailSender s = new GMailSender();
            if (csvData != null) {
                s.sendMailWithAttachment(subject, body, sender, recipient, csvData);
            } else {
                s.sendMail(subject, body, sender, recipient);
            }
            return "uspeh";
        } catch (Exception e) {
            System.out.println("Greska: " + e);
            return "greska";
        }

    }

    @Override
    protected void onPostExecute(String result) {

        if ("uspeh".equals(result)) {
            Toast.makeText(context, "Email je uspešno poslat na: "+getRecipient() , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Greška prilikom slanja mejla!", Toast.LENGTH_SHORT).show();
        }
    }
}





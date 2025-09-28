package com.hemofarm.g_track.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hemofarm.g_track.R;
import com.hemofarm.g_track.db.AppDatabase;
import com.hemofarm.g_track.db.Zapis;
import com.hemofarm.g_track.db.ZapisDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private List<Zapis> logovi;
    private OnItemClickListener listener;

    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Zapis log);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LogAdapter(Context context, List<Zapis> logovi) {
        this.context = context;
        this.logovi = logovi;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_log, parent, false);
        return new LogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        Zapis log = logovi.get(position);

        AppDatabase db = AppDatabase.getInstance(context);
        ZapisDao zapisDao = db.ZapisDao();
        String imeKorisnika = zapisDao.ucitajImeKorsnika(log.korisnikID);

        holder.tvOznaka.setText(log.oznaka);
        holder.tvKorisnik.setText(imeKorisnika);
        holder.tvOpisStavke.setText(log.opisStavke);

        String datumFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date(log.datumUnosa));
        holder.tvDatum.setText(datumFormat);

        // Klik na stavku
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(log);
            }
        });
    }

    @Override
    public int getItemCount() {
        return logovi.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvOznaka, tvKorisnik, tvDatum, tvOpisStavke;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOznaka = itemView.findViewById(R.id.textvOznaka);
            tvKorisnik = itemView.findViewById(R.id.textvKorisnik);
            tvDatum = itemView.findViewById(R.id.textvDatum);
            tvOpisStavke = itemView.findViewById(R.id.textvOpisStavke);
        }
    }
}

package com.hemofarm.g_track.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hemofarm.g_track.R;


import java.util.List;

public class StatistikaAdapter extends RecyclerView.Adapter<StatistikaAdapter.StatistikaViewHolder> {

    private List<StatistikaKorisnika> statistika;

    public StatistikaAdapter(List<StatistikaKorisnika> statistika) {
        this.statistika = statistika;
    }

    @NonNull
    @Override
    public StatistikaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_statistika, parent, false);
        return new StatistikaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StatistikaViewHolder holder, int position) {
        StatistikaKorisnika item = statistika.get(position);
        holder.tvKorisnik.setText(item.ime_korisnika);
        holder.tvBroj.setText(String.valueOf(item.broj));
    }

    @Override
    public int getItemCount() {
        return statistika.size();
    }

    public static class StatistikaViewHolder extends RecyclerView.ViewHolder {
        TextView tvKorisnik, tvBroj;

        public StatistikaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKorisnik = itemView.findViewById(R.id.textvStatKorisnik);
            tvBroj = itemView.findViewById(R.id.textvStatBroj);
        }
    }
}

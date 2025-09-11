package com.hemofarm.g_track.ui.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hemofarm.g_track.R;
import com.hemofarm.g_track.db.Korisnik;

import java.util.List;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<Korisnik> korisnici;

    public interface OnItemClickListener {
        void onItemClick(String korisnikIme);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public UserAdapter(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Korisnik korisnik = korisnici.get(position);
        holder.txtUsername.setText(korisnik.ime);
        holder.txtEmail.setText(korisnik.email);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(korisnik.ime);
            }
        });
    }

    @Override
    public int getItemCount() {
        return korisnici.size();
    }



    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView txtUsername;
        TextView txtEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtEmail =  itemView.findViewById(R.id.txtEmail);
        }
    }
}

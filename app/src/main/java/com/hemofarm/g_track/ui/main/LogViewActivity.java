package com.hemofarm.g_track.ui.main;

import android.os.Bundle;
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
import com.hemofarm.g_track.db.Log;
import com.hemofarm.g_track.util.Osluskivac;

import java.util.List;


public class LogViewActivity extends AppCompatActivity {

    RecyclerView recyclerLog;
    LogAdapter adapter;
    ImageButton bIzlaz;


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

        recyclerLog = findViewById(R.id.recyclerLog);
        recyclerLog.setLayoutManager(new LinearLayoutManager(this));

        // Učitaj podatke iz baze
        List<Log> logovi = AppDatabase.getInstance(this).LogDao().getAll();

        adapter = new LogAdapter(logovi);
        recyclerLog.setAdapter(adapter);

        bIzlaz = findViewById(R.id.btnIzlazLog);
        bIzlaz.setOnClickListener(new Osluskivac(this));

    }
}
package br.ucs.android.newsapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import br.ucs.android.newsapplication.R;

public class MainActivity extends AppCompatActivity {

    // Aqui vai a chave da API
    private final static String API_KEY = "16613c31e3b54b27bf64db1ba67bfe95";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "É necessário obter a chave da API themoviedb.org primeiro!", Toast.LENGTH_LONG).show();
            return;
        }
    }
}
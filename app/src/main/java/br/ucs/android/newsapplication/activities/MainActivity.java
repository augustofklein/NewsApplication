package br.ucs.android.newsapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.ucs.android.newsapplication.BuscarFragment;
import br.ucs.android.newsapplication.FavoritosFragment;
import br.ucs.android.newsapplication.R;
import br.ucs.android.newsapplication.database.BDSQLiteHelper;
import br.ucs.android.newsapplication.model.Artigo;
import br.ucs.android.newsapplication.model.Source;
import br.ucs.android.newsapplication.rest.ApiClient;
import br.ucs.android.newsapplication.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Aqui vai a chave da API
    private final static String API_KEY = "16613c31e3b54b27bf64db1ba67bfe95";

    public BottomNavigationView bnvMenu;
    public FragmentManager fragmentManager;

    private BDSQLiteHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd = new BDSQLiteHelper(this);

        bnvMenu = (BottomNavigationView) findViewById(R.id.bnvMenu);
        fragmentManager = getSupportFragmentManager();

        bnvMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_historico:
                        verifica_disponibilidade_aplicacao();
                        break;

                    case R.id.nav_favoritos:
                        verifica_disponibilidade_aplicacao();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fcvMain, FavoritosFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("favoritos") // name can be null
                                .commit();
                        break;

                    case R.id.nav_principal:
                        verifica_disponibilidade_aplicacao();
                        break;

                    case R.id.nav_buscar:
                        verifica_disponibilidade_aplicacao();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fcvMain, BuscarFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("buscar") // name can be null
                                .commit();
                        break;

                }

                return false;
            }
        });


        /*

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date();
        String dataFormatada = dateFormat.format(data);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "É necessário obter a chave da API https://newsapi.org/!", Toast.LENGTH_LONG).show();
            return;
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Artigo> call = apiService.getTopHeadLines("BR", "business", API_KEY);

        //Call<Artigo> call = apiService.getSearchByUser("tesla", dataFormatada, API_KEY);

        call.enqueue(new Callback<Artigo>() {
            @Override
            public void onResponse(Call<Artigo> call, Response<Artigo> response) {
                int statusCode = response.code();
                //List<Artigo> movies = response.body().getResults();
                //recyclerView.setAdapter(new NewsAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<Artigo> call, Throwable t) {
                mostraAlerta("Erro", t.toString());
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
        */

    }


    private void mostraAlerta(String titulo, String mensagem) {
        AlertDialog alertDialog = new
                AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensagem);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public void verifica_disponibilidade_aplicacao(){
        if (!verifica_conexao_mobile()){
            View view = getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(view, "Sem internet, aplicação em modo offline! ", Snackbar.LENGTH_LONG).show();
        }
    }

    public boolean verifica_conexao_mobile() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected() &&
                (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE ||
                        activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }
}
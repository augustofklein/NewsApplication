package br.ucs.android.newsapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.ucs.android.newsapplication.BuscarFragment;
import br.ucs.android.newsapplication.FavoritosFragment;
import br.ucs.android.newsapplication.HistoricoFragment;
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

        //Source source = bd.getSource(1);

        bnvMenu = (BottomNavigationView) findViewById(R.id.bnvMenu);
        fragmentManager = getSupportFragmentManager();

        bnvMenu.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {

                case R.id.nav_historico -> {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fcvMain, HistoricoFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("historico") // name can be null
                            .commit();
                    return true;
                }
                case R.id.nav_favoritos -> {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fcvMain, FavoritosFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("favoritos") // name can be null
                            .commit();
                    return true;
                }
                case R.id.nav_principal -> {
                    return false;
                }
                case R.id.nav_buscar -> {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fcvMain, BuscarFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("buscar") // name can be null
                            .commit();
                    return true;
                }
            }
            return true;
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
}
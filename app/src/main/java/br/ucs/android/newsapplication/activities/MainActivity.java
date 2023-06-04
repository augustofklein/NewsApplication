package br.ucs.android.newsapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ucs.android.newsapplication.BuscarFragment;
import br.ucs.android.newsapplication.FavoritosFragment;
import br.ucs.android.newsapplication.R;
import br.ucs.android.newsapplication.adapter.NewsAdapter;
import br.ucs.android.newsapplication.database.BDSQLiteHelper;
import br.ucs.android.newsapplication.model.Artigo;
import br.ucs.android.newsapplication.model.NewsResponse;
import br.ucs.android.newsapplication.model.Source;
import br.ucs.android.newsapplication.rest.ApiClient;
import br.ucs.android.newsapplication.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "16613c31e3b54b27bf64db1ba67bfe95";
    public BottomNavigationView bnvMenu;
    public FragmentManager fragmentManager;
    private BDSQLiteHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializa_bd_local();
        processa_carregamento_headlines();
        verifica_disponibilidade_aplicacao();

        bnvMenu = (BottomNavigationView) findViewById(R.id.bnvMenu);
        fragmentManager = getSupportFragmentManager();

        atualiza_headlines_bd_local();

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
                                .replace(R.id.articles_recycler_view, FavoritosFragment.class, null)
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
                                .replace(R.id.articles_recycler_view, BuscarFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("buscar") // name can be null
                                .commit();
                        break;

                }

                return false;
            }
        });
    }

    private void inicializa_bd_local(){
        bd = new BDSQLiteHelper(this);
    }

    private void atualiza_headlines_bd_local(){
        if(verifica_conexao_mobile()){
            bd.deletaTodasHeadLines();
            grava_headlines_bd_local();
        }
    }

    private void grava_headlines_bd_local(){
        Call<NewsResponse> call;

        call = retorna_dados_endpoint_headlines();

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                int statusCode = response.code();
                List<Artigo> artigos = response.body().getResults();

                for(int i = 0; i<artigos.size(); i++){
                    bd.addArtigo(artigos.get(i), 3);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                mostraAlerta("Erro", t.toString());
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    public void processa_carregamento_headlines(){
        if(verifica_conexao_mobile()){
            processa_carregamento_dados_online(retorna_dados_endpoint_headlines());
        } else {
            processa_carregamento_dados_offline(retorna_dados_headlines_bd());
        }
    }

    private Call<NewsResponse> retorna_dados_endpoint_headlines(){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsResponse> call = apiService.getTopHeadLines("BR", "business", API_KEY);

        return call;
    }

    private ArrayList<Artigo> retorna_dados_headlines_bd(){
        ArrayList<Artigo> artigos;
        artigos = bd.getAllHeadLineArticles();

        return artigos;
    }

    public void processa_carregamento_search(String pesquisa){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date();
        String dataFormatada = dateFormat.format(data);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsResponse> call = apiService.getSearchByUser(pesquisa, dataFormatada, API_KEY);

        processa_carregamento_dados_online(call);
    }

    public void processa_carregamento_dados_online(Call<NewsResponse> call){
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                int statusCode = response.code();
                List<Artigo> artigos = response.body().getResults();
                adicionaRegistroTela(artigos);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                mostraAlerta("Erro", t.toString());
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void processa_carregamento_dados_offline(ArrayList<Artigo> artigos){
        artigos = bd.getAllHeadLineArticles();
        adicionaRegistroTela(artigos);
    }

    private void adicionaRegistroTela(List<Artigo> artigos){
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.articles_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NewsAdapter(artigos, R.layout.item_registro, getApplicationContext()));
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
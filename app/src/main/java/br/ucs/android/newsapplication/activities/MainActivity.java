package br.ucs.android.newsapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ucs.android.newsapplication.R;
import br.ucs.android.newsapplication.adapter.FavoritoAdapter;
import br.ucs.android.newsapplication.adapter.HistoricoAdapter;
import br.ucs.android.newsapplication.adapter.ArtigoAdapter;
import br.ucs.android.newsapplication.database.BDSQLiteHelper;
import br.ucs.android.newsapplication.model.Artigo;
import br.ucs.android.newsapplication.model.Favorito;
import br.ucs.android.newsapplication.model.Historico;
import br.ucs.android.newsapplication.model.NewsResponse;
import br.ucs.android.newsapplication.model.WebViewClientImpl;
import br.ucs.android.newsapplication.rest.ApiClient;
import br.ucs.android.newsapplication.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "16613c31e3b54b27bf64db1ba67bfe95";
    public BottomNavigationView bnvMenu;

    private BDSQLiteHelper bd;

    private RecyclerView listaBuscar, listaFavoritos, listaHistorico, listaInicial;
    private LinearLayout camposBusca;
    private TextInputEditText campoBusca;
    private ImageButton botaoBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializa_bd_local();
        atualiza_headlines_bd_local();
        verifica_disponibilidade_aplicacao();
        processa_carregamento_headlines();

        bnvMenu = (BottomNavigationView) findViewById(R.id.bnvMenu);

        listaBuscar = (RecyclerView) findViewById(R.id.rvBuscar);
        listaInicial = (RecyclerView) findViewById(R.id.rvInicial);
        listaFavoritos = (RecyclerView) findViewById(R.id.rvFavoritos);
        listaHistorico = (RecyclerView) findViewById(R.id.rvHistorico);

        camposBusca = (LinearLayout) findViewById(R.id.llBuscar);
        campoBusca = (TextInputEditText) findViewById(R.id.tietBuscar);
        botaoBuscar = (ImageButton) findViewById(R.id.ibBuscar);

        botaoBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String termo = campoBusca.getText().toString();
                processa_carregamento_search(termo);
            }
        });


        atualiza_headlines_bd_local();

        bnvMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_historico:
                        listaHistorico.setVisibility(View.VISIBLE);
                        listaFavoritos.setVisibility(View.GONE);
                        listaInicial.setVisibility(View.GONE);
                        listaBuscar.setVisibility(View.GONE);
                        camposBusca.setVisibility(View.GONE);
                        atualiza_historico_bd_local();
                        break;

                    case R.id.nav_favoritos:
                        listaFavoritos.setVisibility(View.VISIBLE);
                        listaBuscar.setVisibility(View.GONE);
                        listaHistorico.setVisibility(View.GONE);
                        listaInicial.setVisibility(View.GONE);
                        camposBusca.setVisibility(View.GONE);
                        atualiza_favoritos_bd_local();
                        break;

                    case R.id.nav_principal:
                        verifica_disponibilidade_aplicacao();
                        listaInicial.setVisibility(View.VISIBLE);
                        listaFavoritos.setVisibility(View.GONE);
                        listaHistorico.setVisibility(View.GONE);
                        listaBuscar.setVisibility(View.GONE);
                        camposBusca.setVisibility(View.GONE);
                        break;

                    case R.id.nav_buscar:
                        verifica_disponibilidade_aplicacao();
                        if(verifica_conexao_mobile()) {
                            listaBuscar.setVisibility(View.VISIBLE);
                            camposBusca.setVisibility(View.VISIBLE);
                            listaFavoritos.setVisibility(View.GONE);
                            listaHistorico.setVisibility(View.GONE);
                            listaInicial.setVisibility(View.GONE);
                        }
                        else return false;

                }

                return true;
            }
        });

        listaInicial.setVisibility(View.VISIBLE);
        listaFavoritos.setVisibility(View.GONE);
        listaHistorico.setVisibility(View.GONE);
        listaBuscar.setVisibility(View.GONE);
        camposBusca.setVisibility(View.GONE);


        listaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getId();
            }
        });
    }

    public void onClickSelecao(View v){

        WebView myWebView = (WebView) findViewById(R.id.wvArtigo);

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);

        myWebView.setWebViewClient(webViewClient);

        myWebView.loadUrl("https://ava.ucs.br");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.setVisibility(View.VISIBLE);

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

    private void atualiza_historico_bd_local(){
        List<Historico> historico = bd.getAllHistorico();
        listaHistorico.setLayoutManager(new LinearLayoutManager(this));
        listaHistorico.setAdapter(new HistoricoAdapter(historico, R.layout.item_historico, this, bd));
    }

    private void grava_headlines_bd_local() {
        Call<NewsResponse> call;

        call = retorna_dados_endpoint_headlines();

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                int statusCode = response.code();
                List<Artigo> artigos = response.body().getResults();

                for (int i = 0; i < artigos.size(); i++) {
                    bd.addArtigo(artigos.get(i), 1);
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

    private void atualiza_favoritos_bd_local(){
        List<Favorito> favoritos = bd.getAllFavoritos();
        listaFavoritos.setLayoutManager(new LinearLayoutManager(this));
        listaFavoritos.setAdapter(new FavoritoAdapter(favoritos, R.layout.item_favoritos, this, bd));

    }

    public void processa_carregamento_headlines(){

        if(verifica_conexao_mobile()){
            processa_carregamento_dados_online(retorna_dados_endpoint_headlines(), R.id.rvInicial);
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
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String dataFormatada = dateFormat.format(cal.getTime());

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsResponse> call = apiService.getSearchByUser(pesquisa, dataFormatada, API_KEY);

        processa_carregamento_dados_online(call, R.id.rvBuscar);
    }

    public void processa_carregamento_dados_online(Call<NewsResponse> call, int idLayout){
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                int statusCode = response.code();
                List<Artigo> artigos = response.body().getResults();
                adicionaRegistroTela(artigos, idLayout);

                if(idLayout == R.id.rvBuscar)
                {
                    Historico historico = new Historico();
                    historico.setData(new Date());
                    historico.setTermo(campoBusca.getText().toString());
                    historico.setQuantidade(artigos.size());
                    bd.addHistorico(historico);
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

    private void processa_carregamento_dados_offline(ArrayList<Artigo> artigos){
        artigos = bd.getAllHeadLineArticles();
        adicionaRegistroTela(artigos, R.id.rvInicial);
    }

    private void adicionaRegistroTela(List<Artigo> artigos, int idLayout){
        final RecyclerView recyclerView = (RecyclerView) findViewById(idLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ArtigoAdapter(artigos, R.layout.item_registro, getApplicationContext(), bd));
    }

    private void mostraAlerta(String titulo, String mensagem) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
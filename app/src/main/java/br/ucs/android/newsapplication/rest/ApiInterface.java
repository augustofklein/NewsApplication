package br.ucs.android.newsapplication.rest;

import br.ucs.android.newsapplication.model.Artigo;
import br.ucs.android.newsapplication.model.Resposta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    // BUSCA GERAL DA PESQUISA DO USUÁRIO
    @GET("everything?sortBy=publishedAt")
    Call<Resposta> getSearchByUser(@Query("search") String search, @Query("data") String string, @Query("apiKey") String apiKey);

    // BUSCA DAS NOTÍCIAS DO MOMENTO
    @GET("top-headlines")
    Call<Resposta> getTopHeadLines(@Query("country") String country, @Query("category") String category, @Query("apiKey") String apiKey);

}

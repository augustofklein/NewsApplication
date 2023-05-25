package br.ucs.android.newsapplication.model;

import java.util.Date;

public class Favorito {

    private int id;
    private News news;
    private Date data;
    private String observacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Favorito() { }

    public Favorito(int id, News news, Date criadoEm, String observacao) {
        this.id = id;
        this.news = news;
        this.data = criadoEm;
        this.observacao = observacao;
    }
}

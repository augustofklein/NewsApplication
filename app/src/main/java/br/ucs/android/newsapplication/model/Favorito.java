package br.ucs.android.newsapplication.model;

import java.util.Date;

public class Favorito {

    private int id;
    private int idArtigo;
    private Artigo artigo;
    private Date data;
    private String observacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdArtigo() {
        return idArtigo;
    }

    public void setIdArtigo(int idArtigo) {
        this.idArtigo = idArtigo;
    }

    public Artigo getArtigo() {
        return artigo;
    }

    public void setArtigo(Artigo artigo) {
        this.artigo = artigo;
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

}

package br.ucs.android.newsapplication.model;

import java.util.ArrayList;
import java.util.Date;

public class Historico {

    private int id;

    private String termo;

    private Date data;

    private ArrayList<Artigo> resultados;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public ArrayList<Artigo> getResultados() {
        return resultados;
    }

    public void setResultados(ArrayList<Artigo> resultados) {
        this.resultados = resultados;
    }


}

package br.ucs.android.newsapplication.model;

import java.util.ArrayList;
import java.util.Date;

public class Historico {

    private int id;

    private String termo;

    private Date data;

    private ArrayList<News> resultados;

    public Historico() { }

    public Historico(int id, String termo, Date data, ArrayList<News> resultados) {
        this.id = id;
        this.termo = termo;
        this.data = data;
        this.resultados = resultados;
    }

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

    public ArrayList<News> getResultados() {
        return resultados;
    }

    public void setResultados(ArrayList<News> resultados) {
        this.resultados = resultados;
    }


}

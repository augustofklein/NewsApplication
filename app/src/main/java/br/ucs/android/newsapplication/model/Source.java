package br.ucs.android.newsapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {

    private int idSource;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public int getIdSource() {
        return idSource;
    }

    public void setIdSource(int id) {
        this.idSource = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(id == null || id.equals("null"))
            this.id = id ;
        else
            this.id = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
package br.ucs.android.newsapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;


import java.util.ArrayList;

import br.ucs.android.newsapplication.model.Artigo;
import br.ucs.android.newsapplication.model.Favorito;
import br.ucs.android.newsapplication.model.Historico;
import br.ucs.android.newsapplication.model.Source;

public class BDSQLiteHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NewsAppDB";

    private static final String TABELA_SOURCE = "source";
    private static final String TABELA_ARTIGO = "artigo";
    private static final String TABELA_FAVORITOS = "favorito";
    private static final String TABELA_HISTORICO = "historico";
    private static final String TABELA_HISTORICO_ARTIGO = "historico_artigo";

    private static final String ID = "id";

    private static final String SOURCE_ID = "source_id";
    private static final String SOURCE_NAME = "source_name";

    private static final String[] COLUNAS_SOURCE = {ID, SOURCE_ID, SOURCE_NAME};


    private static final String ARTIGO_SOURCE = "idSource";
    private static final String ARTIGO_AUTHOR = "author";
    private static final String ARTIGO_TITLE = "title";
    private static final String ARTIGO_DESCRIPTION = "description";
    private static final String ARTIGO_URL = "url";
    private static final String ARTIGO_URLTOIMAGE = "urlToImage";
    private static final String ARTIGO_PUBLISHEDAT = "publishedAt";
    private static final String ARTIGO_CONTENT = "content";
    private static final String[] COLUNAS_ARTIGO = {ID, ARTIGO_SOURCE, ARTIGO_AUTHOR, ARTIGO_TITLE,
            ARTIGO_DESCRIPTION, ARTIGO_URL, ARTIGO_URLTOIMAGE, ARTIGO_PUBLISHEDAT, ARTIGO_CONTENT};

    private static final String FAVORITOS_ARTIGO = "idArtigo";
    private static final String FAVORITOS_DATA = "data";
    private static final String FAVORITOS_OBSERVACAO = "observacao";

    private static final String[] COLUNAS_FAVORITOS = {ID, FAVORITOS_ARTIGO, FAVORITOS_DATA,
            FAVORITOS_OBSERVACAO};

    private static final String HISTORICO_DATA = "data";
    private static final String HISTORICO_TERMO = "termo";

    private static final String[] COLUNAS_HISTORICO = {ID, HISTORICO_DATA, HISTORICO_TERMO};

    private static final String HISTORICO_ARTIGO_ARTIGO = "idArtigo";
    private static final String HISTORICO_ARTIGO_HISTORICO = "idHistorico";

    private static final String[] COLUNAS_HISTORICO_ARTIGO = {HISTORICO_ARTIGO_ARTIGO, HISTORICO_ARTIGO_HISTORICO};


    public BDSQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABELA_SOURCE + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SOURCE_ID + " TEXT, " +
                SOURCE_NAME + " TEXT ) ";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABELA_ARTIGO + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ARTIGO_SOURCE + " INTEGER, " +
                ARTIGO_AUTHOR + " TEXT, " +
                ARTIGO_TITLE + " TEXT, " +
                ARTIGO_DESCRIPTION + " TEXT, " +
                ARTIGO_URL + " TEXT, " +
                ARTIGO_URLTOIMAGE + " TEXT, " +
                ARTIGO_PUBLISHEDAT + " TEXT, " +
                ARTIGO_CONTENT + " TEXT, "+
                "FOREIGN KEY ( " + ARTIGO_SOURCE + " )" +
                "REFERENCES " + TABELA_SOURCE + " ( " + ID + " ) " +
                "ON DELETE CASCADE " +
                "ON UPDATE NO ACTION)";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABELA_FAVORITOS + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FAVORITOS_ARTIGO + " INTEGER, " +
                FAVORITOS_DATA + " TEXT, " +
                FAVORITOS_OBSERVACAO + " TEXT, " +
                "FOREIGN KEY ( " + FAVORITOS_ARTIGO + " ) " +
                "REFERENCES " + TABELA_ARTIGO + " ( " + ID + " )" +
                "ON DELETE CASCADE " +
                "ON UPDATE NO ACTION) ";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABELA_HISTORICO + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HISTORICO_DATA + " TEXT, " +
                HISTORICO_TERMO + " TEXT ) ";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABELA_HISTORICO_ARTIGO + " ( " +
                HISTORICO_ARTIGO_ARTIGO + " INTEGER, " +
                HISTORICO_ARTIGO_HISTORICO + " INTEGER, " +
                "FOREIGN KEY ( " + HISTORICO_ARTIGO_ARTIGO + " ) " +
                "REFERENCES " + TABELA_ARTIGO + " ( " + ID + " ) " +
                "ON DELETE CASCADE " +
                "ON UPDATE NO ACTION, " +
                "FOREIGN KEY ( " + HISTORICO_ARTIGO_HISTORICO + " ) " +
                "REFERENCES " + TABELA_HISTORICO + " ( " + ID +  " ) " +
                "ON DELETE CASCADE " +
                "ON UPDATE NO ACTION ) ";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_SOURCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ARTIGO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_FAVORITOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_HISTORICO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_HISTORICO_ARTIGO);
        this.onCreate(db);
    }

    public void addArtigo(Artigo article) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ARTIGO_SOURCE, article.getSource().getId());
        values.put(ARTIGO_AUTHOR, article.getAuthor());
        values.put(ARTIGO_TITLE, article.getTitle());
        values.put(ARTIGO_DESCRIPTION, article.getDescription());
        values.put(ARTIGO_URL, article.getUrl());
        values.put(ARTIGO_URLTOIMAGE, article.getUrlToImage());
        values.put(ARTIGO_PUBLISHEDAT, article.getPublishedAt());
        values.put(ARTIGO_CONTENT, article.getContent());
        db.insert(TABELA_ARTIGO, null, values);
        db.close();
    }

    public void addFavorito(Favorito favorito) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVORITOS_ARTIGO, favorito.getArtigo().getId());
        values.put(FAVORITOS_DATA, favorito.getData().toLocaleString());
        values.put(FAVORITOS_OBSERVACAO, favorito.getObservacao());
        db.insert(TABELA_FAVORITOS, null, values);
        db.close();
    }
    public void addHistorico(Historico historico) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HISTORICO_DATA, historico.getData().toLocaleString());
        values.put(HISTORICO_TERMO, historico.getTermo());
        db.insert(TABELA_HISTORICO, null, values);
        db.close();
    }

    public Source getSource(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_SOURCE, // a. tabela
                COLUNAS_SOURCE, // b. colunas
                " id = ?", // c. colunas para comparar
                new String[] { String.valueOf(id) }, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Source source = cursorToSource(cursor);
            return source;
        }
    }

    public Artigo getArtigo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_ARTIGO, // a. tabela
                COLUNAS_ARTIGO, // b. colunas
                " id = ?", // c. colunas para comparar
                new String[] { String.valueOf(id) }, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Artigo artigo = cursorToArtigo(cursor);
            artigo.setSource(getSource(artigo.getIdSource()));
            return artigo;
        }
    }

    public Favorito getFavorito(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_FAVORITOS, // a. tabela
                COLUNAS_FAVORITOS, // b. colunas
                " id = ?", // c. colunas para comparar
                new String[] { String.valueOf(id) }, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Favorito favorito = cursorToFavorito(cursor);
            favorito.setArtigo(getArtigo(favorito.getIdArtigo()));
            return favorito;
        }
    }
    private Source cursorToSource(Cursor cursor) {
        Source source = new Source();
        source.setId(cursor.getString(0));
        source.setSource_id(cursor.getString(1));
        source.setSource_name(cursor.getString(2));
        return source;
    }
    private Artigo cursorToArtigo(Cursor cursor) {
        Artigo artigo = new Artigo();
        artigo.setId(Integer.parseInt(cursor.getString(0)));
        return artigo;
    }

    private Favorito cursorToFavorito(Cursor cursor) {
        Favorito favorito = new Favorito();
        favorito.setId(Integer.parseInt(cursor.getString(0)));
        favorito.setIdArtigo(Integer.parseInt(cursor.getString(1)));
        //favorito.setData((cursor.getString(2)));
        favorito.setObservacao(cursor.getString(3));
        return favorito;
    }

    private Historico cursorToHistorico(Cursor cursor) {
        Historico historico = new Historico();
        historico.setId(Integer.parseInt(cursor.getString(0)));
        return historico;
    }

    public ArrayList<Artigo> getAllArtigos() {
        ArrayList<Artigo> listaNews = new ArrayList<Artigo>();
        String query = "SELECT * FROM " + TABELA_ARTIGO
                + " ORDER BY " + ARTIGO_TITLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Artigo artigo = cursorToArtigo(cursor);
                listaNews.add(artigo);
            } while (cursor.moveToNext());
        }
        return listaNews;
    }

    public ArrayList<Favorito> getAllFavoritos() {
        ArrayList<Favorito> listaFavoritos = new ArrayList<Favorito>();
        String query = "SELECT * FROM " + TABELA_FAVORITOS
                + " ORDER BY " + FAVORITOS_DATA + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Favorito favorito = cursorToFavorito(cursor);
                listaFavoritos.add(favorito);
            } while (cursor.moveToNext());
        }
        return listaFavoritos;
    }

    public ArrayList<Historico> getAllHistorico() {
        ArrayList<Historico> listaHistorico = new ArrayList<Historico>();
        String query = "SELECT * FROM " + TABELA_HISTORICO
                + " ORDER BY " + HISTORICO_DATA + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Historico historico = cursorToHistorico(cursor);
                listaHistorico.add(historico);
            } while (cursor.moveToNext());
        }
        return listaHistorico;
    }

    public int updateNews(Artigo article) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int i = db.update(TABELA_ARTIGO, //tabela
                values, // valores
                ID + " = ?", // colunas para comparar
                new String[]
                        { String.valueOf(article.getId()) }); //parâmetros
        db.close();
        return i; // número de linhas modificadas
    }

    public int deleteArtigo(Artigo article) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_ARTIGO, //tabela
                ID + " = ?", new String[] { String.valueOf(article.getId()) });
        db.close();
        return i; // número de linhas excluídas
    }

    public int deleteFavoritos() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_FAVORITOS, //tabela
                ID + " <> ?", new String[] { "0" });
        db.close();
        return i;
    }

    public int deleteHistorico() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_HISTORICO, //tabela
                ID + " <> ?", new String[] { "0"});
        db.close();
        return i;
    }

}


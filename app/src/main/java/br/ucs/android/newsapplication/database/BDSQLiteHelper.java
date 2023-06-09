package br.ucs.android.newsapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;


import java.util.ArrayList;
import java.util.Date;

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
    private static final String ARTIGO_TIPO = "tipo";
    private static final String ARTIGO_AUTHOR = "author";
    private static final String ARTIGO_TITLE = "title";
    private static final String ARTIGO_DESCRIPTION = "description";
    private static final String ARTIGO_URL = "url";
    private static final String ARTIGO_URLTOIMAGE = "urlToImage";
    private static final String ARTIGO_PUBLISHEDAT = "publishedAt";
    private static final String ARTIGO_CONTENT = "content";
    private static final String[] COLUNAS_ARTIGO = {ID, ARTIGO_TIPO, ARTIGO_SOURCE, ARTIGO_AUTHOR,
        ARTIGO_TITLE, ARTIGO_DESCRIPTION, ARTIGO_URL, ARTIGO_URLTOIMAGE, ARTIGO_PUBLISHEDAT, ARTIGO_CONTENT};

    private static final String FAVORITOS_ARTIGO = "idArtigo";
    private static final String FAVORITOS_DATA = "data";
    private static final String FAVORITOS_OBSERVACAO = "observacao";

    private static final String[] COLUNAS_FAVORITOS = {ID, FAVORITOS_ARTIGO, FAVORITOS_DATA,
            FAVORITOS_OBSERVACAO};

    private static final String HISTORICO_DATA = "data";
    private static final String HISTORICO_TERMO = "termo";
    private static final String HISTORICO_RESULTADOS = "resultados";

    private static final String[] COLUNAS_HISTORICO = {ID, HISTORICO_DATA, HISTORICO_TERMO, HISTORICO_RESULTADOS};

   /*
    private static final String HISTORICO_ARTIGO_ARTIGO = "idArtigo";
    private static final String HISTORICO_ARTIGO_HISTORICO = "idHistorico";
    private static final String[] COLUNAS_HISTORICO_ARTIGO = {HISTORICO_ARTIGO_ARTIGO, HISTORICO_ARTIGO_HISTORICO};
    */

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
                ARTIGO_TIPO + " INTEGER, " +
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
                HISTORICO_TERMO + " TEXT, " +
                HISTORICO_RESULTADOS + " INTEGER ) ";
        db.execSQL(CREATE_TABLE);

        /*CREATE_TABLE = "CREATE TABLE " + TABELA_HISTORICO_ARTIGO + " ( " +
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
        db.execSQL(CREATE_TABLE);*/
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_SOURCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ARTIGO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_FAVORITOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_HISTORICO);
        //db.execSQL("DROP TABLE IF EXISTS " + TABELA_HISTORICO_ARTIGO);
        this.onCreate(db);
    }

    public long addSource(Source source)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SOURCE_ID, source.getId());
        values.put(SOURCE_NAME, source.getName());
        long id = db.insert(TABELA_SOURCE, null, values);
        db.close();
        return id;
    }
    public long addArtigo(Artigo article, int tipo) {

        if(article.getIdSource() == 0) {
            Source source = getSource(article.getSource().getName());
            if(source != null) {
                article.setIdSource(source.getIdSource());
                article.getSource().setIdSource(source.getIdSource());
            }
            else {
                long id = addSource(article.getSource());
                article.setIdSource((int) id);
                article.getSource().setIdSource((int) id);
            }
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ARTIGO_TIPO, tipo);
        values.put(ARTIGO_SOURCE, article.getIdSource());
        values.put(ARTIGO_AUTHOR, article.getAuthor());
        values.put(ARTIGO_TITLE, article.getTitle());
        values.put(ARTIGO_DESCRIPTION, article.getDescription());
        values.put(ARTIGO_URL, article.getUrl());
        values.put(ARTIGO_URLTOIMAGE, article.getUrlToImage());
        values.put(ARTIGO_PUBLISHEDAT, article.getPublishedAt());
        values.put(ARTIGO_CONTENT, article.getContent());
        long id = db.insert(TABELA_ARTIGO, null, values);
        db.close();
        return id;
    }

    public ArrayList<Artigo> getAllHeadLineArticles(){

        ArrayList<Artigo> listaArtigos = new ArrayList<Artigo>();

        String query = "SELECT *" +
                       "  FROM " + TABELA_ARTIGO +
                       " WHERE " + ARTIGO_TIPO + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Artigo artigo = cursorToArtigo(cursor);
                artigo.setSource(getSource(artigo.getIdSource()));
                listaArtigos.add(artigo);
            } while (cursor.moveToNext());
        }

        return listaArtigos;
    }

    public void deletaTodasHeadLines(){
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABELA_ARTIGO,
                ARTIGO_TIPO + " = ?",
                new String[] { String.valueOf(1) });
        db.close();
    }

    public ArrayList<Artigo> getAllFavoritesArticles(){

        ArrayList<Artigo> listaArtigos = new ArrayList<Artigo>();

        String query = "SELECT *" +
                       "  FROM " + TABELA_ARTIGO +
                       " WHERE " + ARTIGO_TIPO + " = 2";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Artigo artigo = cursorToArtigo(cursor);
                listaArtigos.add(artigo);
            } while (cursor.moveToNext());
        }

        return listaArtigos;
    }

    public void deletaTodasFavoritas(){
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABELA_ARTIGO,
                ARTIGO_TIPO + " = ?",
                new String[] { String.valueOf(2) });
        db.close();
    }

    public void addFavorito(Favorito favorito) {

        if(favorito.getIdArtigo() == 0) {
            favorito.setIdArtigo((int) addArtigo(favorito.getArtigo(), 3));
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVORITOS_ARTIGO, favorito.getIdArtigo());
        values.put(HISTORICO_DATA, favorito.getData().getTime());
        values.put(FAVORITOS_OBSERVACAO, favorito.getObservacao());
        db.insert(TABELA_FAVORITOS, null, values);
        db.close();


    }
    
    public void addHistorico(Historico historico) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HISTORICO_DATA, historico.getData().getTime());
        values.put(HISTORICO_TERMO, historico.getTermo());
        values.put(HISTORICO_RESULTADOS, historico.getQuantidade());
        historico.setId((int) db.insert(TABELA_HISTORICO, null, values));
        db.close();

       /* for (var artigo : historico.getResultados()) {
            artigo.setId((int) addArtigo(artigo, 2));
            addArtigoHistorico(artigo.getId(), historico.getId());
        }*/
    }



    /*private void addArtigoHistorico(int idArtigo, int idHistorico)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HISTORICO_ARTIGO_ARTIGO, idArtigo);
        values.put(HISTORICO_ARTIGO_HISTORICO, idHistorico);
        db.insert(TABELA_HISTORICO_ARTIGO, null, values);
        db.close();
    }*/

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

    public Source getSource(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABELA_SOURCE
                + " WHERE " + SOURCE_NAME + " = '" + name + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            Source source = cursorToSource(cursor);
            return source;
        }
        return null;

        /*
        Cursor cursor = db.query(TABELA_SOURCE, // a. tabela
                COLUNAS_SOURCE, // b. colunas
                " source_name = ?", // c. colunas para comparar
                new String[] { name }, // d. parâmetros
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
        }*/
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
        source.setIdSource(Integer.parseInt(cursor.getString(0)));
        source.setId(cursor.getString(1));
        source.setName(cursor.getString(2));
        return source;
    }
    private Artigo cursorToArtigo(Cursor cursor) {
        Artigo artigo = new Artigo();
        artigo.setId(Integer.parseInt(cursor.getString(0)));
        artigo.setIdSource(cursor.getInt(2));
        artigo.setAuthor(cursor.getString(3));
        artigo.setTitle(cursor.getString(4));
        artigo.setDescription(cursor.getString(5));
        artigo.setUrl(cursor.getString(6));
        artigo.setUrlToImage(cursor.getString(7));
        artigo.setPublishedAt(cursor.getString(8));
        artigo.setContent(cursor.getString(9));

        return artigo;
    }

    private Favorito cursorToFavorito(Cursor cursor) {
        Favorito favorito = new Favorito();
        favorito.setId(Integer.parseInt(cursor.getString(0)));
        favorito.setIdArtigo(Integer.parseInt(cursor.getString(1)));
        favorito.setData(new Date(Long.parseLong(cursor.getString(2))));
        favorito.setObservacao(cursor.getString(3));
        return favorito;
    }

    private Historico cursorToHistorico(Cursor cursor) {
        Historico historico = new Historico();
        historico.setId(Integer.parseInt(cursor.getString(0)));
        historico.setData(new Date(Long.parseLong(cursor.getString(1))));
        historico.setTermo(cursor.getString(2));
        historico.setQuantidade(cursor.getInt(3));
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
                + " ORDER BY " + ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Favorito favorito = cursorToFavorito(cursor);
                favorito.setArtigo(getArtigo(favorito.getIdArtigo()));
                listaFavoritos.add(favorito);
            } while (cursor.moveToNext());
        }
        return listaFavoritos;
    }

    public ArrayList<Historico> getAllHistorico() {
        ArrayList<Historico> listaHistorico = new ArrayList<Historico>();
        String query = "SELECT * FROM " + TABELA_HISTORICO
                + " ORDER BY " + ID + " DESC";
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

    public int updateFavorito(Favorito favorito) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVORITOS_OBSERVACAO, favorito.getObservacao());
        int i = db.update(TABELA_FAVORITOS, //tabela
                values, // valores
                ID + " = ?", // colunas para comparar
                new String[] { String.valueOf(favorito.getId()) }); //parâmetros
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
    public int deleteFavorito(Favorito favorito) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_FAVORITOS, //tabela
                ID + " = ?", new String[] { String.valueOf(favorito.getId()) });
        db.close();
        return i; // número de linhas excluídas
    }

    public int deleteHistorico(Historico historico) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_HISTORICO, //tabela
                ID + " = ?", new String[] { String.valueOf(historico.getId()) });
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


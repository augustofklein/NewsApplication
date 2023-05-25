package br.ucs.android.newsapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;


import java.util.ArrayList;

import br.ucs.android.newsapplication.model.Favorito;
import br.ucs.android.newsapplication.model.Historico;
import br.ucs.android.newsapplication.model.News;

public class BDSQLiteHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NewsApplication_DB";
    private static final String TABELA_NEWS = "news";
    private static final String TABELA_FAVORITOS = "favoritos";
    private static final String TABELA_HISTORICO = "historico";
    private static final String ID = "id";

    private static final String NEWS_SOURCE = "source";
    private static final String NEWS_TITLE = "title";
    private static final String NEWS_AUTHOR = "author";
    private static final String[] COLUNAS_NEWS = {ID, NEWS_SOURCE, NEWS_AUTHOR, NEWS_TITLE};

    private static final String FAVORITOS_DATA = "data";
    private static final String FAVORITOS_OBSERVACAO = "observacao";

    private static final String[] COLUNAS_FAVORITOS = {ID, FAVORITOS_DATA, FAVORITOS_OBSERVACAO};

    private static final String HISTORICO_DATA = "data";
    private static final String HISTORICO_TERMO = "termo";

    private static final String[] COLUNAS_HISTORICO = {ID, HISTORICO_DATA, HISTORICO_TERMO};



    public BDSQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ TABELA_NEWS +" ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                NEWS_SOURCE + " TEXT,"+
                NEWS_AUTHOR + " TEXT,"+
                NEWS_TITLE + " TEXT,"+
                "ano INTEGER)";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE "+ TABELA_FAVORITOS +" ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                FAVORITOS_DATA + " DATETIME,"+
                FAVORITOS_OBSERVACAO + " TEXT)";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE "+ TABELA_HISTORICO +" ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                HISTORICO_DATA + " DATETIME,"+
                HISTORICO_TERMO + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_FAVORITOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_HISTORICO);
        this.onCreate(db);
    }

    public void addNews(News news) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NEWS_SOURCE, news.getSource());
        values.put(NEWS_AUTHOR, news.getAuthor());
        values.put(NEWS_TITLE, news.getTitle());
        db.insert(TABELA_NEWS, null, values);
        db.close();
    }

    public void addFavorito(Favorito favorito) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVORITOS_DATA, favorito.getData().toString());
        values.put(FAVORITOS_OBSERVACAO, favorito.getObservacao());
        db.insert(TABELA_FAVORITOS, null, values);
        db.close();
    }
    public void addHistorico(Historico historico) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HISTORICO_DATA, historico.getData().toString());
        values.put(HISTORICO_TERMO, historico.getTermo());
        db.insert(TABELA_HISTORICO, null, values);
        db.close();
    }

    public News getNews(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_NEWS, // a. tabela
                COLUNAS_NEWS, // b. colunas
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
            News news = cursorToNews(cursor);
            return news;
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
            return favorito;
        }
    }

    private News cursorToNews(Cursor cursor) {
        News news = new News();
        news.setId(Integer.parseInt(cursor.getString(0)));
        return news;
    }

    private Favorito cursorToFavorito(Cursor cursor) {
        Favorito favorito = new Favorito();
        favorito.setId(Integer.parseInt(cursor.getString(0)));
        return favorito;
    }

    private Historico cursorToHistorico(Cursor cursor) {
        Historico historico = new Historico();
        historico.setId(Integer.parseInt(cursor.getString(0)));
        return historico;
    }

    public ArrayList<News> getAllNews() {
        ArrayList<News> listaNews = new ArrayList<News>();
        String query = "SELECT * FROM " + TABELA_NEWS
                + " ORDER BY " + NEWS_TITLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                News news = cursorToNews(cursor);
                listaNews.add(news);
            } while (cursor.moveToNext());
        }
        return listaNews;
    }

    public ArrayList<Favorito> getAllFavoritos() {
        ArrayList<Favorito> listaFavoritos = new ArrayList<Favorito>();
        String query = "SELECT * FROM " + TABELA_FAVORITOS
                + " ORDER BY " + NEWS_TITLE;
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
                + " ORDER BY " + NEWS_TITLE;
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

    public int updateNews(News news) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int i = db.update(TABELA_NEWS, //tabela
                values, // valores
                ID + " = ?", // colunas para comparar
                new String[]
                        { String.valueOf(news.getId()) }); //parâmetros
        db.close();
        return i; // número de linhas modificadas
    }

    public int deleteNews(News news) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_NEWS, //tabela
                ID + " = ?", new String[] { String.valueOf(news.getId()) });
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


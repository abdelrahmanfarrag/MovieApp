package com.example.mana.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abdelrahman on 8/21/2016.
 */
public class Offline extends SQLiteOpenHelper {
    private static final String DB_NAME ="MOVIES";
    private static final String TABLE_NAME="FAVOURITES";
    private static final int DB_VERSION=4;
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT , POSTER_PATH TEXT , OVERVIEW TEXT , MOVIE_ID INTEGER NOT NULL UNIQUE , TITLE TEXT , VOTE_AVERAGE INTEGER , DATE TEXT  );";
    private static final String DROP_TABLE = " DROP TABLE IF EXISTS "+ TABLE_NAME;

    public Offline(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA automatic_index = off;");
        }
    }
    public long insert_movie(String post_path,String over_view ,int Movie_id, String title, double rate,String date)

    {SQLiteDatabase sd =getReadableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("POSTER_PATH",post_path);
        cv.put("OVERVIEW",over_view);
        cv.put("MOVIE_ID",Movie_id);
        cv.put("TITLE",title);
        cv.put("VOTE_AVERAGE",rate);
        cv.put("DATE", date);

        long result = sd.insert(TABLE_NAME,null,cv);
        return result;

    }
    public Cursor getData(SQLiteDatabase db)
    {
    Cursor c;

        String[] Columns = {"POSTER_PATH","OVERVIEW","MOVIE_ID","TITLE","VOTE_AVERAGE","DATE"};
        c=db.query(TABLE_NAME,Columns,null,null,null,null,null);
        return c;
    }
}

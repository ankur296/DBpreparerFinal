package com.example.dbpreparerfinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.dbpreparerfinal.MovieContract.MovieEntry;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MovieDbAssetHelper extends SQLiteAssetHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1 ; 
    public static final String DATABASE_NAME = "Movies.db"; 
    
    private static final String TEXT_TYPE = " TEXT"; 
    private static final String COMMA_SEP = ",";
       
    private static final String SQL_CREATE_ENTRIES =
    
    		"CREATE TABLE " + MovieEntry.TABLE_NAME + " (" + 
    				 MovieEntry._ID + " INTEGER PRIMARY KEY," + 
    			        MovieEntry.COLUMN_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
    			        MovieEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
    			        MovieEntry.COLUMN_VOTE_COUNT + TEXT_TYPE + COMMA_SEP +
    			        MovieEntry.COLUMN_STAR_CAST + TEXT_TYPE +
        " )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    public MovieDbAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(SQL_CREATE_ENTRIES);
//    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onUpgrade(db, oldVersion, newVersion);
//    }
}

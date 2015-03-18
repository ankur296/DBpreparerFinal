package com.example.dbpreparerfinal;

import android.provider.BaseColumns;

public final class MovieContract  {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public MovieContract() {}

    /* Inner class that defines the table contents */ 
    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_VOTE_COUNT = "votecount";
        public static final String COLUMN_STAR_CAST = "starcast";
        public static final String COLUMN_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_CHARACTER = "character";
    }
}
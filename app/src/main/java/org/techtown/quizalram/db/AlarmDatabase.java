package org.techtown.quizalram.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.techtown.quizalram.main.MainActivity;

public class AlarmDatabase {

    private static final String TAG = "1717";

    private static AlarmDatabase database;
    public static String DATABASE_NAME = "ALARMDATABASE";
    public static String TABLE_ALARM = "ALARM";
    public static String TABLE_QUIZ = "QUIZ";
    public static String TABLE_BACK = "BACK";
    public static int DATABASE_VERSION = 1;

    private static DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    private AlarmDatabase(Context context){
        this.context = context;
    }

    public static AlarmDatabase getInstance(Context context){
        if (database == null){
            database = new AlarmDatabase(context);
        }
        return database;
    }

    public boolean open(){
        println("opening database [" + DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close(){
        println("closing database [" + DATABASE_NAME + "].");

        db.close();

        database = null;
    }

    public Cursor rawQuery(String SQL){
        println("\nexecuteQuery called.\n");

        if (db == null){
            open();
        }

        Cursor cursor = null;

        try{
            cursor = db.rawQuery(SQL, null);
            println("cursor count : " + cursor.getCount());
        } catch(Exception ex){
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return cursor;

    }

    public boolean execSQL(String SQL){
        println("\nexecute called.\n");

        try{
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex){
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }
        return true;
    }

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            println("creating database [" + DATABASE_NAME + "].");

            String DROP_SQL = "drop table if exists " + TABLE_ALARM;
            try {
                db.execSQL(DROP_SQL);
            } catch(Exception ex){
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String DROP_SQL2 = "drop table if exists " + TABLE_QUIZ;
            try {
                db.execSQL(DROP_SQL2);
            } catch(Exception ex){
                Log.e(TAG, "Exception in DROP_SQL2", ex);
            }

            String DROP_SQL3 = "drop table if exists " + TABLE_BACK;
            try {
                db.execSQL(DROP_SQL3);
            } catch(Exception ex){
                Log.e(TAG, "Exception in DROP_SQL3", ex);
            }

            String CREATE_SQL = "create table " + TABLE_ALARM + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " HOUR INTEGER DEFAULT '', "
                    + " MINUTE INTEGER DEFAULT '', "
                    + " LABEL TEXT DEFAULT '', "
                    + " REPEATEDAY TEXT DEFAULT '', "
                    + " RINGTONE TEXT DEFAULT '', "
                    + " VIBRATOR TEXT DEFAULT '', "
                    + " TURN TEXT DEFAULT '' " + ")";

            String CREATE_SQL2 = "create table " + TABLE_QUIZ + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " TITLE TEXT DEFAULT '', "
                    + " QUESTION TEXT DEFAULT '', "
                    + " ANSWER TEXT DEFAULT '', "
                    + " VALUE TEXT DEFAULT '' " + ")";

            String CREATE_SQL3 = "create table " + TABLE_BACK + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " TOP_BACKGROUND TEXT DEFAULT '', "
                    + " BASE_BACKGROUND TEXT DEFAULT '', "
                    + " TOP_TEXTCOLOR TEXT DEFAULT '', "
                    + " BASE_TEXTCOLOR TEXT DEFAULT '', "
                    + " BASE_BACKGROUND_BITMAP BLOB DEFAULT '' " + ")";

            try {
                db.execSQL(CREATE_SQL);
            } catch(Exception ex){
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            try {
                db.execSQL(CREATE_SQL2);
            } catch(Exception ex){
                Log.e(TAG, "Exception in CREATE_SQL2", ex);
            }

            try {
                db.execSQL(CREATE_SQL3);
            } catch(Exception ex){
                Log.e(TAG, "Exception in CREATE_SQL3", ex);
            }

            String CREATE_INDEX_SQL = "create index " + TABLE_ALARM + "_IDX ON " + TABLE_ALARM + "(" + "_id" + ")";
            String CREATE_INDEX_SQL2 = "create index " + TABLE_QUIZ + "_IDX ON " + TABLE_QUIZ + "(" + "_id" + ")";
            String CREATE_INDEX_SQL3 = "create index " + TABLE_BACK + "_IDX ON " + TABLE_BACK + "(" + "_id" + ")";

            try {
                db.execSQL(CREATE_INDEX_SQL);
            } catch(Exception ex){
                Log.e(TAG, "Exception in CREATE_INDEX_SQL", ex);
            }

            try {
                db.execSQL(CREATE_INDEX_SQL2);
            } catch(Exception ex){
                Log.e(TAG, "Exception in CREATE_INDEX_SQL2", ex);
            }

            try {
                db.execSQL(CREATE_INDEX_SQL3);
            } catch(Exception ex){
                Log.e(TAG, "Exception in CREATE_INDEX_SQL3", ex);
            }

        }

        public void onOpen(SQLiteDatabase db){
            println("opened database [" + DATABASE_NAME + "].");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }

    }

    public static void println(String data){
        Log.d(TAG, data);
    }

    public static void addEntry(byte[] bytes){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("BASE_BACKGROUND", "");
        cv.put("BASE_BACKGROUND_BITMAP", bytes);
        db.update(TABLE_BACK, cv, null, null);
    }

}

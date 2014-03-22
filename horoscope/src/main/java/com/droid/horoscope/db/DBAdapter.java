package com.droid.horoscope.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.droid.horoscope.constants.Constants;
import com.droid.horoscope.model.HoroscopeText;
import com.droid.horoscope.model.Horoscopes;

import java.util.ArrayList;


/**
 * Created by james on 21/03/14.
 */
public class DBAdapter {

    private static final String LOG_TAG = "dbAdapter";

    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public DBAdapter open(){
        db = dbHelper.getWritableDatabase();

        //force foreign key checks
        try{
            db.execSQL(Constants.FORCE_FOREIGN_KEY_CHECKS);
        }catch (NullPointerException npe){
            Log.e(LOG_TAG, "foreign key checks:: " + npe.getMessage());
        }
        return this;
    }

    public void close(){
        db.close();
    }

    //get all horoscopes
    public ArrayList<Horoscopes> getHoroscopeList(){
        ArrayList<Horoscopes> horoscopeList = new ArrayList<Horoscopes>();
        String[] cols = {Horoscopes.HOROSCOPE_ID, Horoscopes.HOROSCOPE_NAME, Horoscopes.HOROSCOPE_DATE};
        Cursor cursor;

        try{
            cursor = db.query(Constants.TBL_HOROSCOPES, cols, null, null, null, null, null);
        }catch (SQLiteException ex){
            Log.e(LOG_TAG, "exception "+ex.getMessage());
            return horoscopeList;
        }
        if(cursor.moveToFirst()){
            do{
                Horoscopes horoscope = new Horoscopes();

                horoscope.setHoroscopeID(cursor.getInt(cursor.getColumnIndex(Horoscopes.HOROSCOPE_ID)));
                horoscope.setHoroscopeName(cursor.getString(cursor.getColumnIndex(Horoscopes.HOROSCOPE_NAME)));
                horoscope.setHoroscopeDate(cursor.getString(cursor.getColumnIndex(Horoscopes.HOROSCOPE_DATE)));

                horoscopeList.add(horoscope);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return horoscopeList;
    }

    //get specific Horoscope
    public ArrayList<Horoscopes> getHoroscope(int horoscopeID){
        ArrayList<Horoscopes> horoscopeList = new ArrayList<Horoscopes>();
        String[] cols = {Horoscopes.HOROSCOPE_ID, Horoscopes.HOROSCOPE_NAME, Horoscopes.HOROSCOPE_DATE};
        Cursor cursor;

        try {
            cursor = db.query(Constants.TBL_HOROSCOPES, cols, Horoscopes.HOROSCOPE_ID+" = "+horoscopeID, null, null, null, null);
        }catch (SQLiteException ex){
            Log.e(LOG_TAG, "exception "+ex.getMessage());
            return horoscopeList;
        }

        if(cursor.moveToFirst()){

            Horoscopes horoscope = new Horoscopes();
            horoscope.setHoroscopeID(cursor.getInt(cursor.getColumnIndex(Horoscopes.HOROSCOPE_ID)));
            horoscope.setHoroscopeName(cursor.getString(cursor.getColumnIndex(Horoscopes.HOROSCOPE_NAME)));
            horoscope.setHoroscopeDate(cursor.getString(cursor.getColumnIndex(Horoscopes.HOROSCOPE_DATE)));

            horoscopeList.add(horoscope);
        }
        cursor.close();
        return horoscopeList;
    }

    //get horoscope text
    public ArrayList<HoroscopeText> getHoroscopeText(int horoscopeID, String date){
        ArrayList<HoroscopeText> horoscopeTextList = new ArrayList<HoroscopeText>();
        Cursor cursor;
        String sql = "SELECT text_id, horoscope_id, text_date, text, text_url " +
            "FROM horoscope_text WHERE horoscope_id = "+horoscopeID+" AND datetime(text_date) = datetime('"+date+"')";

        try {
            cursor = db.rawQuery(sql, null);
        }catch (SQLiteException ex){
            Log.e(LOG_TAG, "exception "+ex.getMessage());
            return horoscopeTextList;
        }

        if(cursor.moveToFirst()){
            do{
                HoroscopeText horoscopeText = new HoroscopeText();

                horoscopeText.setTextID(cursor.getInt(cursor.getColumnIndex(HoroscopeText.TEXT_ID)));
                horoscopeText.setHoroscopeID(cursor.getInt(cursor.getColumnIndex(HoroscopeText.HOROSCOPE_ID)));
                horoscopeText.setTextDate(cursor.getString(cursor.getColumnIndex(HoroscopeText.TEXT_DATE)));
                horoscopeText.setText(cursor.getString(cursor.getColumnIndex(HoroscopeText.TEXT)));
                horoscopeText.setTextURL(cursor.getString(cursor.getColumnIndex(HoroscopeText.TEXT_URL)));

                horoscopeTextList.add(horoscopeText);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return horoscopeTextList;
    }

    //add horoscope
    public void addHoroscopeText(ArrayList<HoroscopeText> horoscopeTexts){
        for(HoroscopeText horoscopeText : horoscopeTexts){
            ContentValues values = new ContentValues();
            values.put(HoroscopeText.HOROSCOPE_ID, horoscopeText.getTextID());
            values.put(HoroscopeText.TEXT_DATE, horoscopeText.getTextDate());
            values.put(HoroscopeText.TEXT, horoscopeText.getText());
            values.put(HoroscopeText.TEXT_URL, horoscopeText.getTextURL());

            try{
                db.insert(Constants.TBL_HOROSCOPE_TEXT, null, values);
            }catch (SQLiteException ex){
                db.endTransaction();
                Log.e(LOG_TAG, "exception "+ex.getMessage());
            }

            finally {
                db.endTransaction();
            }
        }
    }

    private static class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context){
            super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

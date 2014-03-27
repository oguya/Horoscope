package com.droid.horoscope.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.droid.horoscope.model.Horoscopes;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.SortedMap;

/**
 * Created by james on 21/03/14.
 */
public class Utils {
    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {
            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable resizeDrawable(Context context, int imageID, int width, int height){
        Drawable drawable = context.getResources().getDrawable(imageID);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
    }

    //return date in format => 2014-03-21 OR Friday, 21 March, 2014

    public static String formatDate(int dateType, String dbDateStr){
        SimpleDateFormat dateFormat ;
        Date now = new Date();
        Date horoscopeDate;
        String dateStr = "";

        switch (dateType){
            case 0: //db date => 2014-03-21
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                break;

            case 1: //display date => Friday, 21 March, 2014
                dateFormat = new SimpleDateFormat("EEEE dd, MMM, yyyy");
                break;

            default:
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                break;
        }
        try {
            if(dbDateStr != null){
                horoscopeDate = dateFormat.parse(dbDateStr);
                dateStr = dateFormat.format(horoscopeDate);
            }else{
                dateStr = dateFormat.format(now);
            }
        } catch (ParseException e) {
            Log.e("Utils", "exception in dateParse: "+e.getMessage());
        }
        return dateStr;
    }

    //return current date 2014-03-22
    public static String formatCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return dateFormat.format(now);
    }

    //return Friday, 21 March, 2014
    public static String formatDisplayDate(String strDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd, MMMM, yyyy");
        SimpleDateFormat horoscopeDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        Date horoscopeDate;
        String dispDateStr = "";
        try {
            horoscopeDate = horoscopeDateFormat.parse(strDate);

            if(horoscopeDate.getYear() != now.getYear()){
                dateFormat = new SimpleDateFormat("EEEE dd, MMM yyyy");
                dispDateStr = dateFormat.format(horoscopeDate);
            }else{
                dispDateStr = dateFormat.format(horoscopeDate);
            }

        } catch (ParseException e) {
            Log.e("Utils", "exception in dateParse: "+e.getMessage());
            dispDateStr = dateFormat.format(now);
        }
        return dispDateStr;
    }

    //datepicker:: get min date=>last yr Jan 1st
    public static long getMinDate(){
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();

        String dateStr = year+"-01-01";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date minDate = null;

        return new DateTime(year-1, 1, 1, 0, 0).getMillis();
    }

    //datepicker:: get max date=>Today
    public static long getMaxDate(int year, int month, int day){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        String dateStr = year+"-"+month+"-"+day;
        try{
            now = dateFormat.parse(dateStr);
        }catch (ParseException ex){
            Log.e("Utils", "exception in dateParse: "+ex.getMessage());
            now = new Date();
        }
        return now.getTime();
    }

    public static long getMaxDate(){
        return new DateTime().getMillis();
    }

    //get all horoscopes
    public static ArrayList<Horoscopes> getHoroscopeDetails(){
        ArrayList<Horoscopes> HoroscopesList = new ArrayList<Horoscopes>();

        HoroscopesList.add(new Horoscopes(0, "Aries", null));
        HoroscopesList.add(new Horoscopes(1, "Taurus", null));
        HoroscopesList.add(new Horoscopes(2, "Gemini", null));
        HoroscopesList.add(new Horoscopes(3, "Cancer", null));
        HoroscopesList.add(new Horoscopes(4, "Leo", null));
        HoroscopesList.add(new Horoscopes(5, "Virgo", null));
        HoroscopesList.add(new Horoscopes(6, "Libra", null));
        HoroscopesList.add(new Horoscopes(7, "Scorpio", null));
        HoroscopesList.add(new Horoscopes(8, "Sagittarius", null));
        HoroscopesList.add(new Horoscopes(9, "Capricorn", null));
        HoroscopesList.add(new Horoscopes(10, "Aquarius", null));
        HoroscopesList.add(new Horoscopes(11, "Pisces", null));

        return HoroscopesList;
    }

    //get query date // => month/day/year => 3/26/2014
    public static String getQueryDate(DateTime  dateTime){
        String dateStr = dateTime.getMonthOfYear()+"/"+dateTime.getDayOfMonth()+"/"+dateTime.getYear();
        Log.e("Utils","queryDate: "+dateStr);
        return dateStr;
    }

    public static String getQueryDate(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date qDate;
        SimpleDateFormat qformat = new SimpleDateFormat("M/dd/yyyy");
        String queryDate;

        try {
            qDate = simpleDateFormat.parse(dateStr);
            queryDate = qformat.format(qDate);
        } catch (ParseException e) {
            e.printStackTrace();
            queryDate = qformat.format(new Date());
        }
        Log.e("Utils", "Query Date: "+queryDate);
        return queryDate;
    }

    public static String formatDBDate(String dateStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd");
        Date date;
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dbDateStr;

        try {
            date = dateFormat.parse(dateStr);
            dbDateStr = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            dbDateStr = dateFormat2.format(new Date());
        }
        return dbDateStr;
    }

    //convert 3/26/2014 to 2014-03-26
    public static String formatScopeDate(String queryDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat scopeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr;
        try {
            Date date = dateFormat.parse(queryDate);
            dateStr = scopeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Utils","Exeception: "+e.getMessage());
            dateStr = formatCurrentDate();
        }
        Log.e("Utils","scopeDate: "+dateStr);
        return dateStr;
    }

}

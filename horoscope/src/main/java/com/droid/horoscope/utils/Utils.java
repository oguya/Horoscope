package com.droid.horoscope.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}

package com.droid.horoscope.model;

/**
 * Created by james on 22/03/14.
 */
public class HoroscopeText {

    public static final String TEXT_ID = "text_id";
    public static final String HOROSCOPE_ID = "horoscope_id";
    public static final String TEXT_DATE = "text_date";
    public static final String TEXT = "text";
    public static final String TEXT_URL = "text_url";

    private int text_id;
    private int horoscope_id;
    private String text_date;
    private String text;
    private String text_url;

    public HoroscopeText(){}

    public HoroscopeText(int text_id, int horoscope_id,
                         String text_date, String text, String text_url){
        this.text_id = text_id;
        this.horoscope_id = horoscope_id;
        this.text_date = text_date;
        this.text = text;
        this.text_url = text_url;
    }

    public int getTextID(){
        return this.text_id;
    }

    public void setTextID(int text_id){
        this.text_id = text_id;
    }

    public int getHoroscopeID(){
        return this.horoscope_id;
    }

    public void setHoroscopeID(int horoscope_id){
        this.horoscope_id = horoscope_id;
    }

    public String getTextDate(){
        return this.text_date;
    }

    public void setTextDate(String text_date){
        this.text_date = text_date;
    }

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getTextURL(){
        return this.text_url;
    }

    public void setTextURL(String text_url){
        this.text_url = text_url;
    }

}

package com.droid.horoscope.model;

/**
 * Created by james on 22/03/14.
 */
public class Horoscopes {

    public static final String HOROSCOPE_ID = "horoscope_id";
    public static final String HOROSCOPE_NAME = "horoscope_name";
    public static final String HOROSCOPE_DATE = "horoscope_date";

    private int horoscope_id;
    private String horoscope_name;
    private String horoscope_date;

    public Horoscopes(){}

    public Horoscopes(int horoscope_id, String horoscope_name, String horoscope_date){
        this.horoscope_id = horoscope_id;
        this.horoscope_name = horoscope_name;
        this.horoscope_date = horoscope_date;
    }

    public int getHoroscopeID(){
        return this.horoscope_id;
    }

    public void setHoroscopeID(int horoscope_id){
        this.horoscope_id = horoscope_id;
    }

    public String getHoroscopeName(){
        return this.horoscope_name;
    }

    public void setHoroscopeName(String horoscope_name){
        this.horoscope_name = horoscope_name;
    }

    public String getHoroscopeDate(){
        return this.horoscope_date;
    }

    public void setHoroscopeDate(String horoscope_date){
        this.horoscope_date = horoscope_date;
    }

}

package com.droid.horoscope.constants;

/**
 * Created by james on 17/02/14.
 *
 * Hold all constants
 *
 */

public class Constants {

    //Search Activity const.
    public static final String KEY_TAB_POS = "Tab_Position";

    //DB consts
    public static final String DB_NAME = "horoscope.sqlite";
    public static final String DB_DIR = "/data/data/com.droid.horoscope/databases/";
    public static final int DB_VERSION = 1;
    public static final String TBL_HOROSCOPES = "horoscopes";
    public static final String TBL_HOROSCOPE_TEXT = "horoscope_text";
    public static final String FORCE_FOREIGN_KEY_CHECKS = "PRAGMA foreign_keys = ON";

    //Fragment position
    public static final String KEY_FRAG_POSITION = "FragmentPosition";
    public static final String KEY_LOADING_STATE = "Loading";

    //horoscope RSS
    public static final String HOROSCOPE_RSS_URL = "http://www.findyourfate.com/rss/horoscope-astrology-feed.asp?mode=view&todate={query}";
}

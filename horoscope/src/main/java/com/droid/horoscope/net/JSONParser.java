package com.droid.horoscope.net;

import com.droid.horoscope.model.HoroscopeText;
import com.droid.horoscope.model.Horoscopes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by james on 26/03/14.
 */
public class JSONParser {

    public static final String RESPONSE_DATA = "responseData";
    public static final String RESPONSE_STATUS = "responseStatus";
    public static final String RESPONSE_DETAILS = "responseDetails";
    public static final String FEED = "feed";
    public static final String FEED_URL = "feedUrl";
    public static final String LINK = "link";
    public static final String ENTRIES = "entries";
    public static final String CONTENT = "content";
    public static final String TITLE = "title";

    private String jsonStr;
    private String dateStr;
    private ArrayList<Horoscopes> horoscopesDetails;

    public JSONParser(){}

    public JSONParser(String jsonStr, String dateStr, ArrayList<Horoscopes> horoscopesDetails ){
        this.jsonStr = jsonStr;
        this.dateStr = dateStr;
        this.horoscopesDetails = horoscopesDetails;
    }

    public ArrayList<HoroscopeText> parseJSON() throws JSONException{
        JSONObject jsonData = new JSONObject(jsonStr);
        ArrayList<HoroscopeText> horoscopeTextList = new ArrayList<HoroscopeText>();

        JSONObject responseData = jsonData.getJSONObject(RESPONSE_DATA);
        int responseStatus = jsonData.getInt(RESPONSE_STATUS);
        String responseDetails = jsonData.getString(RESPONSE_DETAILS);

        JSONObject feeds = responseData.getJSONObject(FEED);
        String feedURL = feeds.getString(FEED_URL);
        JSONArray entries = feeds.getJSONArray(ENTRIES);

        for(int i=0; i<entries.length(); i++){
            JSONObject entry = entries.getJSONObject(i);

            String title = entry.getString(TITLE);
            int horoscopeID = getHoroscopeID(title);
            String link = entry.getString(LINK);
            String content = entry.getString(CONTENT);

            HoroscopeText horoscopeText = new HoroscopeText();
            horoscopeText.setHoroscopeID(horoscopeID);
            horoscopeText.setTextURL(link);
            horoscopeText.setText(content);
            horoscopeText.setTextDate(dateStr);

            horoscopeTextList.add(horoscopeText);
        }
        return horoscopeTextList;
    }

    public int getHoroscopeID(String title){
        int horoscopeID = 0;
        for(Horoscopes horoscope : horoscopesDetails){
            if(horoscope.getHoroscopeName().contains(title)){
                horoscopeID = horoscope.getHoroscopeID();
            }
        }
        return horoscopeID;
    }
}

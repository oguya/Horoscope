package com.droid.horoscope.net;

import android.os.AsyncTask;
import android.util.Log;

import com.droid.horoscope.constants.Constants;
import com.droid.horoscope.db.DBAdapter;
import com.droid.horoscope.model.HoroscopeText;
import com.droid.horoscope.model.Horoscopes;
import com.droid.horoscope.utils.Utils;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by james on 26/03/14.
 */
public class FetchHoroscopes extends AsyncTask<String, Integer, ArrayList<HoroscopeText>> {

    public static final String LOG_TAG = "FetchHoroscope";
    private String dateStr;

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected ArrayList<HoroscopeText> doInBackground(String... params) {
        String dateStr = params[0]; // => month/day/year => 3/26/2014
        this.dateStr = Utils.formatScopeDate(dateStr);
        String xmlResponse = null;
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            xmlResponse = restTemplate.getForObject(Constants.HOROSCOPE_RSS_URL, String.class, dateStr);

            Log.e(LOG_TAG, "Got Response size: "+xmlResponse.length()+" data: "+xmlResponse);
            parseResult(xmlResponse);
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(LOG_TAG, "ex: "+ex.getMessage());
        }

        return (xmlResponse != null) ? parseResult(xmlResponse) : null;
    }

    @Override
    protected void onPostExecute(ArrayList<HoroscopeText> results){
        super.onPostExecute(results);
    }

    public ArrayList<HoroscopeText> parseResult(String xmlStr) {
        ArrayList<HoroscopeText> horoscopeTextList = new ArrayList<HoroscopeText>();
        XMLParser xmlParser = new XMLParser(xmlStr);
        try {
            Document document = xmlParser.getDomElements();
            NodeList nodeList = document.getElementsByTagName(XMLParser.ITEM);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String title = xmlParser.getValue(element, XMLParser.TITLE);
                int horoscopeID = getHoroscopeID(title);
                String link = xmlParser.getValue(element, XMLParser.LINK);
                String description = xmlParser.getValue(element, XMLParser.DESCRIPTION);

                HoroscopeText horoscopeText = new HoroscopeText();
                horoscopeText.setHoroscopeID(horoscopeID);
                horoscopeText.setTextDate(dateStr);
                horoscopeText.setText(description);
                horoscopeText.setTextURL(link);

                if(horoscopeID != -1){
                    horoscopeTextList.add(horoscopeText);
                    Log.e(LOG_TAG, "ID:"+horoscopeID+"scope: "+title+" link: "+link+" desc: "+description);
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return horoscopeTextList;
    }

    public int getHoroscopeID(String title){
        int horoscopeID = -1;

        for(Horoscopes horoscope : Utils.getHoroscopeDetails()){
            if(title.contains(horoscope.getHoroscopeName())){
                horoscopeID = horoscope.getHoroscopeID();
                return horoscopeID;
            }
        }
        return horoscopeID;
    }

}

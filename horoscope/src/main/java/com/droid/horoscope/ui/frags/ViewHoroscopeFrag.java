package com.droid.horoscope.ui.frags;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.droid.horoscope.R;

import org.w3c.dom.Text;

/**
 * Created by james on 21/03/14.
 */
public class ViewHoroscopeFrag extends Fragment {

    private Activity activity;
    private final String LOG_TAG = "ViewHoroscopeFrag";

    private int horoscope;
    private String[] horoscopeList;

    private ProgressBar scope_loading;
    private TextView scope_date_txt;
    private ImageView vwh_img_thumbnail;
    private TextView vwh_horoscope_name;
    private TextView vwh_bday_txt;
    private TextView vwh_horoscope_txt;
    private Button btn_read_more;

    public ViewHoroscopeFrag(int horoscope) {
        this.horoscope = horoscope;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_view_horoscope, container, false);

        //init ui here
        scope_loading = (ProgressBar)rootView.findViewById(R.id.scope_loading);
        scope_date_txt = (TextView)rootView.findViewById(R.id.vwh_date_txt);
        vwh_img_thumbnail = (ImageView)rootView.findViewById(R.id.vwh_img_thumbnail);
        vwh_horoscope_name = (TextView)rootView.findViewById(R.id.vwh_horoscope_name);
        vwh_bday_txt = (TextView)rootView.findViewById(R.id.vwh_bday_txt);
        vwh_horoscope_txt = (TextView)rootView.findViewById(R.id.vwh_horoscope_txt);
        btn_read_more = (Button)rootView.findViewById(R.id.btn_read_more);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    //change ui stuff..frag is live
    @Override
    public void onStart(){
        super.onStart();

        horoscopeList = activity.getResources().getStringArray(R.array.nav_drawer_items);

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }
}

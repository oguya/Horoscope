package com.droid.horoscope.ui.frags;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droid.horoscope.R;

/**
 * Created by james on 21/03/14.
 */
public class ViewHoroscopeFrag extends Fragment {

    private Activity activity;
    private final String LOG_TAG = "ViewHoroscopeFrag";

    private int horoscope;
    private String[] horoscopeList;
    private TextView horoscope_name;

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

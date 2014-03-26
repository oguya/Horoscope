package com.droid.horoscope.ui.frags;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.horoscope.R;
import com.droid.horoscope.constants.Constants;
import com.droid.horoscope.db.DBAdapter;
import com.droid.horoscope.model.HoroscopeText;
import com.droid.horoscope.model.Horoscopes;
import com.droid.horoscope.net.FetchHoroscopes;
import com.droid.horoscope.utils.Utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * Created by james on 21/03/14.
 */
public class ViewHoroscopeFrag extends Fragment {

    private Activity activity;
    private final String LOG_TAG = "ViewHoroscopeFrag";

    private int horoscopeID;
    private String[] horoscopeNameList;

    private ProgressBar scope_loading;
    private ScrollView scroll_section;
    private TextView scope_date_txt;
    private ImageView vwh_img_thumbnail;
    private TextView vwh_horoscope_name;
    private TextView vwh_bday_txt;
    private TextView vwh_horoscope_txt;
    private Button btn_read_more;

    public DBAdapter dbAdapter;
    public ArrayList<HoroscopeText> horoscopeTextList;
    public ArrayList<Horoscopes> horoscopeDetails;
    public String horoscopeDate;
    public String scope_url;
    public boolean LOADING=false;
    public DateTime datePickerTime;

    public ViewHoroscopeFrag(int horoscopeID) {
        this.horoscopeID = horoscopeID;
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
        scope_loading = (ProgressBar) rootView.findViewById(R.id.scope_loading);
        scroll_section = (ScrollView) rootView.findViewById(R.id.section_scroll);
        scope_date_txt = (TextView) rootView.findViewById(R.id.vwh_date_txt);
        vwh_img_thumbnail = (ImageView) rootView.findViewById(R.id.vwh_img_thumbnail);
        vwh_horoscope_name = (TextView) rootView.findViewById(R.id.vwh_horoscope_name);
        vwh_bday_txt = (TextView) rootView.findViewById(R.id.vwh_bday_txt);
        vwh_horoscope_txt = (TextView) rootView.findViewById(R.id.vwh_horoscope_txt);
        btn_read_more = (Button) rootView.findViewById(R.id.btn_read_more);
        btn_read_more.setOnClickListener(btnListener);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        if(savedInstanceState != null){
            LOADING = savedInstanceState.getBoolean(Constants.KEY_LOADING_STATE);
        }
    }

    //change ui stuff..frag is live
    @Override
    public void onStart() {
        super.onStart();
        horoscopeNameList = activity.getResources().getStringArray(R.array.nav_drawer_items);

        dbAdapter = new DBAdapter(this.activity);
        dbAdapter.open();

        horoscopeDate = Utils.formatCurrentDate();
        horoscopeTextList = dbAdapter.getHoroscopeText(horoscopeID, horoscopeDate);
        horoscopeDetails = dbAdapter.getHoroscope(horoscopeID);

        if (horoscopeTextList.size() <= 0) {
            Log.e(LOG_TAG, "No horoscopes for date: " + horoscopeDate + ". Getting the lattest!");
            horoscopeTextList = dbAdapter.getLatestHoroscopeText(horoscopeID);

            //TODO fetch latest from Net
        }

        if (horoscopeTextList.size() <= 0) {
            scope_loading.setVisibility(View.VISIBLE);
            scroll_section.setVisibility(View.GONE);
            LOADING = true;
        } else {
            setData();
            LOADING = false;
        }
    }

    private void setData() {
        String scope_date = horoscopeTextList.get(0).getTextDate();
        String scope_txt = horoscopeTextList.get(0).getText();
        TypedArray imgs = activity.getResources().obtainTypedArray(R.array.nav_drawer_icons);
        String scope_name = horoscopeNameList[horoscopeID];
        String scope_bday = horoscopeDetails.get(0).getHoroscopeDate();
        scope_url = horoscopeTextList.get(0).getTextURL();

        scope_date_txt.setText(Utils.formatDisplayDate(scope_date));
        vwh_img_thumbnail.setImageResource(imgs.getResourceId(horoscopeID, -1));
        vwh_horoscope_name.setText(scope_name);
        vwh_bday_txt.setText(scope_bday);
        vwh_horoscope_txt.setText(scope_txt);
    }

    @Override
    public void onResume() {
        super.onResume();
        dbAdapter.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        dbAdapter.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.KEY_LOADING_STATE, LOADING);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.view_horoscope, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(LOADING)
            return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_datepicker: //open settings
                createDatePicker().show();
                break;
            case R.id.action_share: //share url
                startActivity(Intent.createChooser(getDefaultShareIntent(),
                        "Share "+horoscopeNameList[horoscopeID]+" horoscope"));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    protected DatePickerDialog createDatePicker() {
        DateTime dateTime = new DateTime();

        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();

        DatePickerDialog datePickerDlg = new DatePickerDialog(activity, datePickerListener, year, month-1, day);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.d(LOG_TAG, "HoneyComb device...");
            datePickerDlg.getDatePicker().setMaxDate(Utils.getMaxDate());
            datePickerDlg.getDatePicker().setMinDate(Utils.getMinDate());
        }

        return datePickerDlg;
    }

    //process date for <= API 10
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public void DatePicker_GB(){
        if(datePickerTime != null){
            DateTime maxDate = new DateTime(Utils.getMaxDate());
            DateTime minDate = new DateTime(Utils.getMinDate());

            //manually check the date
            if(datePickerTime.getYear() < minDate.getYear() ||
                    datePickerTime.getYear() > maxDate.getYear() ){
                Toast.makeText(activity, "Please select a date between the year "+minDate.getYear()+
                        " and "+maxDate.getYear(), Toast.LENGTH_LONG).show();
            }
        }
    }

    //datepicker dlglistener
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //month starts from 0 =>Jan & 11=>Dec
            String dateStr = year + "-" + monthOfYear + "-" + dayOfMonth;
            datePickerTime = new DateTime(year, monthOfYear+1, dayOfMonth, 0, 0);
            Log.e(LOG_TAG, "DatePicker: " + dateStr);

            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1){
                DatePicker_GB();
            }

            //fetch scopes
            String queryDate = Utils.getQueryDate(datePickerTime);
            String[] args = {queryDate};
            ArrayList<HoroscopeText> horoscopeTexts = new ArrayList<HoroscopeText>();
            new FetchHoroscopes().execute(args);

            /*
            try {
                 horoscopeTexts = new FetchHoroscopes().execute(args).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Fetch interrupt: "+e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "execution exception: "+e.getMessage());
            }
            */
        }
    };

    public View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_read_more: //open browser
                    if (scope_url != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scope_url));
                        startActivity(intent);
                    } else {
                        view.setEnabled(false);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    //share scope_url
    private Intent getDefaultShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Today's "+horoscopeNameList[horoscopeID]+" horoscope "+scope_url);
        intent.setType("text/plain");
        return intent;
    }

    //check for
    public class FetchWrapper{

        public FetchWrapper(){}

        public ArrayList<HoroscopeText> getHoroscopes(int horoscopeID){
            ArrayList<HoroscopeText> horoscopeTexts = new ArrayList<HoroscopeText>();

            return horoscopeTexts;
        }
    }
}
package com.droid.horoscope.ui;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.droid.horoscope.R;
import com.droid.horoscope.adapters.NavDrawerListAdapter;
import com.droid.horoscope.constants.Constants;
import com.droid.horoscope.db.DBAdapter;
import com.droid.horoscope.model.NavDrawerItem;
import com.droid.horoscope.ui.frags.ViewHoroscopeFrag;
import com.droid.horoscope.utils.FirstRunInit;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ActionBar actionBar;
    private static final String LOG_TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence drawerAppTitle;
    private String[] drawerMenuTitles;
    private TypedArray drawerMenuIcons;
    private ArrayList<NavDrawerItem> drawerItems;
    private NavDrawerListAdapter drawerListAdapter;
    private int fragPosition = -1;

    private FirstRunInit firstRunInit;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check first run
        checkFirstRun();

        actionBar = getSupportActionBar();

        prepDrawerMenu();

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        //check for saved instances..orientation changes
        if(savedInstanceState == null){ //default view
            displayDrawerView(0);
        }else{
            fragPosition = savedInstanceState.getInt(Constants.KEY_FRAG_POSITION, 0);
            displayDrawerView(fragPosition);
        }

    }

    private void prepDrawerMenu(){
        drawerTitle = drawerAppTitle = getTitle();
        drawerMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        drawerMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.list_slidermenu);

        //add nav drawer items to array
        drawerItems = new ArrayList<NavDrawerItem>();
        for(int x = 0; x < drawerMenuTitles.length; x++){
            drawerItems.add(new NavDrawerItem(drawerMenuTitles[x], drawerMenuIcons.getResourceId(x, -1)));
        }

        //recycle typed array
        drawerMenuIcons.recycle();
        drawerListAdapter = new NavDrawerListAdapter(getApplicationContext(), drawerItems);
        drawerList.setAdapter(drawerListAdapter);
        drawerList.setOnItemClickListener(new drawerClickListener());

        //enable AB app icon..acts a toggle button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, //drawer layout
                R.drawable.ic_drawer, //nav drawer toggle icon
                R.string.app_name, //drawer open - content desc
                R.string.app_name //drawer close - content desc
        ){
            @Override
            public void onDrawerClosed(View view){
                actionBar.setTitle(drawerTitle);
                //call onPrepareOptionsMenu() to show actionbar icons
                if(!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1))
                    invalidateOptionsMenu();
                else
                    supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View view){
                //call onPrepareOptionsMenu() to hide actionbar icons
                if(!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1))
                    invalidateOptionsMenu();
                else
                    supportInvalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
    }

    //display frags for selected nav drawer list item
    //update current frag by replacing frags
    private void displayDrawerView(int position){
        Fragment fragment = null;
        fragPosition = position;
        switch (position){
            case 0: // Aries
                fragment = new ViewHoroscopeFrag(0);
                break;
            case 1: // Taurus
                fragment = new ViewHoroscopeFrag(1);
                break;
            case 2: // Gemini
                fragment = new ViewHoroscopeFrag(2);
                break;
            case 3: // Cancer
                fragment = new ViewHoroscopeFrag(3);
                break;
            case 4: // Leo
                fragment = new ViewHoroscopeFrag(4);
                break;
            case 5: // Virgo
                fragment = new ViewHoroscopeFrag(5);
                break;
            case 6: // Libra
                fragment = new ViewHoroscopeFrag(6);
                break;
            case 7: // Scorpio
                fragment = new ViewHoroscopeFrag(7);
                break;
            case 8: // Sagittarius
                fragment = new ViewHoroscopeFrag(8);
                break;
            case 9: // Capricorn
                fragment = new ViewHoroscopeFrag(9);
                break;
            case 10: // Aquarius
                fragment = new ViewHoroscopeFrag(10);
                break;
            case 11: // Pisces
                fragment = new ViewHoroscopeFrag(11);
                break;

            default: break;
        }

        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            //update selected item & title, close drawer
            drawerList.setItemChecked(position, true);
            drawerList.setSelection(position);
//            setTitle(drawerMenuTitles[position]);
            actionBar.setSubtitle(drawerMenuTitles[position]);
            drawerLayout.closeDrawer(drawerList);
        }else{
            //null fragment
            Log.e(LOG_TAG, "Unable to create fragment");
            Toast.makeText(this, "Unable to create fragment", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //toggle nav drawer on selecting AB app icon/title
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()){
            case R.id.action_settings: //open settings
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    //called when invalidateOptionsMenu() is called
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is open, hide action items
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title){
        drawerAppTitle = title;
        actionBar.setTitle(drawerAppTitle);
    }

    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        //sync toggle state after onRestoreInstanceState has occured
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        //pass any configuration changes to drawerToggle
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //navdrawer list item click listener
    private class drawerClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //display view for selected nav drawer item
            displayDrawerView(position);
        }
    }

    public void onPause(){
        super.onPause();
        dbAdapter.close();
    }

    public void onResume(){
        super.onResume();
        dbAdapter.open();

        if(fragPosition != -1){
            displayDrawerView(fragPosition);
        }
    }

    public void onSaveInstanceState(Bundle outState){
        outState.putInt(Constants.KEY_FRAG_POSITION, fragPosition);
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        fragPosition = savedInstanceState.getInt(Constants.KEY_FRAG_POSITION, 0);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void checkFirstRun(){
        SharedPreferences firstRunPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstRun = firstRunPrefs.getBoolean("FirstRunInit", true);

        if(firstRun){
            //copy db
            Log.e(LOG_TAG, "First Run! initializing resources...");
            firstRunInit = new FirstRunInit(this);
            firstRunInit.copyDBFile();

            firstRunPrefs.edit().putBoolean("FirstRunInit", false).commit();
        }else{
            Log.e(LOG_TAG, "First Run! all assets GREEN...");
        }
    }

}

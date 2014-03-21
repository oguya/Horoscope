package com.droid.horoscope.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.horoscope.R;
import com.droid.horoscope.model.NavDrawerItem;
import com.droid.horoscope.utils.Utils;

import java.util.ArrayList;

/**
 * Created by james on 21/03/14.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
        }
        ImageView imgIcon = (ImageView)convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView)convertView.findViewById(R.id.title);

//        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        imgIcon.setImageDrawable(Utils.resizeDrawable(context,
                navDrawerItems.get(position).getIcon(), WIDTH, HEIGHT));
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        return convertView;
    }
}

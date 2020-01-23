package com.gls.vz_lock.adapters;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.vz_lock.R;
import com.gls.vz_lock.beans.AppInfo;

import java.util.ArrayList;

/**
 * Created by Santosh on 12-Dec-17.
 */

public class MenuAdapter extends BaseAdapter {
    AppCompatActivity context;
    int resource;
    ArrayList<AppInfo> apps;
    LayoutInflater inflater;
    PackageManager packageManager;
    public MenuAdapter(AppCompatActivity context, int resource, ArrayList<AppInfo> apps){
        this.context = context;
        this.resource = resource;
        this.apps = apps;
        inflater = context.getLayoutInflater();
        packageManager = context.getPackageManager();
    }

    public int getCount() {
        return apps.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout menu = (LinearLayout) inflater.inflate(resource,null);
        ImageView icon = (ImageView) menu.findViewById(R.id.appIcon);
        TextView label = (TextView) menu.findViewById(R.id.appName);
        label.setText(apps.get(position).getAppname());
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(apps.get(position).getAppPackage(), PackageManager.GET_META_DATA);
            icon.setImageDrawable(packageManager.getApplicationIcon(appInfo));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        menu.setTag(apps.get(position));
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfo app = (AppInfo) v.getTag();
                Intent launchIntent = packageManager.getLaunchIntentForPackage(app.getAppPackage());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity( launchIntent );
            }
        });
        return menu;
    }
}

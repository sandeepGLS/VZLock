package com.gls.vz_lock;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gls.vz_lock.adapters.MenuAdapter;
import com.gls.vz_lock.beans.AppInfo;
import com.gls.vz_lock.utils.SP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    LinearLayout loginContainer, settingContainer;
    EditText adminPassword, adminPassword1, adminPassword2;
    SP sp;
    ListView appList;
    ArrayList<AppInfo> menuApps = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = new SP(getApplicationContext());
        loginContainer = findViewById(R.id.loginContainer);
        settingContainer = findViewById(R.id.settingContainer);
        adminPassword = findViewById(R.id.adminPassword);
        adminPassword1 = findViewById(R.id.newAdminPassword1);
        adminPassword2 = findViewById(R.id.newAdminPassword2);
        appList = findViewById(R.id.appList);
        loginContainer.setVisibility(View.VISIBLE);
        settingContainer.setVisibility(View.GONE);
    }

    public void adminLogin(View v){
        String password = adminPassword.getText().toString().trim();
        if(password.length()==0){
            Toast.makeText(getApplicationContext(),"Please Enter Password", Toast.LENGTH_LONG).show();
            return;
        }

        if(password.equals(sp.getPassword()) || password.equals(sp.getMasterPassword())){
            loginContainer.setVisibility(View.GONE);
            settingContainer.setVisibility(View.VISIBLE);
            loadAppList();
        } else {
            Toast.makeText(getApplicationContext(),"Please Enter Valid Password", Toast.LENGTH_LONG).show();
        }
        adminPassword.setText("");
    }


    public void changePassword(View v){
        String pass1 = adminPassword1.getText().toString().trim();
        String pass2 = adminPassword2.getText().toString().trim();
        if(pass1.equals(pass2) && pass1.length()>5){
            adminPassword1.setText("");
            adminPassword2.setText("");
            sp.setPassword(pass1);
            Toast.makeText(getApplicationContext(),"Password Changed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"Password Not Matching", Toast.LENGTH_LONG).show();
        }
    }

    public void loadAppList(){
        final PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resInfos = packageManager.queryIntentActivities(intent, 0);

        HashSet<String> packageNames = new HashSet<String>(0);
        List<ApplicationInfo> appInfos = new ArrayList<ApplicationInfo>(0);

        for(ResolveInfo resolveInfo : resInfos) {
            packageNames.add(resolveInfo.activityInfo.packageName);
        }
        for(String packageName : packageNames) {
            if(!packageName.equals("vztrack.gls.com.vztracksociety")) {
                try {
                    appInfos.add(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
        }

        Collections.sort(appInfos, new ApplicationInfo.DisplayNameComparator(packageManager));
        for(int i = 0; i<appInfos.size();i++){
            AppInfo app = new AppInfo();
            app.setIcon(packageManager.getApplicationIcon(appInfos.get(i)));
            app.setAppname(packageManager.getApplicationLabel(appInfos.get(i)).toString());
            app.setAppPackage(appInfos.get(i).packageName);
            menuApps.add(app);
        }

        MenuAdapter appAdapter = new MenuAdapter(this, R.layout.app_row, menuApps);
        appList.setAdapter(appAdapter);
    }
}

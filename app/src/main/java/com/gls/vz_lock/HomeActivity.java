package com.gls.vz_lock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.vz_lock.utils.SP;

public class HomeActivity extends AppCompatActivity {
    //final String APP = "vztrack.gls.com.vztrack_user";
    final String APP = "vztrack.gls.com.vztracksociety";
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private static final int REQUEST_ENABLE = 1;
    private static final int SET_PASSWORD = 2;
    private int settingCnt = 0;
    LinearLayout  menuLL;
    PackageManager packageManager;
    ImageView icon;
    TextView label;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ENABLE:
                    devicePolicyManager.setMaximumTimeToLock(componentName, 3000L);
                    devicePolicyManager.setMaximumFailedPasswordsForWipe(componentName, 5);
                    devicePolicyManager.setPasswordQuality(componentName, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
                    devicePolicyManager.setCameraDisabled(componentName, false);
                    //devicePolicyManager.setLockTaskPackages(componentName,new String[]{getPackageName(),"vztrack.gls.com.vztracksociety"});
                    Log.e("#####",componentName.getPackageName());

                    boolean isSufficient = devicePolicyManager.isActivePasswordSufficient();

                    if (!isSufficient) {
                        Intent setPasswordIntent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                        startActivityForResult(setPasswordIntent, SET_PASSWORD);
                        devicePolicyManager.setPasswordExpirationTimeout(componentName, 10000L);
                    }
                    break;
            }
        } else {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyPolicyReceiver.class);
        boolean isActive = devicePolicyManager.isAdminActive(componentName);
        packageManager = getPackageManager();
        menuLL = findViewById(R.id.menuLL);
        icon = findViewById(R.id.appIcon);
        label =  findViewById(R.id.appName);
        if(!isActive) {
            setAppAsAdmin();
        } else {
            devicePolicyManager.setKeyguardDisabled(componentName, true);
            devicePolicyManager.setStatusBarDisabled(componentName, true);
        }
    }

    public void setAppAsAdmin() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Why this needed message here");
        startActivityForResult(intent, REQUEST_ENABLE);

        //devicePolicyManager.removeActiveAdmin(componentName);

    }

    public void openSettings(View v){
        settingCnt++;
        if(settingCnt > 4){
            settingCnt = 0;
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));
        }
    }



    public void launchApp(View v){
        try {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(APP);
            //LaunchIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(LaunchIntent);
        } catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        settingCnt = 0;
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(APP, PackageManager.GET_META_DATA);
            icon.setImageDrawable(packageManager.getApplicationIcon(appInfo));
            label.setText("VZ-Track");
        } catch (Exception e){
            label.setText("App Not Installed");
        }
    }
}

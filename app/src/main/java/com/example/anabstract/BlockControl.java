package com.example.anabstract;

import static kotlinx.coroutines.DelayKt.delay;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BlockControl extends AppCompatActivity {

    private List<AppInfo> appList = new ArrayList<>();
    private AppAdapter appAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_control);

        ListView appListView = findViewById(R.id.appListView);


        // Check if usage access permission is granted
        if (!isAccessGranted()) {
            Toast.makeText(BlockControl.this, "Grant permission to use feature",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

        // Load the list of installed apps
        loadInstalledApps();

        appAdapter = new AppAdapter(this, appList);
        appListView.setAdapter(appAdapter);
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void loadInstalledApps() {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // Iterate through installed applications
        for (ApplicationInfo app : apps) {
            // Check if the app is not our own app and is either a system app or user-installed app
            if (!app.packageName.equals(getPackageName()) &&
                    ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0 ||
                            (app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)) {

                // Load application name and icon
                String appName = app.loadLabel(packageManager).toString();
                appList.add(new AppInfo(appName, app.packageName, app.loadIcon(packageManager)));
            }
        }
    }



}


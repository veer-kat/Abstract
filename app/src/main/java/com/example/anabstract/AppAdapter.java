package com.example.anabstract;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class AppAdapter extends ArrayAdapter<AppInfo> {

    private HashMap<String, Long> blockedApps = new HashMap<>();

    public AppAdapter(Context context, List<AppInfo> apps) {
        super(context, 0, apps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_app, parent, false);
        }

        AppInfo app = getItem(position);
        ImageView appIcon = convertView.findViewById(R.id.appIcon);
        TextView appName = convertView.findViewById(R.id.appName);
        Switch blockSwitch = convertView.findViewById(R.id.blockSwitch);

        appIcon.setImageDrawable(app.getIcon());
        appName.setText(app.getName());

        blockSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Blocking " + app.getName() + " for 10 seconds", Toast.LENGTH_SHORT).show();
                blockSwitch.setEnabled(false);

                // Add the app to the blocked list for 10 seconds
                long unblockTime = System.currentTimeMillis() + 10000;
                blockedApps.put(app.getPackageName(), unblockTime);
                BlockedAppAccessibilityService.setBlockedApps(blockedApps); // Update the accessibility service

                new Handler().postDelayed(() -> {
                    blockSwitch.setChecked(false);
                    blockSwitch.setEnabled(true);
                    blockedApps.remove(app.getPackageName());
                    BlockedAppAccessibilityService.setBlockedApps(blockedApps); // Update the accessibility service
                    Toast.makeText(getContext(), app.getName() + " is unblocked", Toast.LENGTH_SHORT).show();
                }, 15000);
            }
        });

        return convertView;
    }
}


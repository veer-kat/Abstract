package com.example.anabstract;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.HashMap;

public class BlockedAppAccessibilityService extends AccessibilityService {

    // HashMap to store blocked apps and their unblock time
    private static HashMap<String, Long> blockedApps = new HashMap<>();

    // Method to set the blocked apps list from the AppAdapter
    public static void setBlockedApps(HashMap<String, Long> apps) {
        blockedApps = apps;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName().toString();
            Log.d("AccessibilityService", "Package opened: " + packageName);

            // Check if the opened app is in the blocked list
            if (blockedApps.containsKey(packageName)) {
                // Launch the overlay activity instead of going back to the home screen
                Intent overlayIntent = new Intent(this, BlockedOverlayActivity.class);
                overlayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(overlayIntent);
            }
        }
    }

    @Override
    public void onInterrupt() {
        // Handle any interruptions (like when another accessibility service takes over)
        Log.d("AccessibilityService", "Service interrupted");
    }


}


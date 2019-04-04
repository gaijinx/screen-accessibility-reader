package com.screenaccessibilityreader;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by Hari on 8/8/16.
 */
public class CustomAccessibilityService extends AccessibilityService {
    private static final String TAG = "CustomAccessibility";
    private Thread workerThread;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo source = accessibilityEvent.getSource();
        if (source == null) {
            return;
        }
//        Log.d(TAG, "onAccessibilityEvent: getCollectionInfo " + source.getCollectionInfo());
//        Log.d(TAG, "onAccessibilityEvent: getLabeledBy " + source.getLabeledBy());
//        Log.d(TAG, "onAccessibilityEvent: getViewIdResourceName " + source.getViewIdResourceName());
//        Log.d(TAG, "onAccessibilityEvent: describeContents " + source.describeContents());
//        Log.d(TAG, "onAccessibilityEvent: getClassName " + source.getClassName());
//        Log.d(TAG, "onAccessibilityEvent: getContentDescription " + source.getContentDescription());
//        Log.d(TAG, this.getRootInActiveWindow().toString());
    }

    @Override
    public void onInterrupt() {

    }

    //Configure the Accessibility Service
    @Override
    protected void onServiceConnected() {
        Log.d(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;

        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        }

        info.notificationTimeout = 0;
        this.setServiceInfo(info);

        // Start our custom Accessibility Server
        workerThread = new ServiceServer(this);
        workerThread.start();


    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroy");
        workerThread.interrupt();
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        Log.d(TAG, "Stopping");
        workerThread.interrupt();
        return super.stopService(name);
    }
}

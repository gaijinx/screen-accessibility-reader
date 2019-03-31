package com.screenaccessibilityreader;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
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
        // Set the type of events that this service wants to listen to. Others won't be passed to this service.
        // We are only considering windows state changed event.
//        info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        // Set the type of feedback your service will provide. We are setting it to GENERIC.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        // Default services are invoked only if no package-specific ones are present for the type of AccessibilityEvent generated.
        // This is a general-purpose service, so we will set some flags
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;

        // We are keeping the timeout to 0 as we don’t need any delay or to pause our accessibility events
        info.notificationTimeout = 0;
        this.setServiceInfo(info);
        workerThread = new Thread(new ServiceServer(this));
        workerThread.start();

    }
}

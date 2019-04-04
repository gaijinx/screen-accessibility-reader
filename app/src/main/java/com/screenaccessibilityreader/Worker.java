package com.screenaccessibilityreader;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Worker extends Thread {
    String TAG = "AccessibilityWorkerService";
    AccessibilityService mService;
    Socket mSocket;


    Worker(AccessibilityService accessibilityService, Socket socket){
        this.mService = accessibilityService;
        this.mSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
            String data = logNode2Xml(mService.getRootInActiveWindow());
            Log.d(TAG, "Sending data");
            writer.writeUTF(data);
            Log.d(TAG, "Reading ok");
            reader.readLine();
            Log.d(TAG, "Finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String logNode2Xml(AccessibilityNodeInfo node){
        String strNode = "<?xml version='1.0' encoding='utf-8' standalone='yes'?><hierarchy rotation=\"0\">";
        strNode += logNode2Xml(node, 0);
        strNode += "</hierarchy>";
        return strNode;
    }

    private String logNode2Xml(AccessibilityNodeInfo node, int index) {
        if (node == null)
            return "";
        StringBuilder strNode = new StringBuilder();
        if (node.getChildCount()>0) {
            strNode.append("<node ");
            strNode.append("index=\"");
            strNode.append(index);
            strNode.append("\"");
            strNode.append(logNodeInfo(node));
            strNode.append(">");
            for(int i = 0; i<node.getChildCount(); i++) {
                strNode.append(logNode2Xml(node.getChild(i), i));
            }
            strNode.append("</node>");
        }else{
            strNode.append("<node ");
            strNode.append("index=\"");
            strNode.append(index);
            strNode.append("\"");
            strNode.append(logNodeInfo(node));
            strNode.append("/>");
        }
        return strNode.toString();
    }

    private String logNodeInfo(AccessibilityNodeInfo node) {
        String rsl = "";
        Rect rc = new Rect();
        node.getBoundsInScreen(rc);
        rsl = ""
                + " text=\"" + (TextUtils.isEmpty(node.getText())?"":node.getText().toString().replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","&quot;").replace("\'","&apos;"))
                + "\" resource-id=\"" + (node.getViewIdResourceName()==null?"":node.getViewIdResourceName())
                + "\" class=\"" + (node.getClassName()==null?"":node.getClassName())
                + "\" package=\"" + (node.getPackageName()==null?"":node.getPackageName())
                + "\" content-desc=\"" + (node.getContentDescription()==null?"":node.getContentDescription().toString().replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","&quot;").replace("\'","&apos;"))
                + "\" checkable=\"" + node.isCheckable()
                + "\" checked=\"" + node.isChecked()
                + "\" clickable=\"" + node.isClickable()
                + "\" enabled=\"" + node.isEnabled()
                + "\" focusable=\"" + node.isFocusable()
                + "\" focused=\"" + node.isFocused()
                + "\" scrollable=\"" + node.isScrollable()
                + "\" long-clickable=\"" + node.isLongClickable()
                + "\" password=\"" + node.isPassword()
                + "\" selected=\"" + node.isSelected()
                + "\" bounds=\"[" + rc.left + "," + rc.top + "][" + rc.right + "," + rc.bottom + "]\""
                + "";
        return rsl;
    }
}

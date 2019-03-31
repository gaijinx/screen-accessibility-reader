package com.screenaccessibilityreader;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Worker implements Runnable {
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
            String data = mService.getRootInActiveWindow().toString();
            Log.d(TAG, "Sending data");
            writer.writeUTF(data);
            Log.d(TAG, "Reading ok");
            reader.readLine();
            Log.d(TAG, "Finished");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

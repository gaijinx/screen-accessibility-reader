package com.screenaccessibilityreader;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServiceServer implements Runnable {
    String TAG = "AccessibilityServer";
    AccessibilityService mService;
    ServerSocket mServer;

    ServiceServer(AccessibilityService accessibilityService){
        this.mService = accessibilityService;
    }

    @Override
    public void run() {
        while (true){
            try {
                Log.d(TAG, "Creating server");
                mServer = new ServerSocket(65432);
                while (true){
                    Log.d(TAG, "Waiting for connection");
                    Socket socket = mServer.accept();
                    Log.d(TAG, "Creating worker thread");
                    new Thread(new Worker(mService, socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

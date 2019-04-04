package com.screenaccessibilityreader;

import android.app.Application;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.test.ApplicationTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Test
    public void testAutomation() throws IOException {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice device = UiDevice.getInstance(instrumentation);
        ServerSocket serverSocket = new ServerSocket(65432);
        while (true) {
            Socket s = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String cmd = reader.readLine();
            if (cmd.equals("dump")){
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                device.dumpWindowHierarchy(s.getOutputStream());
                s.close();
            } else if (cmd.equals("screenshot")) {
                Bitmap bitmap = instrumentation.getUiAutomation().takeScreenshot();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, s.getOutputStream());
                s.close();
            }
        }
    }
}
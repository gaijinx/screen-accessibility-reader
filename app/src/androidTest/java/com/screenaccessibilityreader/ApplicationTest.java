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
            System.out.println("Waiting for connection");
            Socket s = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());

            System.out.println("Waiting for input");
            String cmd = reader.readLine();
            System.out.println(cmd);
            if (cmd.equals("dump")){
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                device.dumpWindowHierarchy(boas);
                sendData(boas.toByteArray(), outputStream);
            } else if (cmd.equals("screenshot")) {
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                Bitmap bitmap = instrumentation.getUiAutomation().takeScreenshot();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, boas);
                sendData(boas.toByteArray(), outputStream);
            }
            reader.readLine();
            s.close();
        }
    }

    private void sendData(byte[] data, DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(data.length);
        outputStream.write(data);
    }

}
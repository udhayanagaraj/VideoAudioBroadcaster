package com.example.Broadcast;

import com.example.Audio.AudioClient;
import com.example.Webcam.WebcamClient;

public class RunClient {

    private static String IP = "localhost";
    private static int PORT_A = 10001;
    private static int PORT_V = 10002;


    public static void main(String[] args) {
        new Thread( () -> {
            new AudioClient(IP,PORT_A).start();
        }).start();

        new Thread(() -> {
            new WebcamClient(IP,PORT_V).start();
        }).start();
    }
}

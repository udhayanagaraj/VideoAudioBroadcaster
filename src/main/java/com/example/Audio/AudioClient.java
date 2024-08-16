package com.example.Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class AudioClient {

    private String IP = "localhost";
    private int PORT = 10001;

    private Socket socket;
    private AudioFormat format;
    private DataLine.Info info;
    private ObjectInputStream ois;
    private SourceDataLine speakers;
    private byte[] data;
    private JFrame frame;

    public AudioClient(){

    }

    public AudioClient(String IP, int PORT) {
        this.IP = IP;
        this.PORT = PORT;
    }

    public void start(){
        try{
            socket = new Socket(IP,PORT);

            format = new AudioFormat(48000.0f,16,2,true,false);
            info = new DataLine.Info(SourceDataLine.class,format);
            ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            data = new byte[1024];

            speakers =(SourceDataLine) AudioSystem.getLine(info);
            speakers.open(format);
            speakers.start();

            frame = new JFrame("[Audio client] - server: "+IP+" -Port: "+PORT);
            frame.setSize(640,480);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            while(true){
                int dSize = ois.read(data);
                if(dSize == 1024){
                    speakers.write(data,0,dSize);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            speakers.drain();
            speakers.close();
        }
    }

    public static void main(String[] args) {
        new AudioClient().start();
    }
}

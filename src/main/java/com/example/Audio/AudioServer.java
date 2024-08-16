package com.example.Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AudioServer {
    private int PORT = 10001;

    private ArrayList<ObjectOutputStream> clients;
    private ServerSocket serverSocket;
    private Socket client;
    private ObjectOutputStream oos;
    private AudioFormat format;
    private DataLine.Info info;
    private TargetDataLine microphone;
    private byte[] data;
    private int dsize;
    private JFrame frame;


    public AudioServer(){
        clients = new ArrayList<>();
    }

    public AudioServer(int port){
        clients = new ArrayList<>();
        PORT = port;
    }

    public void start(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Audio server started");
            new broadcast().start();


            while(true){
                client = serverSocket.accept();
                oos = new ObjectOutputStream((new BufferedOutputStream(client.getOutputStream())));
                clients.add(oos);
                System.out.println("Connected from ["+ client.getInetAddress()+"]");
                System.out.println("Audio clients: "+clients.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class broadcast extends Thread{
        @Override
        public void run(){
            try{
                format = new AudioFormat(48000.0f,16,2,true,false);
                microphone = AudioSystem.getTargetDataLine(format);
                info = new DataLine.Info(TargetDataLine.class,format);
                data = new byte[1024];

                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();

                frame = new JFrame("[Audio server] - Host: "+ InetAddress.getLocalHost()+" -Port: "+PORT);
                frame.setSize(640,480);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            while (true) {
                try{
                    dsize = microphone.read(data,0,data.length);
                    int size = clients.size();

                    for(int i=0;i<size;i++){
                        oos = clients.get(i);
                        oos.write(data,0,dsize);
                        oos.reset();
                    }
                } catch (IOException e) {
                    clients.remove(oos);
                    System.out.println("Client disconnected");
                    System.out.println("Audio clients: "+clients.size());
                }
            }
        }
    }

    public static void main(String[] args) {
        new AudioServer().start();
    }
}

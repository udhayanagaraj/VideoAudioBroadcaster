package com.example.Webcam;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class WebcamClient {


    private String IP = "localhost";
    private int PORT = 10002;


    private Socket socket;
    private ObjectInputStream ois;
    private JFrame frame;
    private JLabel label;


    public WebcamClient(){

    }

    public WebcamClient(String ip,int port){
        this.IP = ip;
        this.PORT = port;
    }

    public void start(){
        try{
            socket = new Socket(IP,PORT);
            ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            frame = new JFrame("[Webcam Client]- Client: "+IP+" -Port: "+PORT);
            frame.setSize(640,480);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            label = new JLabel();
            label.setSize(640,480);
            label.setVisible(true);

            frame.add(label);
            frame.setVisible(true);

            while(true){
                label.setIcon((ImageIcon)ois.readObject());
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


    public static void main(String[] args){
        new WebcamClient().start();
    }
}

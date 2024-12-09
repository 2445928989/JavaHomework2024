package com.example.backend;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import com.example.frontend.*;

public class ClientReaderThread extends Thread {
    private Socket socket;
    private ObjectInputStream bis;
    private Window window;

    public ClientReaderThread(Socket socket, Window window) {
        this.socket = socket;
        this.window = window;
        try{
            bis = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (true) {
            try {
                Message message = (Message) bis.readObject();
                System.out.println(message.toString());
            } catch (Exception e) {
                if(socket.isClosed())return;
                else e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}

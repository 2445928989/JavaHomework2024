package com.example.backend;

import com.example.frontend.Window;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientWriterThread extends Thread {
    private Socket socket;
    private ObjectOutputStream bos;
    private Window clientWindow;
    private Message message;

    ClientWriterThread(Socket socket, ObjectOutputStream bos, Window clientWindow, Message message) {
        this.socket = socket;
        this.bos = bos;
        this.clientWindow = clientWindow;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            bos.writeObject(message);
            bos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
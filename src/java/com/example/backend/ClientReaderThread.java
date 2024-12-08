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
    private Window clientWindow;

    public ClientReaderThread(Socket socket, ObjectInputStream bis, Window clientWindow) {
        this.socket = socket;
        this.bis = bis;
        this.clientWindow = clientWindow;
    }
    @Override
    public void run() {
        String line = null;
        while (true) {
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = bis.read(buffer)) != -1) {
                    // 将字节数组转换为字符串并打印
                    String data = new String(buffer, 0, bytesRead,"utf-8");
                    clientWindow.jta.append(line + System.lineSeparator());
                }
            } catch (IOException e) {
                if(socket.isClosed())return;
                else e.printStackTrace();
            }
        }
    }
}

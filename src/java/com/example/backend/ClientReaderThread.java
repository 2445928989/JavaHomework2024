package com.example.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import com.example.frontend.*;

public class ClientReaderThread extends Thread {
    private BufferedReader br;
    private Window clientWindow;

    public ClientReaderThread(BufferedReader br, Window clientWindow) {
        this.br = br;
        this.clientWindow = clientWindow;
    }

    @Override
    public void run() {
        String line = null;
        while (true) {
            try {
                if ((line = br.readLine()) != null) {
                    clientWindow.jta.append(line + System.lineSeparator());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

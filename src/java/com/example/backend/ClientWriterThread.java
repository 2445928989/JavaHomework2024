package com.example.backend;

import com.example.frontend.Window;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientWriterThread extends Thread {
    private Socket socket;
    private ObjectOutputStream oos;
    private Window clientWindow;
    private BlockingQueue<Message> queue;
    ClientWriterThread(Socket socket, Window clientWindow, BlockingQueue<Message> queue) {
        this.socket = socket;
        this.clientWindow = clientWindow;
        this.queue = queue;
        try{
            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = queue.take(); // 阻塞直到获取到消息
                oos.writeObject(message);
                oos.flush();
                System.out.println("Sent message: " + message.getContent());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
package com.example.backend;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ServerWriterThread extends Thread {
    private ObjectOutputStream oos;
    private Socket socket;
    private BlockingQueue<Message> queue;
    ServerWriterThread(Socket socket , BlockingQueue<Message> queue) {
        this.socket = socket;
        this.queue = queue;
        try{
            oos=new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        try {
            while (true) {
                Message message = queue.peek(); // 阻塞直到获取到消息
                if(message.getDestinationId()==)
                oos.writeObject(message);
                oos.flush();
                System.out.println("Sent message: " + message.getContent());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

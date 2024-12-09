package com.example.backend;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ServerReaderThread extends Thread {
    private Socket socket;
    private ObjectInputStream ois;
    private BlockingQueue<Message> queue;

    public ServerReaderThread(Socket socket) {
        this.socket = socket;
        try {
            BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
            ois = new ObjectInputStream(is);
            System.out.println("收信线程已创建");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = (Message) ois.readObject();
                System.out.println(message.toString());
                if(message.getMessageType()==MessageType.LAUNCH){
                    queue.put(new Message(message.getSourceId(), null,0,MessageType.LAUNCH,message.getContent()));
                }
            } catch (Exception e) {
                try {
                    System.out.println(socket.getRemoteSocketAddress() + " 下线了");
                    Server.sockets.remove(socket);
                    ois.close();
                    socket.close();
                    break;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

//    private void sendMsgToAll(String msg) throws IOException {
//         //发送给全部在线的 socket 管道接收
//         synchronized (Server.sockets) {
//         for (Socket onLineSocket : Server.sockets) {
//         if (onLineSocket != socket) { // 避免发送给自己
//         BufferedWriter Bw = new BufferedWriter(new
//         OutputStreamWriter(onLineSocket.getOutputStream()));
//         Bw.write(msg);
//         Bw.newLine();
//         Bw.flush(); // 强制将缓冲区中的数据立即写入目的地，并清空缓冲区
//         }
//         }
//         }
//    }
}
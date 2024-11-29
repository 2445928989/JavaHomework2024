package com.example.backend;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import com.example.frontend.Window;

public class ServerReaderThread extends Thread {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;

    public ServerReaderThread(Socket socket) {
        this.socket = socket;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String msg;
        while (true) {
            try {
                msg = br.readLine();
                if (msg == null) {
                    break; // 客户端断开连接
                }
                System.out.println(socket.getRemoteSocketAddress() + "：" + msg);
                // 把这个消息分发给全部客户端进行接收
                sendMsgToAll(msg);
            } catch (IOException e) {
                try {
                    System.out.println(socket.getRemoteSocketAddress() + " 下线了");
                    Server.onLineSockets.remove(socket);
                    br.close();
                    bw.close();
                    socket.close();
                    break;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void sendMsgToAll(String msg) throws IOException {
        // 发送给全部在线的 socket 管道接收
        synchronized (Server.onLineSockets) {
            for (Socket onLineSocket : Server.onLineSockets) {
                if (onLineSocket != socket) { // 避免发送给自己
                    BufferedWriter Bw = new BufferedWriter(new OutputStreamWriter(onLineSocket.getOutputStream()));
                    Bw.write(msg);
                    Bw.newLine();
                    Bw.flush(); // 强制将缓冲区中的数据立即写入目的地，并清空缓冲区
                }
            }
        }
    }
}
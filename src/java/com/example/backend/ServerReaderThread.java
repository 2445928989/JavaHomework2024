package com.example.backend;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import com.example.frontend.Window;

public class ServerReaderThread extends Thread {
    private Socket socket;
    private BufferedInputStream bis;
    public ServerReaderThread(Socket socket) {
        this.socket = socket;
        try {
            bis = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String msg;
        while (true) {
            try {
//                msg = br.readLine();
//                if (msg == null) {
//                    break; // 客户端断开连接
//                }
//                System.out.println(socket.getRemoteSocketAddress() + "：" + msg);
//                // 把这个消息分发给全部客户端进行接收
//                sendMsgToAll(msg);
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = bis.read(buffer)) != -1) {
                    // 将字节数组转换为字符串并打印
                    String data = new String(buffer, 0, bytesRead,"utf-8");
                    System.out.println(data);
                }
            } catch (IOException e) {
                try {
                    System.out.println(socket.getRemoteSocketAddress() + " 下线了");
                    Server.onLineSockets.remove(socket);
                    bis.close();
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
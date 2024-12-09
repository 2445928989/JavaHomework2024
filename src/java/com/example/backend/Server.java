package com.example.backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    // 存储在线socket的集合
    static public Map<Integer,Socket>map = new HashMap<Integer,Socket>();
    static private BlockingQueue<Message> queue = new LinkedBlockingQueue<>(32);
    public static Set<Socket> sockets = new HashSet<Socket>();
    static private Thread serverWriterThread;

    public static void main(String[] args) throws IOException {
        System.out.println("-------------服务端启动成功--------------");
        // 1、创建服务端ServerSocket对象，并为服务端注册端口
        ServerSocket serverSocket = new ServerSocket(8888);// 要和客户端连接的端口号一致
        serverWriterThread = new ServerWriterThread(sockets,queue);
        serverWriterThread.start();

        while (true) {
            // 2、等待并接受客户端的连接请求，新建服务端的socket对象，形成socket管道
            Socket socket = serverSocket.accept();
            sockets.add(socket);
            System.out.println("有人上线了" + socket.getRemoteSocketAddress());

            // 3、把这个和客户端对应的socket通信管道，交给一个独立的线程来处理
            new ServerReaderThread(socket).start();
        }
    }
}

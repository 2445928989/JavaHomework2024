package socketapp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MySocketServer {
    public static void main(String[] args) {
        try {
            // 创建一个 socket 服务器
            ServerSocket serverSocket = new ServerSocket(11451);

            while (true) {
                // 服务器等待并接收客户端请求
                Socket socket = serverSocket.accept();
                // 创建线程收发信息
                new Thread(new ServerListen(socket)).start();
                new Thread(new ServerSend(socket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// 服务端监听客户端发来的消息
class ServerListen implements Runnable {
    private Socket socket;

    // 构造函数
    public ServerListen(Socket socket) {
        this.socket = socket;
    }

    // 覆写 run 方法
    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 监听消息
            while (true) {
                System.out.println(ois.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

// 服务器发送给客户端消息
class ServerSend implements Runnable {
    // 覆写 run 方法
    private Socket socket;

    // 构造函数
    public ServerSend(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oss = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("input:");
                oss.writeObject("aaa");
                oss.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
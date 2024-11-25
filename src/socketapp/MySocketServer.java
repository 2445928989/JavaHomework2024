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
            System.out.println("服务器已创建！");
            while (true) {
                // 服务器等待并接收客户端请求
                Socket socket = serverSocket.accept();
                System.out.println("连接到客户端！");
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
                // 将监听到的消息输出)
                System.out.println("来自客户端的消息：" + ois.readObject());
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
    private Socket socket;

    // 构造函数
    public ServerSend(Socket socket) {
        this.socket = socket;
    }

    // 覆写 run 方法
    @Override
    public void run() {
        try {
            ObjectOutputStream oss = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in, "gbk");// 从输入区读取数据

            while (true) {
                // oss.writeObject("aaa");
                String string = scanner.nextLine();
                oss.writeObject(string);// 向socket的OutputStream中输出消息
                oss.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
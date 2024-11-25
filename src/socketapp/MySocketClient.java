package socketapp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MySocketClient {
    private static Socket socket;
    private static boolean connectionState = false;

    public static void main(String[] args) {
        connect();
        if (connectionState == true) {
            new Thread(new ClientListen(socket)).start();
            new Thread(new ClientSend(socket)).start();
        }
    }

    // 尝试连接到服务器
    static void connect() {
        try {
            socket = new Socket("127.0.0.1", 11451);
            connectionState = true;
        } catch (Exception e) {
            connectionState = false;
            e.printStackTrace();
        }
    }
}

// 客户端监听服务端发来的消息
class ClientListen implements Runnable {
    private Socket socket;

    // 构造函数
    public ClientListen(Socket socket) {
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

// 客户端发送给服务端消息
class ClientSend implements Runnable {
    // 覆写 run 方法
    private Socket socket;

    // 构造函数
    public ClientSend(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oss = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while (true) {
                oss.writeObject("bbb");
                oss.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
package backend;

import frontend.LoginRegisterFrame;
import frontend.Window;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;


public class Client {
    public static void main(String[] args) {
        new Client();
    }

    // 客户端套接字
    private Socket socket;
    // 输入流
    private DataInputStream in;
    // 输出流
    private DataOutputStream out;
    // 聊天窗口实例
    private Window window;

    public Client() {
        // 启动登录注册流程
        new LoginRegisterFrame(this);
    }

    // 登录方法（这里只是模拟，实际应该与服务器交互验证）
    public boolean login(String username, String password) {
        // 这里可以添加与服务器通信验证登录的逻辑
        return true;
    }

    // 注册方法（这里只是模拟，实际应该与服务器交互注册用户）
    public boolean register(String username, String password) {
        // 这里可以添加与服务器通信注册用户的逻辑
        return true;
    }

    // 打开聊天窗口
    public void openChatWindow(String username) {
        try {
            socket = new Socket("localhost", 8888);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            // 发送用户名到服务器
            out.writeUTF(username);
            // 启动接收服务器消息的线程
            new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        if (window!= null) {
                            window.receiveMessageFromServer(message);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
            window = new Window(this);
            window.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送消息到服务器
    public void sendMessageToServer(String message, String chatType, String chatTarget) {
        try {
            out.writeUTF("MESSAGE:" + chatType + ":" + chatTarget + ":" + message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 发送文件到服务器
    public void sendFileToServer(File file, String chatType, String chatTarget) {
        try {
            out.writeUTF("FILE:" + chatType + ":" + chatTarget);
            // 发送文件内容
            //...
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 发送图片到服务器
    public void sendPictureToServer(File picture, String chatType, String chatTarget) {
        try {
            out.writeUTF("PICTURE:" + chatType + ":" + chatTarget);
            // 发送图片内容
            //...
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 关闭与服务器的连接
    public void closeConnection() {
        try {
            out.writeUTF("EXIT");
            out.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

package com.example.backend;

import com.example.frontend.LoginRegisterFrame;
import com.example.frontend.Window;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;

import static java.lang.Math.random;

public class Client {
    public static void main(String[] args) {
        new Client();
    }

    // 客户端套接字
    private Account account = null;
    private Boolean isConnected = false;
    private Window window;
    private Socket socket;
    private Thread readerThread = null;
    private Thread writerThread = null;
    private BlockingQueue<Message> queue = new LinkedBlockingQueue<>(32);//消息队列
    public Client() {
        // 启动登录注册流程
        try {
             //创建并显示“正在连接到服务器...”的对话框
             JDialog connectingDialog;
             connectingDialog = new JDialog(null, "提示", Dialog.ModalityType.MODELESS);
             connectingDialog.setContentPane(new JLabel("正在连接到服务器..."));
             connectingDialog.pack();
             connectingDialog.setLocationRelativeTo(null);
             connectingDialog.setVisible(true);
            int tryTimes = 0;
            int maxTryTimes = 10;
            while (!isConnected && tryTimes < maxTryTimes) {
                try {
                    tryTimes++;
                    this.socket = new Socket("127.0.0.1", 8888);
                    isConnected = true;
                } catch (Exception e) {
                    isConnected = false;
                    System.out.println("未成功连接到服务器，正在尝试重新连接...");
                    try {
                        Thread.sleep(1 * 1000);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            SwingUtilities.invokeLater(() -> connectingDialog.dispose());

            if (tryTimes == maxTryTimes) {
                System.out.println("尝试重连次数已达到最大值");
                JOptionPane.showMessageDialog(null, "无法连接到服务器，请检查您的网络连接！", "错误", JOptionPane.WARNING_MESSAGE);
                return;
            }
            System.out.println("成功连接到服务器");

            // 启动接收服务器消息的线程
            writerThread = new ClientWriterThread(socket,window,queue);
            writerThread.start();
            System.out.println("启动发信线程成功");
            Random randomConnectId = new Random();
            int connectId = randomConnectId.nextInt();
            queue.add(new Message(connectId,null,0,MessageType.LAUNCH,socket.getLocalAddress()+":"+socket.getLocalPort()));

            readerThread = new ClientReaderThread(socket, window);
            readerThread.start();
            System.out.println("启动收信线程成功");

            new LoginRegisterFrame(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            // 发送上线消息到服务

            window = new Window(this);
            System.out.println("创建主窗口成功");
            window.setVisible(true);
            System.out.println("主窗口已显示");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送消息到服务器
    public void sendMessageToServer(String message, String chatType, String chatTarget) {

    }

    // 发送文件到服务器
    public void sendFileToServer(File file, String chatType, String chatTarget) {

    }

    // 发送图片到服务器
    public void sendPictureToServer(File picture, String chatType, String chatTarget) {

    }

    public void sendExitToServer() {

    }

    // 关闭与服务器的连接
    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

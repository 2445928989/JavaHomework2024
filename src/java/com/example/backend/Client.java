package com.example.backend;

import com.example.frontend.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class Client implements ActionListener, KeyListener {
    public static void main(String[] args) throws IOException {
        new Client("127.0.0.1", 8999);
    }

    // 属性
    private Account account = null;

    private Boolean isConnected = false;
    private Window clientWindow;
    private Socket socket;
    private BufferedOutputStream bos = null;
    private BufferedInputStream bis = null;
    // 构造方法
    public Client(String host, int port) throws IOException {
        // 建立窗口
        clientWindow = new Window();
        clientWindow.setTitle("客户端");
        // 生成随机位置
        Random random = new Random();
        clientWindow.setLocation(random.nextInt(800), random.nextInt(600));
        clientWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 在关闭窗口时，释放资源
                try {
                    if (bos != null) {
                        bos.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                        isConnected = false;
                    }
                } catch (IOException ex) {
                    System.out.println("退出成功！" + socket.getRemoteSocketAddress() + "自己下线了");
                }
            }
        });

        int tryTimes = 0;
        int maxTryTimes = 10;
        while (!isConnected && tryTimes < maxTryTimes) {
            try {
                tryTimes++;
                socket = new Socket(host, port);
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
        if (tryTimes == maxTryTimes) {
            System.out.println("尝试重连次数已达到最大值");
            return;
        }

        bos = new BufferedOutputStream(socket.getOutputStream());

        sendFileToSocket("D:\\C++\\11.txt");
        // 给发送按钮绑定一个监听点击事件
        clientWindow.jb.addActionListener(this);
        // 给文本框绑定一个键盘点击事件
        clientWindow.jtf.addKeyListener(this);

        // 3、收数据（在子线程中进行）
        // 输入流包装
        bis = new BufferedInputStream(new BufferedInputStream(socket.getInputStream()));
        Thread readerThread = new ClientReaderThread(socket, bis, clientWindow);
        readerThread.start();
    }

    private void sendDataToSocket() throws IOException {
        String text = clientWindow.jtf.getText();
        text = "客户端：" + text;
        clientWindow.jta.append(text + System.lineSeparator());// 将输出内容展示在文本域（分行）

        bos.write(text.getBytes("utf-8"),0,text.getBytes("utf-8").length);
        bos.flush();// 强制将缓冲区中的数据立即写入目的地，并清空缓冲区
        clientWindow.jtf.setText("");
    }

    private void sendFileToSocket(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bfis = new BufferedInputStream(fis);
            System.out.println("try to send a file");
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = bfis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            System.out.println("send over");
            bos.flush();
            bfis.close();
            fis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            sendDataToSocket();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 回车键
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                sendDataToSocket();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

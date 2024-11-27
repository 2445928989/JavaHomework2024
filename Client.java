package test;

import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Client implements ActionListener, KeyListener {
    public static void main(String[] args) throws IOException {
        new Client("127.0.0.1",8999);
    }

    //属性
    private Window clientWindow;
    private Socket socket;
    private BufferedWriter bw=null;
    private BufferedReader br=null;

    //构造方法
    public Client(String host,int port) throws IOException {
        //建立窗口
        clientWindow=new Window();
        clientWindow.setTitle("客户端");
        // 生成随机位置
        Random random = new Random();
        clientWindow.setLocation(random.nextInt(800), random.nextInt(600));
        clientWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //在关闭窗口时，释放资源
                try {
                    bw.close();
                    br.close();
                    socket.close();
                } catch (IOException ex) {
                    System.out.println("退出成功！"+socket.getRemoteSocketAddress()+"自己下线了");
                }
            }
        });

        //——————————————————————————————————————网络通信——————————————————————————————————————————————
        //1、创建客户端socket对象，并请求与服务端连接
        socket=new Socket(host,port);

        //2、发数据
        // 输出流包装
        bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //给发送按钮绑定一个监听点击事件
        clientWindow.jb.addActionListener(this);
        //给文本框绑定一个键盘点击事件
        clientWindow.jtf.addKeyListener(this);

        //3、收数据（在子线程中进行）
        //输入流包装
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Thread readerThread = new ClientReaderThread(br, clientWindow);
        readerThread.start();
    }

    private void readDataFromSocket() throws IOException {
        String line=null;
        while((line=br.readLine())!=null){ //一行一行读取输入流的数据
            clientWindow.jta.append(line+System.lineSeparator());
        }
    }

    private void sendDataToSocket() throws IOException {
        String text=clientWindow.jtf.getText();
        text="客户端："+text;
        clientWindow.jta.append(text+System.lineSeparator());//将输出内容展示在文本域（分行）

        bw.write(text);
        bw.newLine();
        bw.flush();//强制将缓冲区中的数据立即写入目的地，并清空缓冲区
        clientWindow.jtf.setText("");
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
        //回车键
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
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

package test;

import javax.swing.*;
import java.awt.*;


public class Window extends JFrame  {
    //窗体组件
    JTextArea jta; //文本域
    private JScrollPane jsp; //滚动条
    private JPanel jp; //面板
    JTextField jtf; //文本框
    JButton jb; //按钮

    public Window(){
        jta=new JTextArea();
        jta.setEditable(false);//设置文本域不可编辑
        jsp=new JScrollPane(jta);//将文本域添加到滚动条中，实现滚动效果

        jp=new JPanel();
        jtf=new JTextField(10);//文本框长度为10
        jb=new JButton("发送");
        jp.add(jtf); jp.add(jb);//将文本框和按钮添加到面板

        //将滚动条和面板添加进窗体，并说明布局位置
        this.add(jsp, BorderLayout.CENTER);
        this.add(jp,BorderLayout.SOUTH);

        //设置标题，大小，位置，关闭，是否可见
        this.setSize(300,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//窗体关闭，程序结束
        this.setVisible(true);

    }

}

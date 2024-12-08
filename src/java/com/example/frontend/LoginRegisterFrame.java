package frontend;

import backend.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 登录注册窗口类
public class LoginRegisterFrame {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private Client client;

    public LoginRegisterFrame(Client client) {
        this.client = client;
        initLoginFrame();
    }

    // 初始化登录窗口
    private void initLoginFrame() {
        frame = new JFrame("登录");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("登录");
        JButton registerButton = new JButton("注册");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        frame.add(panel, BorderLayout.CENTER);

        // 登录按钮点击事件处理
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (client.login(username, password)) {
                    // 登录成功，关闭登录窗口，打开聊天窗口
                    frame.dispose();
                    client.openChatWindow(username);
                } else {
                    JOptionPane.showMessageDialog(frame, "登录失败，请检查用户名和密码。");
                }
            }
        });

        // 注册按钮点击事件处理
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 打开注册窗口
                initRegisterFrame();
            }
        });

        frame.setVisible(true);
    }

    // 初始化注册窗口
    private void initRegisterFrame() {
        JFrame registerFrame = new JFrame("注册");
        registerFrame.setSize(300, 250);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel usernameLabel = new JLabel("用户名:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordField = new JPasswordField();
        JButton registerButton = new JButton("注册");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(new JLabel());
        panel.add(registerButton);

        registerFrame.add(panel, BorderLayout.CENTER);

        // 注册按钮点击事件处理
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                if (password.equals(confirmPassword)) {
                    if (client.register(username, password)) {
                        JOptionPane.showMessageDialog(registerFrame, "注册成功！");
                        registerFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(registerFrame, "注册失败，用户名已存在。");
                    }
                } else {
                    JOptionPane.showMessageDialog(registerFrame, "两次输入的密码不一致。");
                }
            }
        });

        registerFrame.setVisible(true);
    }
}

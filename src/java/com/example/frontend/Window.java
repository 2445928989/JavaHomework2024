package frontend;

import backend.Client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Window extends JFrame {
    // 最侧边栏：头像及个人主页相关组件
    private JLabel avatarLabel;

    // 侧边栏：好友和群聊列表组件
    private JList<String> friendList;
    private JList<String> groupList;
    private DefaultListModel<String> friendListModel;
    private DefaultListModel<String> groupListModel;

    // 中间文本域：消息显示区域
    private JTextArea messageArea;

    // 下方面板：文本框及发送按钮等组件
    private JTextField inputTextField;
    private JButton sendButton;
    private JButton sendFileButton;
    private JButton sendPictureButton;

    // 右侧边栏（群聊时）：群成员列表组件
    private JList<String> memberList;
    private DefaultListModel<String> memberListModel;

    // 当前聊天类型（好友或群聊）
    private String currentChatType;
    // 当前聊天对象（好友昵称或群聊名称）
    private String currentChatTarget;

    // 与客户端实例关联
    private Client client;

    public Window(Client client) {
        this.client = client;
        initComponents();
        setupLayout();
        registerListeners();
    }

    private void initComponents() {
        // 初始化头像标签
        avatarLabel = new JLabel();
        avatarLabel.setIcon(new ImageIcon("default_avatar.jpg")); // 设置默认头像图片路径
        avatarLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        avatarLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                avatarLabelMouseClicked(evt);
            }
        });

        // 初始化好友列表模型和组件
        friendListModel = new DefaultListModel<>();
        friendList = new JList<>(friendListModel);
        friendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 初始化群聊列表模型和组件
        groupListModel = new DefaultListModel<>();
        groupList = new JList<>(groupListModel);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 初始化消息显示区域
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);

        // 初始化输入文本框
        inputTextField = new JTextField();

        // 初始化发送按钮
        sendButton = new JButton("发送");

        // 初始化发送文件按钮
        sendFileButton = new JButton("发送文件");

        // 初始化发送图片按钮
        sendPictureButton = new JButton("发送图片");

        // 初始化群成员列表模型和组件（初始隐藏，群聊时显示）
        memberListModel = new DefaultListModel<>();
        memberList = new JList<>(memberListModel);
        memberList.setVisible(false);
    }

    private void setupLayout() {
        // 设置窗口布局为 BorderLayout
        setLayout(new BorderLayout());

        // 最侧边栏（头像）放在 WEST 区域
        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.add(avatarLabel, BorderLayout.CENTER);
        add(avatarPanel, BorderLayout.WEST);

        // 好友和群聊列表放在 NORTH 区域
        JPanel sidePanel = new JPanel(new GridLayout(2, 1));
        sidePanel.add(new JScrollPane(friendList));
        sidePanel.add(new JScrollPane(groupList));
        add(sidePanel, BorderLayout.NORTH);

        // 中间消息显示区域放在 CENTER 区域
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // 下方面板（文本框和按钮）放在 SOUTH 区域
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputTextField, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(sendButton);
        buttonPanel.add(sendFileButton);
        buttonPanel.add(sendPictureButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // 右侧边栏（群成员列表）放在 EAST 区域
        JPanel memberPanel = new JPanel(new BorderLayout());
        memberPanel.add(new JLabel("群成员"), BorderLayout.NORTH);
        memberPanel.add(new JScrollPane(memberList), BorderLayout.CENTER);
        add(memberPanel, BorderLayout.EAST);

        // 设置窗口大小、标题等属性
        setSize(800, 600);
        setTitle("仿QQ聊天室");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // 处理窗口关闭事件，例如询问用户是否确认关闭
                int option = JOptionPane.showConfirmDialog(Window.this, "确定要退出吗？", "退出提示", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // 执行关闭操作，如关闭网络连接等
                    client.closeConnection();
                    System.exit(0);
                }
            }
        });
    }

    private void registerListeners() {
        // 好友列表点击事件
        friendList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                currentChatType = "好友";
                currentChatTarget = friendList.getSelectedValue();
                // 这里可以添加获取与该好友聊天记录并显示在消息区域的逻辑
                showChatHistory(currentChatType, currentChatTarget);
            }
        });

        // 群聊列表点击事件
        groupList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                currentChatType = "群聊";
                currentChatTarget = groupList.getSelectedValue();
                // 显示群成员列表
                memberList.setVisible(true);
                // 这里可以添加获取群成员信息并显示在右侧边栏的逻辑
                showGroupMembers(currentChatTarget);
                // 这里可以添加获取群聊天记录并显示在消息区域的逻辑
                showChatHistory(currentChatType, currentChatTarget);
            }
        });

        // 发送按钮点击事件
        sendButton.addActionListener(e -> {
            String message = inputTextField.getText();
            if (!message.isEmpty()) {
                client.sendMessageToServer(message, currentChatType, currentChatTarget);
                inputTextField.setText("");
            }
        });

        // 发送文件按钮点击事件
        sendFileButton.addActionListener(e -> {
            // 打开文件选择对话框，选择文件后发送文件到服务器
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                client.sendFileToServer(selectedFile, currentChatType, currentChatTarget);
            }
        });

        // 发送图片按钮点击事件
        sendPictureButton.addActionListener(e -> {
            // 打开图片选择对话框，选择图片后发送图片到服务器
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("图片文件", "jpg", "png", "gif"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                client.sendPictureToServer(selectedFile, currentChatType, currentChatTarget);
            }
        });
    }

    private void avatarLabelMouseClicked(MouseEvent evt) {
        // 点击头像打开个人主页，这里可以创建一个新的窗口或面板来显示个人主页信息
        JOptionPane.showMessageDialog(this, "个人主页功能待实现");
    }

    private void showChatHistory(String chatType, String chatTarget) {
        // 根据聊天类型和聊天对象从服务器获取聊天记录并显示在消息区域
        messageArea.setText("获取 " + chatType + " " + chatTarget + " 的聊天记录并显示在此处\n");
    }

    private void showGroupMembers(String groupName) {
        // 根据群聊名称从服务器获取群成员信息并显示在右侧边栏
        memberListModel.clear();
        // 假设从服务器获取到的群成员列表为 groupMembers
        String[] groupMembers = {"成员1", "成员2", "成员3"};
        for (String member : groupMembers) {
            memberListModel.addElement(member);
        }
    }

    // 接收服务器消息并显示在聊天窗口
    public void receiveMessageFromServer(String message) {
        messageArea.append(message + "\n");
    }

    // 在Window类中添加以下方法
    public DefaultListModel<String> getFriendListModel() {
        return friendListModel;
    }

    public DefaultListModel<String> getGroupListModel() {
        return groupListModel;
    }
}

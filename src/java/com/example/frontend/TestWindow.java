package frontend;

import frontend.Window;

import frontend.Window;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

public class TestWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window(null);

            // 给好友列表添加测试数据
            DefaultListModel<String> friendListModel = window.getFriendListModel();
            friendListModel.addElement("好友1");
            friendListModel.addElement("好友2");

            // 给群聊列表添加测试数据
            DefaultListModel<String> groupListModel = window.getGroupListModel();
            groupListModel.addElement("群聊1");
            groupListModel.addElement("群聊2");

            window.setVisible(true);
        });
    }
}

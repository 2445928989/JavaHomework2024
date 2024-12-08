package com.example.backend;

public class Message {
    private int sourceId; //消息来源
    private DetinationType detinationType;
    private int destinationId;//消息目的地
    private Object content;
    private MessageType messageType;

    Message(int sourceId,DetinationType detinationType, int destinationId, MessageType messageType,Object content) {
        this.sourceId = sourceId;
        this.detinationType = detinationType;
        this.destinationId = destinationId;
        this.content = content;
        this.messageType = messageType;
    }

}
enum DetinationType {
    USER,
    GROUP
}
enum MessageType {//枚举消息种类
    TEXT,
    FILE_DATA,
    IMAGE,
    END_OF_FILE
}
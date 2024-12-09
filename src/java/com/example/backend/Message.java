package com.example.backend;

import java.io.Serializable;

public class Message implements Serializable {
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
    public int getSourceId() {
        return sourceId;
    }
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
    public DetinationType getDetinationType() {
        return detinationType;
    }
    public void setDetinationType(DetinationType detinationType) {
        this.detinationType = detinationType;
    }
    public int getDestinationId() {
        return destinationId;
    }
    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }
    public Object getContent() {
        return content;
    }
    public void setContent(Object content) {
        this.content = content;
    }
    public MessageType getMessageType() {
        return messageType;
    }
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    @Override
    public String toString() {
        return "Message [sourceId=" + sourceId + ", detinationType=" + detinationType + ", destinationId=" + destinationId + ", content=" + content + ", messageType=" + messageType + "]";
    }
}
enum DetinationType {
    USER,
    GROUP
}
enum MessageType {//枚举消息种类
    LOGIN_REQUEST,
    REGISTER_REQUEST,
    TEXT,
    FILE_DATA,
    IMAGE,
    END_OF_FILE,
    EXIT,
    CONNECT,
    LAUNCH
}
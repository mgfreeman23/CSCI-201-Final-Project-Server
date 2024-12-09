package com.collegeapp.chat;

import java.sql.Timestamp;

public class Message {
    private Integer messageId;
    private Integer matchId;
    private Integer senderId;
    private String content;
    private Timestamp sentAt;
    private Timestamp expiresAt;
    private boolean isRead;

    // Constructor
    public Message(Integer matchId, Integer senderId, String content) {
        this.matchId = matchId;
        this.senderId = senderId;
        this.content = content;
        this.sentAt = new Timestamp(System.currentTimeMillis());
        this.expiresAt = new Timestamp(System.currentTimeMillis() + 24*60*60*1000);
    }

    // Getters and setters

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
    
    //getters and setters 
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public int getSenderId() {
        return senderId;
    }
        
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public Integer getMatchId() {
        return matchId;
    }
}

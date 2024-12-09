package com.collegeapp.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.collegeapp.DatabaseConnector;

import com.collegeapp.chat.Message;
import com.collegeapp.chat.MessageRepository;

public class ChatService {
    private final MessageRepository messageRepository;

    public ChatService() {
        this.messageRepository = new MessageRepository();
    }

    public List<Message> getMessages(Integer matchId) throws SQLException {
        return MessageRepository.findByMatchId(matchId);
    }

    public void sendMessage(Message message) throws SQLException {
        // Add message to database
        String query = "INSERT INTO messages (match_id, sender_id, content, expires_at) VALUES (?, ?, ?, DATE_ADD(NOW(), INTERVAL 24 HOUR))";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, message.getMessageId());
            stmt.setInt(2, message.getSenderId());
            stmt.setString(3, message.getContent());
            stmt.executeUpdate();
        }
    }


}
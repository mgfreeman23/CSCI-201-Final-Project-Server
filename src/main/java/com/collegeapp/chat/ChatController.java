package com.collegeapp.chat;

import java.sql.Connection;
import java.sql.Statement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.collegeapp.DatabaseConnector;  
import com.collegeapp.chat.Message;

public class ChatController {
	public static List<Message> getMessages(int matchId) throws SQLException {
	    List<Message> messages = new ArrayList<>();
	    String query = "SELECT * FROM messages WHERE match_id = ? AND expires_at > NOW() ORDER BY sent_at DESC";

	    try (Connection conn = DatabaseConnector.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, matchId);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Message message = new Message(
	                rs.getInt("match_id"),
	                rs.getInt("sender_id"),
	                rs.getString("content")
	            );
	            message.setMessageId(rs.getInt("message_id"));
	            
	            messages.add(message);
	        }
	    }
	    return messages;
	}

	public Message sendMessage(int matchId, int senderId, String content) throws SQLException {
	    String query = "INSERT INTO messages (match_id, sender_id, content, sent_at, expires_at) " +
	                  "VALUES (?, ?, ?, ?, ?)";

	    // Create message object first to get timestamps
	    Message message = new Message(matchId, senderId, content);

	    try (Connection conn = DatabaseConnector.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        
	        stmt.setInt(1, message.getMatchId());
	        stmt.setInt(2, message.getSenderId());
	        stmt.setString(3, message.getContent());
	        stmt.setTimestamp(4, message.getSentAt());
	        stmt.setTimestamp(5, message.getExpiresAt());
	        
	        stmt.executeUpdate();
	        
	        // Get the generated message ID
	        try (ResultSet rs = stmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                message.setMessageId(rs.getInt(1));
	            }
	        }
	        
	        return message;
	    }
	}
}

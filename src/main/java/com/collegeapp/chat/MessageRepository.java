package com.collegeapp.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.collegeapp.DatabaseConnector;  // Update import to match where DatabaseConnector actually is
import com.collegeapp.chat.Message;

public class MessageRepository {
    public static List<Message> findByMatchId(Integer matchId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM messages WHERE match_id = ? AND expires_at > NOW()";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, matchId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
        }
        return messages;
    }

    private static Message mapResultSetToMessage(ResultSet rs) throws SQLException {
        return new Message(
            rs.getInt("match_id"),
            rs.getInt("sender_id"),
            rs.getString("content")
        );
    }
}

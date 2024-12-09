package com.collegeapp;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.collegeapp.chat.Message;
import com.collegeapp.chat.ChatController;
import com.collegeapp.chat.ChatService;


public class Server {
    private List<User> users; // List of Users
    private Matching matching; // Matching object to be called when necessary (call findMatches(int userID) which returns a list of matches)
    private ChatService chatService;  // Add ChatService


    public Server() {
        this.users = new ArrayList<>();
        this.matching = new Matching(users);
        this.chatService = new ChatService();  // Initialize ChatService

    }

    public void run() {


        // Initialize chat functionality
        try {
        	
            // Your existing chat initialization
            initializeChat();
            
            // Initialize WebSocket server
            ChatController chatController = new ChatController();


        } catch (Exception e) {
            System.err.println("Error initializing chat: " + e.getMessage());
        }	
    }

    private void initializeChat() {
        try {
            // Verify database connection for chat functionality
            try (Connection conn = DatabaseConnector.getConnection()) {
                System.out.println("Chat database connection successful");
                
                // Check if messages table exists
                String checkTableQuery = "SELECT 1 FROM messages LIMIT 1";
                PreparedStatement stmt = conn.prepareStatement(checkTableQuery);
                stmt.executeQuery();
                
                // Set up message cleanup - remove expired messages
                scheduleMessageCleanup();
                
            } catch (SQLException e) {
                System.err.println("Error initializing chat database: " + e.getMessage());
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize chat system: " + e.getMessage());
        }
    }

    private void scheduleMessageCleanup() {
        // Create a thread to periodically clean up expired messages
        Thread cleanupThread = new Thread(() -> {
            while (true) {
                try {
                    // Delete expired messages
                    String cleanupQuery = "DELETE FROM messages WHERE expires_at <= NOW()";
                    try (Connection conn = DatabaseConnector.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(cleanupQuery)) {
                        int deleted = stmt.executeUpdate();
                        if (deleted > 0) {
                            System.out.println("Cleaned up " + deleted + " expired messages");
                        }
                    }
                    
                    // Sleep for an hour before next cleanup
                    Thread.sleep(60 * 60 * 1000); // 1 hour
                } catch (Exception e) {
                    System.err.println("Error in message cleanup: " + e.getMessage());
                }
            }
        });
        
        cleanupThread.setDaemon(true); // Make it a daemon thread so it doesn't prevent shutdown
        cleanupThread.start();
    }

    public void handleChatMessage(int matchId, int senderId, String content) {
    	Message mess = new Message(matchId, senderId, content);
        try {
            chatService.sendMessage(mess); // Changed from just content
        } catch (Exception e) {
            System.err.println("Error handling chat message: " + e.getMessage());
        }
    }

    public List<Message> getChatMessages(int matchId) {
        try {
            return chatService.getMessages(matchId);
        } catch (Exception e) {
            System.err.println("Error getting chat messages: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
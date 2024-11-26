CREATE DATABASE IF NOT EXISTS app_database;
USE app_database;


CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,   -- Unique identifier for each user (auto-increment integer)
    email_address VARCHAR(255) NOT NULL UNIQUE, -- User's email (must be unique)
    username VARCHAR(50) NOT NULL UNIQUE,     -- User's username (must be unique)
    password VARCHAR(255) NOT NULL,           -- Hashed password
    age INT,                                  -- User's age
    major VARCHAR(100),                       -- User's college major
    hometown VARCHAR(100),                    -- User's hometown
    instagram_handle VARCHAR(255)             -- User's Instagram handle
);

CREATE TABLE matches (
    match_id INT AUTO_INCREMENT PRIMARY KEY,  -- Unique identifier for each match (auto-increment integer)
    user1_id INT NOT NULL,                    -- First user in the match
    user2_id INT NOT NULL,                    -- Second user in the match
    matched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the match was created
    is_active BOOLEAN DEFAULT TRUE,           -- Whether the match is still active
    FOREIGN KEY (user1_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (user2_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE hobbies (
    hobby_id INT AUTO_INCREMENT PRIMARY KEY,  -- Unique identifier for each hobby
    user_id INT NOT NULL,                     -- User ID the hobby belongs to
    hobby VARCHAR(100) NOT NULL,              -- The hobby name
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each message
    match_id INT NOT NULL,                     -- Identifier for the match the message belongs to
    sender_id INT NOT NULL,                    -- User who sent the message
    content TEXT NOT NULL,                     -- The message content
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the message was sent
    expires_at TIMESTAMP,                      -- Expiration time for the message
    is_read BOOLEAN DEFAULT FALSE,             -- Whether the recipient has read the message
    FOREIGN KEY (match_id) REFERENCES matches(match_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE password_resets (
    secret_key CHAR(36) PRIMARY KEY,           -- Secret key for resetting password (still use UUID for this)
    user_id INT NOT NULL,                      -- User ID the reset request belongs to
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
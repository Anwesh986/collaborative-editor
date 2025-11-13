-- Insert initial test data for development
-- Note: These are sample users for testing purposes only

-- Insert test users (password will be properly encrypted in real implementation)
INSERT INTO users (email, first_name, last_name, username, password, is_verified, created_at) 
VALUES 
    ('admin@example.com', 'Admin', 'User', 'admin', X'74657374', true, CURRENT_TIMESTAMP),
    ('user@example.com', 'Test', 'User', 'testuser', X'74657374', false, CURRENT_TIMESTAMP),
    ('anweshp96@gmail.com', 'Anwesh', 'P16', 'anweshp16', X'74657374', true, CURRENT_TIMESTAMP),
    ('anweshp06@gmail.com', 'Anwesh', 'P06', 'anweshp06', X'74657374', true, CURRENT_TIMESTAMP),
    ('anweshpoff@gmail.com', 'anppna', '', 'anppna', X'74657374', true, CURRENT_TIMESTAMP);

-- Insert test rooms
INSERT INTO rooms (id, name, created_by) VALUES
    ('room1', 'Test Room 1', 'admin@example.com'),
    ('room2', 'Test Room 2', 'anweshp96@gmail.com');

-- Insert user-room relationships (simulate already joined user)
INSERT INTO user_rooms (user_id, room_id, is_active) VALUES
    ('admin@example.com', 'room1', 'Y'),
    ('anweshp96@gmail.com', 'room2', 'Y');


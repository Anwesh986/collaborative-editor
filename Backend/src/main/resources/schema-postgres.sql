-- PostgreSQL Schema for Production
-- Run this script on your PostgreSQL database after deployment

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password BYTEA,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_token VARCHAR(255),
    verification_token_expiry TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP,
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_verification_token ON users(verification_token);

-- Create rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_by VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rooms_created_by FOREIGN KEY (created_by) REFERENCES users(email) ON DELETE SET NULL
);

-- Create user_rooms join table
CREATE TABLE IF NOT EXISTS user_rooms (
    user_id VARCHAR(320) NOT NULL,
    room_id VARCHAR(50),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active VARCHAR(1) DEFAULT 'Y',
    PRIMARY KEY (user_id, room_id),
    CONSTRAINT fk_user_rooms_user FOREIGN KEY (user_id) REFERENCES users(email) ON DELETE CASCADE,
    CONSTRAINT fk_user_rooms_room FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    CONSTRAINT email_format_user_rooms CHECK (user_id ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Store current code state for each room
CREATE TABLE IF NOT EXISTS room_code (
    room_id VARCHAR(50) PRIMARY KEY,
    code TEXT,
    language VARCHAR(20) DEFAULT 'javascript',
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_room_code_room FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

-- Active sessions tracking
CREATE TABLE IF NOT EXISTS active_sessions (
    session_id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(320) NOT NULL,
    room_id VARCHAR(50) NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active VARCHAR(1) DEFAULT 'Y',
    CONSTRAINT fk_active_sessions_user FOREIGN KEY (user_id) REFERENCES users(email) ON DELETE CASCADE,
    CONSTRAINT fk_active_sessions_room FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

-- Create indexes for active_sessions
CREATE INDEX IF NOT EXISTS idx_active_sessions_user ON active_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_active_sessions_room ON active_sessions(room_id);
CREATE INDEX IF NOT EXISTS idx_active_sessions_is_active ON active_sessions(is_active);

-- Insert initial admin user (password: 'admin123' - CHANGE THIS!)
-- You should hash the password properly in your application
INSERT INTO users (email, first_name, last_name, username, password, is_verified, created_at) 
VALUES ('admin@yourdomain.com', 'Admin', 'User', 'admin', decode('61646d696e313233', 'hex'), true, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Grant necessary permissions (adjust based on your PostgreSQL setup)
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO your_db_user;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO your_db_user;

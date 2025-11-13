-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE 
        CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARBINARY(255),  -- Store encrypted password as bytes (nullable during registration)
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_token VARCHAR(255),
    verification_token_expiry TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    last_login_at TIMESTAMP
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
    FOREIGN KEY (created_by) REFERENCES users(email)
);

-- Create user_rooms join table
CREATE TABLE IF NOT EXISTS user_rooms (
    user_id VARCHAR(320) NOT NULL 
        CHECK (user_id REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    room_id VARCHAR(50),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active VARCHAR(1) DEFAULT 'Y',
    --T = intransition, A = active, I = inactive
    PRIMARY KEY (user_id, room_id),
    FOREIGN KEY (user_id) REFERENCES users(email),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

-- Store current code state for each room
CREATE TABLE IF NOT EXISTS room_code (
    room_id VARCHAR(50) PRIMARY KEY,
    code TEXT,
    language VARCHAR(20) DEFAULT 'javascript',
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(320),
    FOREIGN KEY (room_id) REFERENCES rooms(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(email)
);

-- Track active users in rooms (for showing online users)
CREATE TABLE IF NOT EXISTS active_sessions (
    session_id VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(320) NOT NULL,
    room_id VARCHAR(50) NOT NULL,
    connected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_heartbeat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cursor_position INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(email),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

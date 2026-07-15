
-- Global code editor - stores current code state
CREATE TABLE IF NOT EXISTS room_code (
    room_id VARCHAR(50) PRIMARY KEY,
    code TEXT,
    language VARCHAR(20) DEFAULT 'javascript',
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(100)
);

-- Track active users in the global editor
CREATE TABLE IF NOT EXISTS active_sessions (
    session_id VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    user_color VARCHAR(20),
    room_id VARCHAR(50) DEFAULT 'global',
    connected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_heartbeat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cursor_line INT DEFAULT 0,
    cursor_column INT DEFAULT 0
);

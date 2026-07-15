-- Seed data for global collaborative editor

-- Ensure the global document exists at startup
INSERT INTO room_code (room_id, code, language, last_modified_at, last_modified_by)
VALUES (
    'global',
    '// Welcome to the global collaborative editor\n// Everyone edits this shared document in real time.',
    'javascript',
    CURRENT_TIMESTAMP,
    'system'
);


CREATE TABLE chat_messages (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    timestamp BIGINT NOT NULL
);
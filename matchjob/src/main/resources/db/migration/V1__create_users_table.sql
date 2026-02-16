CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL,
    keyword VARCHAR(255),
    location VARCHAR(255),
    remote BOOLEAN DEFAULT FALSE,
    plan VARCHAR(50) DEFAULT 'FREE',
    role VARCHAR(50) DEFAULT 'USER',
    telegram_chat_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

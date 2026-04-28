-- V1__init_schema.sql
-- Created for Tool-66 Capstone Project

CREATE TABLE risk_events (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(100),
    impact_level VARCHAR(50), -- e.g., Low, Medium, High
    status VARCHAR(50) DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE -- Used for the "Soft Delete" requirement 
);

-- Indexing for performance as required by the project guide 
CREATE INDEX idx_risk_events_status ON risk_events(status);
CREATE INDEX idx_risk_events_title ON risk_events(title);
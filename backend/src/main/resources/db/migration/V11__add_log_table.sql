-- Adds the log entity table.

CREATE TABLE IF NOT EXISTS log (
    id BINARY(16) NOT NULL,
    created_date DATETIME(6) NULL,
    updated_date DATETIME(6) NULL,
    type VARCHAR(255) NOT NULL DEFAULT 'update',
    entity VARCHAR(255) NOT NULL DEFAULT '',
    entity_id VARCHAR(255) NOT NULL DEFAULT '',
    message VARCHAR(255) NOT NULL DEFAULT '',
    output MEDIUMTEXT NOT NULL DEFAULT (''),
    life_time VARBINARY(255) NOT NULL,
    log_expiry DATETIME(6) NULL,
    non_expiry BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    KEY idx_log_entity_entity_id (entity, entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

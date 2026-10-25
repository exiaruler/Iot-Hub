-- Adds ordered schedule transitions and route cycle flags.

SET @schema_name = DATABASE();

SET @sql = IF(
    NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = @schema_name
          AND table_name = 'route'
    )
    OR EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = @schema_name
          AND table_name = 'route'
          AND column_name = 'cycle_forward'
    ),
    'SELECT 1',
    'ALTER TABLE route ADD COLUMN cycle_forward BOOLEAN NOT NULL DEFAULT FALSE'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF(
    NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = @schema_name
          AND table_name = 'route'
    )
    OR EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = @schema_name
          AND table_name = 'route'
          AND column_name = 'cycle_back'
    ),
    'SELECT 1',
    'ALTER TABLE route ADD COLUMN cycle_back BOOLEAN NOT NULL DEFAULT FALSE'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS next_schedule (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_date DATETIME(6) NULL,
    updated_date DATETIME(6) NULL,
    schedule_order INT NOT NULL DEFAULT 0,
    schedule_id BIGINT NULL,
    next_schedule_id BIGINT NULL,
    PRIMARY KEY (id),
    KEY idx_next_schedule_schedule_id (schedule_id),
    KEY idx_next_schedule_next_schedule_id (next_schedule_id),
    CONSTRAINT fk_next_schedule_schedule
        FOREIGN KEY (schedule_id) REFERENCES schedule (id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_next_schedule_target
        FOREIGN KEY (next_schedule_id) REFERENCES schedule (id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

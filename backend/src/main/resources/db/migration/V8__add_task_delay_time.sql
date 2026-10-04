-- Adds the schedule interval delay to task records.

SET @schema_name = DATABASE();

SET @sql = IF(
    NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = @schema_name
          AND table_name = 'task'
    )
    OR EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = @schema_name
          AND table_name = 'task'
          AND column_name = 'delay_time'
    ),
    'SELECT 1',
    'ALTER TABLE task ADD COLUMN delay_time BIGINT NOT NULL DEFAULT 0 AFTER scheduled_time'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
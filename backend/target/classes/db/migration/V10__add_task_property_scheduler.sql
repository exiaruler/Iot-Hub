-- Adds the scheduler membership flag to task records.

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
          AND column_name = 'in_scheduler'
    ),
    'SELECT 1',
    'ALTER TABLE task ADD COLUMN in_scheduler BOOLEAN NOT NULL DEFAULT FALSE AFTER retry'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

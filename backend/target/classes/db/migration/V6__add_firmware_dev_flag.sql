-- Adds the development firmware flag.

SET @schema_name = DATABASE();

SET @sql = IF(
    NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = @schema_name
          AND table_name = 'firmware'
    )
    OR EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = @schema_name
          AND table_name = 'firmware'
          AND column_name = 'dev'
    ),
    'SELECT 1',
    'ALTER TABLE firmware ADD COLUMN dev BOOLEAN NOT NULL DEFAULT FALSE AFTER mandatory_update'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
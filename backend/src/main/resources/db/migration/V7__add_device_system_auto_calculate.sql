-- Adds the device system task auto-calculation flag.

SET @schema_name = DATABASE();

SET @sql = IF(
    NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = @schema_name
          AND table_name = 'device'
    )
    OR EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = @schema_name
          AND table_name = 'device'
          AND column_name = 'system_auto_calculate'
    ),
    'SELECT 1',
    'ALTER TABLE device ADD COLUMN system_auto_calculate BOOLEAN NOT NULL DEFAULT TRUE AFTER name'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
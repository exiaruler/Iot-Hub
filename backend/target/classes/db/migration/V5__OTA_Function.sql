-- V5__OTA_Function.SQL
-- Adds the firmware entity and the board columns used by the firmware OTA flow.

CREATE TABLE IF NOT EXISTS firmware (
    id BIGINT NOT NULL AUTO_INCREMENT,
    version VARCHAR(255) NOT NULL,
    major_version INT NOT NULL DEFAULT 0,
    minor_version INT NOT NULL DEFAULT 0,
    patch_version INT NOT NULL DEFAULT 0,
    latest BOOLEAN NOT NULL DEFAULT FALSE,
    mandatory_update BOOLEAN NOT NULL DEFAULT FALSE,
    notes TEXT NULL,
    main_file VARCHAR(2048) NOT NULL DEFAULT '',
    bootloader_file VARCHAR(2048) NOT NULL DEFAULT '',
    map_file VARCHAR(2048) NOT NULL DEFAULT '',
    partition_file VARCHAR(2048) NOT NULL DEFAULT '',
    created_date DATETIME(6) NULL,
    updated_date DATETIME(6) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_firmware_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET @schema_name = DATABASE();

SET @sql = IF(
    EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board'),
    'ALTER TABLE board ADD COLUMN board_key VARCHAR(255) NULL AFTER board_id',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'ssid'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN ssid VARCHAR(255) NULL AFTER name');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'mac_address'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN mac_address VARCHAR(255) NULL AFTER ssid');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'firmware_version'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN firmware_version VARCHAR(255) NOT NULL DEFAULT '' AFTER mac_address');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'firmware_id'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN firmware_id BIGINT NULL AFTER firmware_version');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'ip'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN ip VARCHAR(255) NULL AFTER firmware_id');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'status'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN status BOOLEAN NOT NULL DEFAULT FALSE AFTER ip');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'arest'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN arest BOOLEAN NOT NULL DEFAULT FALSE AFTER status');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'arest_command'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN arest_command BOOLEAN NOT NULL DEFAULT FALSE AFTER arest');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'socket'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN socket BOOLEAN NOT NULL DEFAULT FALSE AFTER arest_command');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'periodic_check'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN periodic_check INT NOT NULL DEFAULT 60000 AFTER socket');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'ram_usage'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN ram_usage INT NOT NULL DEFAULT 0 AFTER periodic_check');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'heap'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN heap INT NOT NULL DEFAULT 0 AFTER ram_usage');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'heap_total'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN heap_total INT NOT NULL DEFAULT 0 AFTER heap');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'millis'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN millis BIGINT NOT NULL DEFAULT 0 AFTER heap_total');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'activated'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN activated BOOLEAN NOT NULL DEFAULT FALSE AFTER millis');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'websocket_id'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN websocket_id VARCHAR(255) NOT NULL DEFAULT '' AFTER activated');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'dev_mode'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN dev_mode BOOLEAN NOT NULL DEFAULT FALSE AFTER websocket_id');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'dev_server_url'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN dev_server_url VARCHAR(255) NOT NULL DEFAULT '' AFTER dev_mode');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'dev_ws_url'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN dev_ws_url VARCHAR(255) NOT NULL DEFAULT '' AFTER dev_server_url');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'last_connect_date_time'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN last_connect_date_time DATETIME(6) NULL AFTER dev_ws_url');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'last_login_date_time'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN last_login_date_time DATETIME(6) NULL AFTER last_connect_date_time');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'activated_date_time'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN activated_date_time DATETIME(6) NULL AFTER last_login_date_time');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'timeout'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN timeout BIGINT NULL AFTER activated_date_time');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'restart_timeout'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN restart_timeout BOOLEAN NULL DEFAULT FALSE AFTER timeout');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'offline'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN offline BIGINT NOT NULL DEFAULT 3600000 AFTER restart_timeout');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'tasks_executed'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN tasks_executed INT NULL DEFAULT 0 AFTER offline');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'created_date'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN created_date DATETIME(6) NULL AFTER tasks_executed');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF(NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board') OR EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = @schema_name AND table_name = 'board' AND column_name = 'updated_date'), 'SELECT 1', 'ALTER TABLE board ADD COLUMN updated_date DATETIME(6) NULL AFTER created_date');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF(
    EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board')
    AND EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'firmware')
    AND NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_schema = @schema_name AND table_name = 'board' AND constraint_name = 'fk_board_firmware'),
    'ALTER TABLE board ADD CONSTRAINT fk_board_firmware FOREIGN KEY (firmware_id) REFERENCES firmware (id) ON DELETE SET NULL ON UPDATE CASCADE',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF(
    NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = @schema_name AND table_name = 'board')
    OR EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = @schema_name AND table_name = 'board' AND index_name = 'idx_board_firmware_id'),
    'SELECT 1',
    'CREATE INDEX idx_board_firmware_id ON board (firmware_id)'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

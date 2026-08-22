CREATE INDEX idx_board_task_id ON board_queue (board, board_task_id);
ALTER TABLE board_queue
	ADD COLUMN next_occurance DATETIME(6) NULL;
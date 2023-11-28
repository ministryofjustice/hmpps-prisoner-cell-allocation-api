ALTER TABLE cell_movement RENAME COLUMN date_time TO occurred_at;
TRUNCATE TABLE cell_movement;
ALTER TABLE cell_movement add COLUMN recorded_at TIMESTAMP NOT NULL;






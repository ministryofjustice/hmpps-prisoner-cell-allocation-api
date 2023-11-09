ALTER TABLE cell_movement ADD COLUMN move_type VARCHAR(255) NOT NULL;
ALTER TABLE cell_movement ALTER COLUMN cell_id SET NOT NULL;
ALTER TABLE cell_movement ALTER COLUMN cell_description SET NOT NULL;

CREATE INDEX move_type_idx ON "cell_movement"(move_type);


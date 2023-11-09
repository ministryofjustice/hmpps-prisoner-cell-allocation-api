ALTER TABLE cell_movement ADD COLUMN direction VARCHAR(255) NOT NULL;
ALTER TABLE cell_movement ALTER COLUMN cell_id SET NOT NULL;
ALTER TABLE cell_movement ALTER COLUMN cell_description SET NOT NULL;



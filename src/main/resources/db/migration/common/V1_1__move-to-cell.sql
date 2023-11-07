CREATE TABLE "move_to_cell"
(
    id serial NOT NULL constraint confirmed_arrival_pk PRIMARY KEY,
    agency VARCHAR(255) NOT NULL,
    cell_id BIGINT NOT NULL,
    cell_description VARCHAR(255) NOT NULL,
    prisoner_id VARCHAR(255) NOT NULL,
    prisoner_name VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    reason VARCHAR(255) NOT NULL
);

CREATE INDEX id_idx ON "move_to_cell"(id);
CREATE INDEX prisoner_id_idx ON "move_to_cell"(prisoner_id);
CREATE INDEX agency_idx ON "move_to_cell"(agency);
CREATE INDEX date_time_idx ON "move_to_cell"(date_time);

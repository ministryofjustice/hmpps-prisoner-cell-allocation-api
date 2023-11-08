CREATE TABLE "cell_movement"
(
    id serial NOT NULL constraint cell_movement_pk PRIMARY KEY,
    agency VARCHAR(255) NOT NULL,
    cell_id BIGINT,
    cell_description VARCHAR(255),
    prisoner_id VARCHAR(255) NOT NULL,
    prisoner_name VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    reason VARCHAR(255) NOT NULL
);

CREATE INDEX prisoner_id_idx ON "cell_movement"(prisoner_id);
CREATE INDEX agency_idx ON "cell_movement"(agency);
CREATE INDEX date_time_idx ON "cell_movement"(date_time);

TRUNCATE TABLE cell_movement RESTART IDENTITY;
insert into cell_movement (id, agency, nomis_cell_id, prisoner_id, prisoner_name, user_id, occurred_at, recorded_at, reason, direction) VALUES
(37, 'LII', 'NA-CELL-A2', '12345F', 'Non Association', 'USER1', to_timestamp('2020-01-08 01:01:01', 'YYYY-MM-DD HH:MI:SS'), to_timestamp('2020-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN');

TRUNCATE TABLE cell_movement RESTART IDENTITY;
insert into cell_movement (id, agency, nomis_cell_id, prisoner_id, prisoner_name, user_id, date_time, reason, direction) VALUES
(1, 'LII', 'LII-CELL-A', 'CURR-1', 'Current Prisoner', 'USER1', to_timestamp('2020-01-01 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN');

TRUNCATE TABLE cell_movement RESTART IDENTITY;
insert into cell_movement (id, agency, nomis_cell_id, prisoner_id, prisoner_name, user_id, occurred_at, recorded_at, reason, direction) VALUES
(35, 'LII', 'LII-CELL-A', 'LEFT-1', 'Former Prisoner', 'USER1', to_timestamp('2020-01-03 01:01:01', 'YYYY-MM-DD HH:MI:SS'), to_timestamp('2020-01-03 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN'),
(36, 'LII', 'LII-CELL-A', 'LEFT-1', 'Former Prisoner', 'USER1', to_timestamp('2020-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), to_timestamp('2020-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'OUT');

TRUNCATE TABLE cell_movement RESTART IDENTITY;
insert into cell_movement (id, agency,  nomis_cell_id, prisoner_id, prisoner_name, user_id, date_time, reason, direction) VALUES
(47, 'LII', 'LII-CELL-A', 'LEFT-2', 'Former Prisoner', 'USER1', to_timestamp('2020-01-03 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN'),
(48, 'LII', 'LII-CELL-A', 'LEFT-2', 'Former Prisoner', 'USER1', to_timestamp('2020-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'OUT'),
(49, 'LII', 'LII-CELL-A', 'LEFT-2', 'Former Prisoner', 'USER1', to_timestamp('2020-01-28 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN'),
(410, 'LII', 'LII-CELL-A', 'LEFT-2', 'Former Prisoner', 'USER1', to_timestamp('2021-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'OUT');

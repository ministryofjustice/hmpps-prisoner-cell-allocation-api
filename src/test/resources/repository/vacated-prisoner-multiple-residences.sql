TRUNCATE TABLE cell_movement RESTART IDENTITY;
insert into cell_movement (id, agency,  nomis_cell_id, prisoner_id, prisoner_name, user_id, occurred_at, recorded_at, reason, direction) VALUES
(47, 'LII', 'LII-CELL-A', 'LEFT-2', 'Former Prisoner', 'USER1', to_timestamp('2020-01-03 01:01:01', 'YYYY-MM-DD HH:MI:SS'), to_timestamp('2020-01-03 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data - stay one in', 'IN'),
(48, 'LII', 'LII-CELL-A', 'LEFT-2', 'Former Prisoner', 'USER1', to_timestamp('2020-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), to_timestamp('2020-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data - stay one out', 'OUT'),
(49, 'LII', 'LII-CELL-B', 'LEFT-2', 'Former Prisoner', 'USER1', to_timestamp('2020-01-28 01:01:01', 'YYYY-MM-DD HH:MI:SS'), to_timestamp('2020-01-28 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data - stay two out', 'IN'),
(410, 'LII', 'LII-CELL-B', 'LEFT-2', 'Former Prisoner', 'USER1', to_timestamp('2021-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), to_timestamp('2021-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data - stay two out', 'OUT');

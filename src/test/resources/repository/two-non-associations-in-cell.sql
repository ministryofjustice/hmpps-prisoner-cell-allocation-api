TRUNCATE TABLE cell_movement RESTART IDENTITY;
insert into cell_movement (id, agency, nomis_cell_id, prisoner_id, prisoner_name, user_id, occurred_at, recorded_at, reason, direction) VALUES
(77, 'LII', 'NA-CELL-B3', '12345J', 'Non Association', 'USER1', to_timestamp('2020-01-05 01:01:01', 'YYYY-MM-DD HH:MI:SS'), to_timestamp('2020-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN'),
(78, 'LII', 'NA-CELL-B3', '98765H', 'Non Association 2', 'USER1', to_timestamp('2020-01-06 01:01:01', 'YYYY-MM-DD HH:MI:SS'), to_timestamp('2020-01-04 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN');

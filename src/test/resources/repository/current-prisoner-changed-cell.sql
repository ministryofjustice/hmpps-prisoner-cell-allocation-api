TRUNCATE TABLE cell_movement RESTART IDENTITY;
insert into cell_movement (id, agency, nomis_cell_id, prisoner_id, prisoner_name, user_id, date_time, reason, direction) VALUES
    (22, 'LII', 'LII-CELL-A', 'CURR-2', 'Current Prisoner Two', 'USER1', to_timestamp('2020-01-01 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN'),
    (23, 'LII', 'LII-CELL-A', 'CURR-2', 'Current Prisoner Two', 'USER1', to_timestamp('2020-01-02 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'OUT'),
    (24, 'LII', 'LII-CELL-B', 'CURR-2', 'Current Prisoner Two', 'USER1', to_timestamp('2020-01-03 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN');
TRUNCATE TABLE cell_movement RESTART IDENTITY;
insert into cell_movement (id, agency, cell_id, cell_description, prisoner_id, prisoner_name, user_id, date_time, reason, direction) VALUES
    (22, 'LII', 1, 'LII-CELL-A', 'CURR-2', 'Current Prisoner Two', 'USER1', to_timestamp('2020-01-01 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN'),
    (23, 'LII', 1, 'LII-CELL-A', 'CURR-2', 'Current Prisoner Two', 'USER1', to_timestamp('2020-01-02 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'OUT'),
    (24, 'LII', 2, 'LII-CELL-B', 'CURR-2', 'Current Prisoner Two', 'USER1', to_timestamp('2020-01-03 01:01:01', 'YYYY-MM-DD HH:MI:SS'), 'Test data', 'IN');
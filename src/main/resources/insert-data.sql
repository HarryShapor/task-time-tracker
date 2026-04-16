DELETE FROM time_record;
DELETE FROM employee;
DELETE FROM task;

ALTER SEQUENCE employee_id_seq RESTART WITH 1;
ALTER SEQUENCE task_id_seq RESTART WITH 1;
ALTER SEQUENCE time_record_id_seq RESTART WITH 1;

INSERT INTO employee (firstname, lastname, middlename) VALUES
('Иван', 'Иванов', 'Иванович'),
('Петр', 'Петров', 'Петрович'),
('Андрей', 'Шапоренко', 'Сергеевич');

INSERT INTO task (title, description, status) VALUES
('Разработать API', 'Создать REST API для учета времени', 'NEW'),
('Написать тесты', 'Покрыть код unit-тестами', 'IN_PROGRESS'),
('Настроить Docker', 'Контейнеризировать приложение', 'DONE');
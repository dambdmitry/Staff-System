CREATE TABLE employees(
	employee_id BIGSERIAL NOT NULL PRIMARY KEY,
	last_name VARCHAR(100) NOT NULL,
	first_name VARCHAR(100) NOT NULL,
	patronymic VARCHAR(100) NOT NULL,
	employee_department_id BIGINT REFERENCES Departments(department_id)
);
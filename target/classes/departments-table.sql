CREATE TABLE Departments(
	department_id BIGSERIAL PRIMARY KEY,
	department_name VARCHAR(200) NOT NULL,
	parent_id BIGINT
);
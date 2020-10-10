CREATE TABLE orders(
	order_date DATE NOT NULL,
	order_number BIGINT NOT NULL,
	order_type VARCHAR(15) NOT NULL,
	department_id BIGINT REFERENCES Departments(department_id),
	employee_id BIGINT REFERENCES employees(employee_id) NOT NULL
);

ALTER TABLE orders ADD CONSTRAINT order_type CHECK(
	order_type = 'Увольнение' OR order_type = 'Перевод' OR order_type = 'Прием');
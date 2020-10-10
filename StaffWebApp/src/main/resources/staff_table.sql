DROP TABLE IF EXISTS worker;

CREATE TABLE worker(
	worker_id SERIAL,
	worker_name varchar(20),
	worker_patronymic varchar(20),
	worker_lastname varchar(20),
	PRIMARY KEY(worker_id)
)
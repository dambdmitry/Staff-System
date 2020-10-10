DROP TABLE IF EXISTS messages;

CREATE TABLE messages(
	login VARCHAR(120) NOT NULL,
	user_action VARCHAR(20) NOT NULL,
	action_object VARCHAR(30) NOT NULL
)
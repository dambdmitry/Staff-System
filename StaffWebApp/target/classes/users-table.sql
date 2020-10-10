CREATE TABLE users(
	username VARCHAR(120) NOT NULL,
	user_password VARCHAR(120) NOT NULL,
	user_role VARCHAR(20) NOT NULL
);

ALTER TABLE users ADD CONSTRAINT user_role CHECK(
		user_role = 'HR' OR user_role = 'EMPLOYEE');

INSERT INTO public.users(
	username, user_password, user_role)
	VALUES ('hr_employee', '$2y$12$9jmed3ZNU7qh2U/jSrt1we6nmCg.k1Yz8H2qDLS7HdX6SiMHgQDXG', 'HR'); /*пароль - qwerty*/

INSERT INTO public.users(
	username, user_password, user_role)
	VALUES ('base_employee', '$2y$12$MhJs51fOIQ1a7dRa9gre2uXVmjiYa776kaVDiPt8Exqs0ypo.tika', 'EMPLOYEE'); /*пароль - ytrewq*/
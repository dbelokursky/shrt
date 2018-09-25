CREATE DATABASE shrt;

CREATE TABLE shrt_user (
user_id SERIAL PRIMARY KEY,
login VARCHAR(300) UNIQUE,
password VARCHAR(300),
enabled BOOLEAN
);

CREATE TABLE url (
  url_id SERIAL PRIMARY KEY,
  original_url VARCHAR(300),
  hash VARCHAR(8),
  user_id INTEGER REFERENCES shrt_user(user_id),
  publication_date TIMESTAMP,
  redirect_code INT,
  click_counter INT,
  UNIQUE (hash, user_id)
);

CREATE TABLE role(
role_id SERIAL PRIMARY KEY,
name VARCHAR(20) UNIQUE
);

CREATE TABLE user_role(
user_id INTEGER REFERENCES shrt_user(user_id),
role_id INTEGER REFERENCES role(role_id),
UNIQUE (user_id, role_id)
);

-- Insert data
INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');

-- user1 with password1, user2 with password2 etc.
INSERT INTO shrt_user (login, password, enabled) VALUES ('user1', '$2a$10$eoIqcMQxa9oubfEa9wv.GOTpOaIudEQmCTr4F.jLzkerJuumAyu6C', TRUE);
INSERT INTO shrt_user (login, password, enabled) VALUES ('user2', '$2a$10$DpIj6lvOCFCrd461pfo9r.AP5koi6sN1g2SDJzLHYvHRzGjCFD6A.', TRUE);
INSERT INTO shrt_user (login, password, enabled) VALUES ('user3', '$2a$10$uoyWPgbo/ujJljySz.rUX.5qZBIFkGri9yuZJExWvJfHDZ/45nhnu', TRUE);

-- owner1 with role USER and owner2 role USER and ADMIN
INSERT INTO user_role(user_id, role_id) VALUES (1,1);
INSERT INTO user_role(user_id, role_id) VALUES (2,1);
INSERT INTO user_role(user_id, role_id) VALUES (2,2);


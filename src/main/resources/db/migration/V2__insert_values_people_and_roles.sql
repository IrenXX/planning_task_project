-- INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
-- INSERT INTO roles (name) VALUES ('ROLE_USER');
 INSERT INTO person (name, email, password, confirmed, status, role)
 VALUES ('admin', 'admin@admin.ru', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', true, 'ACTIVE', 'ROLE_ADMIN');
 INSERT INTO person (name, email, password, confirmed, status, role)
 VALUES ('user', 'user@user.ru', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', true, 'ACTIVE', 'ROLE_USER');
-- INSERT INTO people_roles (person_id, role_id) VALUES (1, 1);
-- INSERT INTO people_roles (person_id, role_id) VALUES (2, 2);
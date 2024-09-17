CREATE TABLE IF NOT EXISTS person
(
    id        SERIAL PRIMARY KEY NOT NULL,
    name      VARCHAR(50)        NOT NULL,
    email     VARCHAR(255)       NOT NULL UNIQUE,
    password  VARCHAR(255)       NOT NULL,
    confirmed BOOLEAN            NOT NULL,
    status    VARCHAR(50)        NOT NULL DEFAULT 'ACTIVE',
    role      VARCHAR(100)       NOT NULL DEFAULT 'ROLE_USER'
);
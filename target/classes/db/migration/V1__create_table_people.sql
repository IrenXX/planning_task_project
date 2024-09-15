CREATE TABLE IF NOT EXISTS person
(
    id        SERIAL PRIMARY KEY NOT NULL,
    status    VARCHAR(50)        NOT NULL DEFAULT 'ACTIVE',
    name      VARCHAR(50)        NOT NULL,
    email     VARCHAR(255)       NOT NULL UNIQUE,
    password  VARCHAR(255)       NOT NULL,
    confirmed BOOLEAN            NOT NULL
);

CREATE TABLE IF NOT EXISTS roles
(
    id      SERIAL PRIMARY KEY NOT NULL,
    status  VARCHAR(50) DEFAULT 'ACTIVE',
    name    VARCHAR(100)       NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS people_roles
(
    person_id   BIGINT    REFERENCES person (id) ON UPDATE RESTRICT ON DELETE CASCADE,
    role_id     BIGINT    REFERENCES roles (id) ON UPDATE RESTRICT ON DELETE CASCADE
)



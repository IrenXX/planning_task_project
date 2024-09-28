CREATE TABLE IF NOT EXISTS tasks
(
    id          SERIAL PRIMARY KEY NOT NULL,
    created     TIMESTAMP          NOT NULL,
    updated     TIMESTAMP          NOT NULL,
    title       VARCHAR(255)       NOT NULL,
    description VARCHAR,
    status      VARCHAR(50)        NOT NULL,
    finished    DATE,
    person_id   int                NOT NULL,
    CONSTRAINT person_id_fk FOREIGN KEY (person_id) REFERENCES person (id)
);
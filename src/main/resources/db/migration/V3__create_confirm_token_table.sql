CREATE TABLE IF NOT EXISTS confirm_tokens
(
    id            SERIAL          PRIMARY KEY NOT NULL,
    token         uuid            NOT NULL UNIQUE,
    person_id     int             NOT NULL,
    expired       TIMESTAMP       NOT NULL,
    CONSTRAINT person_id_fk FOREIGN KEY (person_id) REFERENCES person (id)
);
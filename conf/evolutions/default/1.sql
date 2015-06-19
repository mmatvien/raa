# --- !Ups

CREATE TABLE surveys (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR,
    zip VARCHAR,
    email VARCHAR,
    created DATETIME,
    q1 VARCHAR,
    q2 VARCHAR,
    q3 VARCHAR,
);

# --- !Downs

DROP TABLE IF EXISTS surveys;
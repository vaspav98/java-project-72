DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
   -- id          bigint PRIMARY KEY AUTO_INCREMENT,
    id          bigint GENERATED ALWAYS AS IDENTITY,
    name        varchar(255),
    created_at  timestamp
);

DROP TABLE IF EXISTS url_checks;

CREATE TABLE url_checks (
    id          bigint GENERATED ALWAYS AS IDENTITY,
    status_code integer,
    title       varchar(255),
    h1          varchar(255),
    description text,
    url_id      bigint,
    created_at  timestamp
);
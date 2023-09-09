DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id          bigint PRIMARY KEY AUTO_INCREMENT,
    name        varchar(255),
    created_at  timestamp
);
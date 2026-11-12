-- MySQL: the database is persistent, so drop the old table on each start
DROP TABLE IF EXISTS course;

CREATE TABLE course (
    id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code        varchar(100) NOT NULL,
    name        varchar(100) NOT NULL,
    description varchar(100),
    credits     int
);

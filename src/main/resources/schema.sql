DROP TABLE IF EXISTS friendly_relations;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS mpa_ratings;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS friendship_statuses;

CREATE TABLE users
(
    id        INT PRIMARY KEY,
    email     VARCHAR(50) UNIQUE,
    login     VARCHAR(20) UNIQUE,
    name      VARCHAR(50),
    birthdate DATE
);
CREATE INDEX idx_user_email ON users (email);

CREATE TABLE mpa_ratings
(
    id   INT PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE films
(
    id                  INT PRIMARY KEY,
    name                VARCHAR(50)  NOT NULL,
    description         VARCHAR(250) NOT NULL,
    release_date        DATE         NOT NULL,
    duration_in_minutes INT          NOT NULL,
    mpa_rating_id       INT          NOT NULL,
    FOREIGN KEY (mpa_rating_id) REFERENCES mpa_ratings (id)
);
CREATE INDEX idx_film_name ON films (name);

CREATE TABLE likes
(
    user_id INT NOT NULL,
    film_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (film_id) REFERENCES films (id)
);

CREATE TABLE friendship_statuses
(
    id   INT PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE friendly_relations
(
    user_id              INT NOT NULL,
    friend_id            INT NOT NULL,
    friendship_status_id INT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (friendship_status_id) REFERENCES friendship_statuses (id)
);

CREATE TABLE genres
(
    id   INT PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE film_genre
(
    film_id  INT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
)
DROP ALL OBJECTS;

CREATE TABLE users
(
    id        LONG PRIMARY KEY,
    email     VARCHAR(50) UNIQUE,
    login     VARCHAR(20) UNIQUE,
    name      VARCHAR(50),
    birthdate DATE
);

CREATE TABLE mpa_ratings
(
    id   LONG PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE films
(
    id                  LONG PRIMARY KEY,
    name                VARCHAR(50)  NOT NULL,
    description         VARCHAR(250) NOT NULL,
    release_date        DATE         NOT NULL,
    duration_in_minutes INT          NOT NULL,
    mpa_rating_id       INT          NOT NULL,
    FOREIGN KEY (mpa_rating_id) REFERENCES mpa_ratings (id)
);

CREATE TABLE likes
(
    user_id LONG NOT NULL,
    film_id LONG NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (film_id) REFERENCES films (id)
);

CREATE TABLE friendly_relations
(
    user_id              LONG NOT NULL,
    friend_id            LONG NOT NULL,
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE genres
(
    id   LONG PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE film_genre
(
    film_id  LONG NOT NULL,
    genre_id LONG NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
)
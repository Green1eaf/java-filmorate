CREATE TABLE IF NOT EXISTS users
(
    id        LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email     VARCHAR(50) UNIQUE,
    login     VARCHAR(20) UNIQUE,
    name      VARCHAR(50),
    birthdate DATE
);
CREATE INDEX IF NOT EXISTS idx_users_id ON users (id);

CREATE TABLE IF NOT EXISTS mpa_ratings
(
    id   LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS films
(
    id            LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          VARCHAR(50)  NOT NULL,
    description   VARCHAR(250) NOT NULL,
    release_date  DATE         NOT NULL,
    duration      INT          NOT NULL,
    mpa_rating_id LONG         NOT NULL,
    FOREIGN KEY (mpa_rating_id) REFERENCES mpa_ratings (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes
(
    user_id LONG NOT NULL,
    film_id LONG NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendly_relations
(
    user_id   LONG NOT NULL,
    friend_id LONG NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres
(
    id   LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  LONG NOT NULL,
    genre_id LONG NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE
);

INSERT INTO mpa_ratings (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO genres (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');
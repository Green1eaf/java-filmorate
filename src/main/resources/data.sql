INSERT INTO genres (name)
VALUES ('Боевик'),
       ('Комедия'),
       ('Исторический'),
       ('Фантастика'),
       ('Триллер'),
       ('Детектив'),
       ('Мелодрама'),
       ('Ужасы');

INSERT INTO mpa_ratings (name)
VALUES ('A'),
       ('B'),
       ('C'),
       ('D'),
       ('E'),
       ('F'),
       ('G');

INSERT INTO films (name, description, release_date, duration_in_minutes, mpa_rating_id)
VALUES ('matrix', 'about Neo', '2000-10-10', 120, 1),
       ('Terminator', 'about Terminator', '1986-1-10', 110, 3),
       ('Iron Man', 'about Tony Stark', '2010-3-12', 99, 2),
       ('Thor', 'about Thor', '2012-3-23', 102, 7),
       ('Star Wars', 'about Jedi', '1990-5-21', 130, 5);

INSERT INTO users (email, login, name, birthdate)
VALUES ('a@mail.ru', 'John', 'John Dow', '2000-10-20'),
       ('alex@mail.ru', 'Alex78', 'Alex Smith', '1998-8-10'),
       ('igor@ya.ru', 'Ig12', 'Igor Waste', '1998-7-23'),
       ('mari@mail.ru', 'mari92', 'Maria Hug', '1992-3-20'),
       ('sarah10@mail.ru', 'sara', 'Sarah Smith', '2010-3-14');

INSERT INTO film_genre (film_id, genre_id)
VALUES (1, 4),
       (1, 1),
       (2, 1),
       (3, 4),
       (3, 1),
       (4, 4),
       (4, 1),
       (5, 4);

INSERT INTO likes (user_id, film_id)
VALUES (1, 1),
       (1, 3),
       (1, 4),
       (2, 3),
       (2, 2),
       (3, 1),
       (3, 2),
       (3, 3),
       (3, 4),
       (4, 1),
       (4, 3),
       (4, 4),
       (4, 5),
       (5, 1),
       (5, 2),
       (5, 5);

INSERT INTO friendly_relations (user_id, friend_id)
VALUES (1, 2),
       (1, 3),
       (1, 5),
       (2, 1),
       (3, 1),
       (5, 1),
       (2, 3),
       (3, 2),
       (2, 4),
       (4, 2),
       (3, 4),
       (4, 3),
       (4, 5),
       (5, 4);
package ru.yandex.practicum.filmorate.exception;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super(message);
    }
}

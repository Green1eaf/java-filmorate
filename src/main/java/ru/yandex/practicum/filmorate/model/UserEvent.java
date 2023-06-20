package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserEvent {

    private Long eventId;

    @NotNull
    private Long timestamp;

    @NotNull
    private Long userId;

    @NotNull
    private String eventType;

    @NotNull
    private String operation;

    @NotNull
    private Long entityId;
}
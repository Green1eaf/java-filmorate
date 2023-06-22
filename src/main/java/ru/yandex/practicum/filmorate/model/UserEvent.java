package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {

    private Long eventId;

    @Builder.Default
    @NotNull
    private Long timestamp = System.currentTimeMillis();

    @NotNull
    private Long userId;

    @NotNull
    private String eventType;

    @NotNull
    private String operation;

    @NotNull
    private Long entityId;
}
package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String login;

    private String name;

    @Past
    private LocalDate birthdate;

    private Set<Long> friends;
}
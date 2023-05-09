package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
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
    private LocalDate birthday;

    private Set<Long> friends;

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name == null ? login : name;
        this.birthday = birthday;
    }

    public void addFriend(Long friend) {
        friends.add(friend);
    }

    public void removeFriend(Long friend) {
        friends.remove(friend);
    }
}

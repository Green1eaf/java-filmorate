package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .birthday((rs.getDate("birthdate")).toLocalDate())
//                .friends(parseToSet(rs.getString("friends")))
                .build();
    }

    private Set<Long> parseToSet(String friends) {
        return friends.isBlank() && friends.isEmpty() ? Collections.emptySet() :
                Arrays.stream(friends.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toSet());
    }
}

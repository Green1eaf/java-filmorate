package ru.yandex.practicum.filmorate.storage.mpa_rating;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MpaRatingDbStorageTest {

    private final MpaRatingStorage mpaRatingStorage;

    @Test
    void getAll() {
        List<Mpa> expected = List.of(
                new Mpa(1L, "G"),
                new Mpa(2L, "PG"),
                new Mpa(3L, "PG-13"),
                new Mpa(4L, "R"),
                new Mpa(5L, "NC-17")
        );
        Assertions.assertArrayEquals(expected.toArray(), mpaRatingStorage.getAll().toArray());
    }
}
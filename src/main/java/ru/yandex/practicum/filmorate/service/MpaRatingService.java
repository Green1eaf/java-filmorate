package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.util.List;

@Service
@Slf4j
public class MpaRatingService {

    private final MpaRatingStorage mpaRatingStorage;

    public MpaRatingService(MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public Mpa get(long id) {
        log.info("Get MpaRating with id=" + id);
        return mpaRatingStorage.get(id)
                .orElseThrow(() -> new NotExistException("Rating with id=" + id + " not exists"));
    }

    public List<Mpa> getAll() {
        log.info("Get all MpaRatings");
        return mpaRatingStorage.getAll();
    }
}

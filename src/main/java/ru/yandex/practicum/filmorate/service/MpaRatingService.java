package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.util.List;

@Service
public class MpaRatingService {

    private final MpaRatingStorage mpaRatingStorage;

    public MpaRatingService(MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public void add(MpaRating mpaRating) {
        mpaRatingStorage.add(mpaRating);
    }

    public void update(MpaRating mpaRating) {
        mpaRatingStorage.update(mpaRating);
    }

    public MpaRating get(long id) {
        return mpaRatingStorage.get(id);
    }

    public void delete(long id) {
        mpaRatingStorage.delete(id);
    }

    public List<MpaRating> getAll() {
        return mpaRatingStorage.getAll();
    }
}

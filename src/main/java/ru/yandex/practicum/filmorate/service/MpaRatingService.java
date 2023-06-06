package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.util.List;

@Service
public class MpaRatingService {

    private final MpaRatingStorage mpaRatingStorage;

    public MpaRatingService(MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public void add(Mpa mpa) {
        mpaRatingStorage.add(mpa);
    }

    public void update(Mpa mpa) {
        mpaRatingStorage.update(mpa);
    }

    public Mpa get(long id) {
        return mpaRatingStorage.get(id);
    }

    public void delete(long id) {
        mpaRatingStorage.delete(id);
    }

    public List<Mpa> getAll() {
        return mpaRatingStorage.getAll();
    }
}

package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {

    private final MpaRatingStorage mpaRatingStorage;

    public MpaRatingController(MpaRatingStorage mpaRatingService) {
        this.mpaRatingStorage = mpaRatingService;
    }

    @GetMapping
    public List<Mpa> getAll() {
        return mpaRatingStorage.getAll();
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable long id) {
        return mpaRatingStorage.get(id);
    }
}

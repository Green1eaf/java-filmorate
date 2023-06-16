package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {

    private final MpaRatingService mpaRatingService;

    public MpaRatingController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping
    public List<Mpa> getAll() {
        return mpaRatingService.getAll();
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable long id) {
        return mpaRatingService.get(id);
    }
}

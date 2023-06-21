package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public ResponseEntity<List<Director>> getAll() {
        return ResponseEntity.ok(directorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Director>> getGenre(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.getDirector(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Director> create(@Valid @RequestBody Director director) {
        return ResponseEntity.ok(directorService.create(director));
    }

    @PutMapping
    public ResponseEntity<Director> update(@Valid @RequestBody Director director) {
        return ResponseEntity.ok(directorService.update(director));
    }

    @DeleteMapping("/{id}")
    public void removeDirector(@PathVariable long id) {
        directorService.remove(id);
    }
}

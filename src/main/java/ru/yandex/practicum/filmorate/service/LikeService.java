package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.util.List;

@Service
public class LikeService {
    private  final LikeDbStorage likeDbStorage;

    public LikeService(LikeDbStorage likeDbStorage) {
        this.likeDbStorage = likeDbStorage;
    }

    public List<Integer> getAll(Integer id){
        return likeDbStorage.getAll(id);
    }
}

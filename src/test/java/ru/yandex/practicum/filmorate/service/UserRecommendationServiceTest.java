package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class UserRecommendationServiceTest {
    @Mock
    private UserStorage userStorage;

    @Mock
    private LikeService likeService;

    @Mock
    private FilmService filmService;

    @Mock
    private FeedService feedService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userStorage, likeService, feedService, filmService);
    }

    /**
     * когда у пользователя нет лайкнутых фильмов
     **/
    @Test
    public void testGetRecommendationFilms_noLikedFilms() {
        User user1 = User.builder().id(1L).login("user1").email("user1@example.com").build();
        when(userStorage.get(user1.getId())).thenReturn(user1);
        when(filmService.findAll()).thenReturn(new ArrayList<>());
        when(likeService.getAll(anyLong())).thenReturn(new ArrayList<>());

        List<Film> result = userService.getRecommendationFilms(user1.getId());

        assertTrue(result.isEmpty());
    }

    /**
     * когда у пользователя есть лайкнутые фильмы,
     * но нет других пользователей,
     * которые лайкнули те же фильмы
     **/
    @Test
    public void testGetRecommendationFilms_noOtherUsersLikedSameFilms() {
        User user = User.builder().id(1L).login("user1").email("user1@example.com").build();
        Film film = new Film();
        film.setId(1L);

        when(userStorage.get(user.getId())).thenReturn(user);
        when(filmService.findAll()).thenReturn(List.of(film));
        when(likeService.getAll(film.getId())).thenReturn(Collections.singletonList(user.getId()));
        List<Film> result = userService.getRecommendationFilms(user.getId());

        assertTrue(result.isEmpty());
    }

    /**
     * когда у пользователя есть лайкнутые фильмы
     * и есть другие пользователи, которые лайкнули те же фильмы,
     * но у этих пользователей нет других лайкнутых фильмов
     **/
    @Test
    public void testGetRecommendationFilms_otherUsersLikedSameFilmsButNoOtherLikedFilms() {
        User user = User.builder().id(1L).login("user1").email("user1@example.com").build();
        User otherUser = User.builder().id(2L).login("user2").email("user2@example.com").build();
        Film film = new Film();
        film.setId(1L);
        when(userStorage.get(user.getId())).thenReturn(user);
        when(userStorage.get(otherUser.getId())).thenReturn(otherUser);
        when(filmService.findAll()).thenReturn(List.of(film));
        when(likeService.getAll(film.getId())).thenReturn(Arrays.asList(user.getId(), otherUser.getId()));
        List<Film> result = userService.getRecommendationFilms(user.getId());

        assertTrue(result.isEmpty());
    }
}

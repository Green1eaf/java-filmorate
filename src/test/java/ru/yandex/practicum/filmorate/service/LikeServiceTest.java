package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private LikeDbStorage likeDbStorage;

    @Mock
    private UserService userService;

    @Mock
    private FeedService feedService;

    @Mock
    private FilmService filmService;

    @InjectMocks
    private LikeService likeService;

    @Test
    public void testGetAll() {
        List<Long> expected = Arrays.asList(1L, 2L, 3L);
        when(likeDbStorage.getAll(anyLong())).thenReturn(expected);
        List<Long> actual = likeService.getAll(1L);
        assertEquals(expected, actual);
        verify(likeDbStorage, times(1)).getAll(anyLong());
    }

    @Test
    public void testAdd() {
        doNothing().when(likeDbStorage).add(anyLong(), anyLong());
        likeService.add(1L, 2L);
        verify(likeDbStorage, times(1)).add(anyLong(), anyLong());
    }

    @Test
    public void testRemove() {
        doNothing().when(likeDbStorage).remove(anyLong(), anyLong());
        likeService.remove(1L, 2L);
        verify(likeDbStorage, times(1)).remove(anyLong(), anyLong());
    }

    @Test
    public void testLike() {
        doNothing().when(likeDbStorage).add(anyLong(), anyLong());
        when(userService.get(anyLong())).thenReturn(User.builder().id(1L).build());
        when(filmService.getById(anyLong())).thenReturn(Film.builder().id(1L).build());
        likeService.like(1L, 2L);
        verify(likeDbStorage, times(1)).add(anyLong(), anyLong());
        verify(userService, times(1)).get(anyLong());
        verify(filmService, times(1)).getById(anyLong());
        verify(feedService, times(1)).save(any(UserEvent.class));
    }

    @Test
    public void testRemoveLike() {
        doNothing().when(likeDbStorage).remove(anyLong(), anyLong());
        when(userService.get(anyLong())).thenReturn(User.builder().id(1L).build());
        when(filmService.getById(anyLong())).thenReturn(Film.builder().id(1L).build());
        likeService.removeLike(1L, 2L);
        verify(likeDbStorage, times(1)).remove(anyLong(), anyLong());
        verify(userService, times(1)).get(anyLong());
        verify(filmService, times(2)).getById(anyLong());
        verify(feedService, times(1)).save(any(UserEvent.class));
    }

    @Test
    public void testGetAllWhenDbThrowsException() {
        when(likeDbStorage.getAll(anyLong())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> likeService.getAll(1L));
    }

    @Test
    public void testAddWhenDbThrowsException() {
        doThrow(new RuntimeException()).when(likeDbStorage).add(anyLong(), anyLong());
        assertThrows(RuntimeException.class, () -> likeService.add(1L, 2L));
    }

    @Test
    public void testRemoveWhenDbThrowsException() {
        doThrow(new RuntimeException()).when(likeDbStorage).remove(anyLong(), anyLong());
        assertThrows(RuntimeException.class, () -> likeService.remove(1L, 2L));
    }

    @Test
    public void testLikeWhenDbThrowsException() {
        doThrow(new RuntimeException()).when(likeDbStorage).add(anyLong(), anyLong());
        assertThrows(RuntimeException.class, () -> likeService.like(1L, 2L));
    }

    @Test
    public void testRemoveLikeWhenDbThrowsException() {
        doThrow(new RuntimeException()).when(likeDbStorage).remove(anyLong(), anyLong());
        assertThrows(RuntimeException.class, () -> likeService.removeLike(1L, 2L));
    }

    @Test
    public void testLike_alreadyLiked() {
        User user = User.builder().id(1L).build();
        Film film = Film.builder().id(2L).build();
        when(userService.get(user.getId())).thenReturn(user);
        when(filmService.getById(film.getId())).thenReturn(film);
        when(likeDbStorage.getAll(film.getId())).thenReturn(Collections.singletonList(user.getId()));
        assertThrows(RuntimeException.class, () -> likeService.like(user.getId(), film.getId()));
    }

    @Test
    public void testRemoveLike_notLiked() {
        User user = User.builder().id(1L).build();
        Film film = Film.builder().id(2L).build();
        when(userService.get(user.getId())).thenReturn(user);
        when(filmService.getById(film.getId())).thenReturn(film);
        when(likeDbStorage.getAll(film.getId())).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> likeService.removeLike(user.getId(), film.getId()));
    }

    @Test
    public void testLike_filmNotFound() {
        User user = User.builder().id(1L).build();
        long filmId = 2L;
        when(userService.get(user.getId())).thenReturn(user);
        when(filmService.getById(filmId)).thenThrow(new RuntimeException("Film not found"));
        assertThrows(RuntimeException.class, () -> likeService.like(user.getId(), filmId));
    }

    @Test
    public void testRemoveLike_filmNotFound() {
        User user = User.builder().id(1L).build();
        long filmId = 2L;
        when(userService.get(user.getId())).thenReturn(user);
        when(filmService.getById(filmId)).thenThrow(new RuntimeException("Film not found"));
        assertThrows(RuntimeException.class, () -> likeService.removeLike(user.getId(), filmId));
    }

    @Test
    public void testRemoveLike_accessDenied() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        Film film = Film.builder().id(3L).build();
        when(userService.get(user1.getId())).thenReturn(user1);
        when(userService.get(user2.getId())).thenReturn(user2);
        when(filmService.getById(film.getId())).thenReturn(film);
        when(likeDbStorage.getAll(film.getId())).thenReturn(Collections.singletonList(user2.getId()));
        assertThrows(RuntimeException.class, () -> likeService.removeLike(user1.getId(), film.getId()));
    }
}

package com.habittracker.service;
/*
import com.habittracker.model.Habit;
import com.habittracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HabitServiceTest {

    @Mock
    private User user;

    @InjectMocks
    private HabitService habitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateHabit_Success() {
        when(user.getHabits()).thenReturn(new ArrayList<>());

        String result = habitService.createHabit(user, "Exercise", "Daily exercise", "daily");

        assertEquals("The habit has been successfully created.", result);
        verify(user, times(1)).getHabits();
    }

    @Test
    void testCreateHabit_AlreadyExists() {
        List<Habit> habits = new ArrayList<>();
        habits.add(new Habit("Exercise", "Daily exercise", "daily"));
        when(user.getHabits()).thenReturn(habits);

        String result = habitService.createHabit(user, "Exercise", "Daily exercise", "daily");

        assertEquals("Error: A habit with this name already exists.", result);
        verify(user, times(1)).getHabits();
    }

    @Test
    void testUpdateHabit_Success() {
        List<Habit> habits = new ArrayList<>();
        Habit habit = new Habit("Exercise", "Daily exercise", "daily");
        habits.add(habit);
        when(user.getHabits()).thenReturn(habits);

        String result = habitService.updateHabit(user, "Exercise", "New Exercise", "New description", "weekly");

        assertEquals("The habit has been successfully renewed.", result);
        assertEquals("New Exercise", habit.getName());
        assertEquals("New description", habit.getDescription());
        assertEquals("weekly", habit.getFrequency());
        verify(user, times(1)).getHabits();
    }

    @Test
    void testUpdateHabit_NotFound() {
        when(user.getHabits()).thenReturn(new ArrayList<>());

        String result = habitService.updateHabit(user, "Exercise", "New Exercise", "New description", "weekly");

        assertEquals("Habit is not found.", result);
        verify(user, times(1)).getHabits();
    }

    @Test
    void testDeleteHabit_Success() {
        List<Habit> habits = new ArrayList<>();
        Habit habit = new Habit("Exercise", "Daily exercise", "daily");
        habits.add(habit);
        when(user.getHabits()).thenReturn(habits);

        String result = habitService.deleteHabit(user, "Exercise");

        assertEquals("The habit has been successfully removed along with its completion history.", result);
        assertTrue(habits.isEmpty());
        verify(user, times(1)).getHabits();
    }

    @Test
    void testDeleteHabit_NotFound() {
        when(user.getHabits()).thenReturn(new ArrayList<>());

        String result = habitService.deleteHabit(user, "Exercise");

        assertEquals("Habit is not found.", result);
        verify(user, times(1)).getHabits();
    }

    @Test
    void testGetAllHabits() {
        List<Habit> habits = new ArrayList<>();
        habits.add(new Habit("Exercise", "Daily exercise", "daily"));
        when(user.getHabits()).thenReturn(habits);

        List<Habit> result = habitService.getAllHabits(user);

        assertEquals(habits, result);
        verify(user, times(1)).getHabits();
    }

    @Test
    void testFindHabitByName_Success() {
        List<Habit> habits = new ArrayList<>();
        Habit habit = new Habit("Exercise", "Daily exercise", "daily");
        habits.add(habit);
        when(user.getHabits()).thenReturn(habits);

        Habit result = habitService.findHabitByName(user, "Exercise");

        assertEquals(habit, result);
        verify(user, times(1)).getHabits();
    }

    @Test
    void testFindHabitByName_NotFound() {
        when(user.getHabits()).thenReturn(new ArrayList<>());

        Habit result = habitService.findHabitByName(user, "Exercise");

        assertNull(result);
        verify(user, times(1)).getHabits();
    }

}*/
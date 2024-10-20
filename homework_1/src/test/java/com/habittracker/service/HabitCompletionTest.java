package com.habittracker.service;

import com.habittracker.model.Habit;
import com.habittracker.model.User;
import com.habittracker.model.HabitCompletion;
import com.habittracker.repository.HabitCompletionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HabitCompletionTest {
    private HabitCompletionService habitCompletionService;
    private HabitCompletionRepository habitCompletionRepository;
    private Habit testHabit;

    @BeforeEach
    void setUp() {
        habitCompletionRepository = mock(HabitCompletionRepository.class);
        habitCompletionService = new HabitCompletionService();
        habitCompletionService.habitCompletionRepository = habitCompletionRepository; // Подмена репозитория моком

        testHabit = new Habit("Test Habit", "Test Description", Habit.Frequency.DAILY, new User("Test User", "test@example.com", "password", User.Role.USER));
    }

    @Test
    void testCreateHabitCompletion_Success() {
        LocalDate date = LocalDate.now();

        when(habitCompletionRepository.addHabitCompletion(eq(testHabit), any(HabitCompletion.class))).thenReturn(true);

        String result = habitCompletionService.createHabitCompletion(date, testHabit);

        assertEquals("Habit successfully marked.", result);

        ArgumentCaptor<HabitCompletion> habitCompletionCaptor = ArgumentCaptor.forClass(HabitCompletion.class);
        verify(habitCompletionRepository).addHabitCompletion(eq(testHabit), habitCompletionCaptor.capture());

        HabitCompletion capturedCompletion = habitCompletionCaptor.getValue();
        assertEquals(date, capturedCompletion.getMarkDate());
        assertEquals(testHabit, capturedCompletion.getHabit());
    }

    @Test
    void testCreateHabitCompletion_Failure() {
        LocalDate date = LocalDate.now();

        when(habitCompletionRepository.addHabitCompletion(eq(testHabit), any(HabitCompletion.class))).thenReturn(false);

        String result = habitCompletionService.createHabitCompletion(date, testHabit);

        assertEquals("Error.", result);

        verify(habitCompletionRepository).addHabitCompletion(eq(testHabit), any(HabitCompletion.class));
    }

    @Test
    void testDeleteAllHabitCompletion_Success() {
        habitCompletionService.deleteAllHabitCompletion(testHabit);

        verify(habitCompletionRepository, times(1)).deleteAllHabitCompletion(testHabit);
    }

    @Test
    void testDeleteAllHabitCompletion_NoInteractionWhenNoHabit() {
        Habit nonExistentHabit = null;

        habitCompletionService.deleteAllHabitCompletion(nonExistentHabit);

        verify(habitCompletionRepository, never()).deleteAllHabitCompletion(any());
    }

    @Test
    void testGetAllHabitCompletions_Success() {
        List<HabitCompletion> mockCompletions = Arrays.asList(
                new HabitCompletion(LocalDate.now(), testHabit),
                new HabitCompletion(LocalDate.now().minusDays(1), testHabit)
        );

        when(habitCompletionRepository.getAllHabitCompletion(testHabit)).thenReturn(mockCompletions);

        List<HabitCompletion> result = habitCompletionService.getAllHabitCompletions(testHabit);

        assertEquals(2, result.size());
        assertEquals(mockCompletions, result);

        verify(habitCompletionRepository, times(1)).getAllHabitCompletion(testHabit);
    }

    @Test
    void testGetAllHabitCompletions_EmptyList() {
        when(habitCompletionRepository.getAllHabitCompletion(testHabit)).thenReturn(Collections.emptyList());

        List<HabitCompletion> result = habitCompletionService.getAllHabitCompletions(testHabit);

        assertTrue(result.isEmpty());

        verify(habitCompletionRepository, times(1)).getAllHabitCompletion(testHabit);
    }

    @Test
    void testGetAllHabitCompletions_NullHabit() {
        List<HabitCompletion> result = habitCompletionService.getAllHabitCompletions(null);

        assertTrue(result.isEmpty());

        verify(habitCompletionRepository, never()).getAllHabitCompletion(any());
    }
}



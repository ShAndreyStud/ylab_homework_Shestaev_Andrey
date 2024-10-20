package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;
import com.habittracker.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HabitCompletionRepositoryImplTest {
    private HabitCompletionRepositoryImpl habitCompletionRepository;
    private Habit testHabit;
    private HabitCompletion testCompletion;

    @BeforeEach
    void setUp() {
        habitCompletionRepository = new HabitCompletionRepositoryImpl();
        testHabit = new Habit("Test Habit", "Description", Habit.Frequency.DAILY, new User("Test User", "test@example.com", "password", User.Role.USER));
        testCompletion = new HabitCompletion(LocalDate.now(), testHabit);

    }

    @Test
    void testGetHabitCompletion_ExistingCompletion() {
        habitCompletionRepository.addHabitCompletion(testHabit, testCompletion);

        HabitCompletion retrievedCompletion = habitCompletionRepository.getHabitCompletion(testHabit, testCompletion.getSerialNumber());

        assertNotNull(retrievedCompletion);
        assertEquals(testCompletion.getSerialNumber(), retrievedCompletion.getSerialNumber());
        assertEquals(testCompletion.getMarkDate(), retrievedCompletion.getMarkDate());
    }

    @Test
    void testGetHabitCompletion_NonExistentCompletion() {
        HabitCompletion retrievedCompletion = habitCompletionRepository.getHabitCompletion(testHabit, 999);

        assertNull(retrievedCompletion);
    }

    @Test
    void testGetHabitCompletion_NonExistentHabit() {
        Habit anotherHabit = new Habit("Another Habit", "Description", Habit.Frequency.WEEKLY, new User("Another User", "another@example.com", "password", User.Role.USER));

        HabitCompletion retrievedCompletion = habitCompletionRepository.getHabitCompletion(anotherHabit, 1);

        assertNull(retrievedCompletion);
    }

    @Test
    void testAddHabitCompletion_NewCompletion() {
        boolean result = habitCompletionRepository.addHabitCompletion(testHabit, testCompletion);

        assertTrue(result);

        HabitCompletion retrievedCompletion = habitCompletionRepository.getHabitCompletion(testHabit, testCompletion.getSerialNumber());
        assertNotNull(retrievedCompletion);
        assertEquals(testCompletion.getSerialNumber(), retrievedCompletion.getSerialNumber());
    }

    @Test
    void testAddHabitCompletion_ExistingCompletion() {
        habitCompletionRepository.addHabitCompletion(testHabit, testCompletion);

        boolean result = habitCompletionRepository.addHabitCompletion(testHabit, testCompletion);

        assertFalse(result);
    }

    @Test
    void testAddHabitCompletion_NewHabit() {
        Habit anotherHabit = new Habit("Another Habit", "Description", Habit.Frequency.WEEKLY, new User("Another User", "another@example.com", "password", User.Role.USER));
        HabitCompletion anotherCompletion = new HabitCompletion(LocalDate.now(), anotherHabit);

        boolean result = habitCompletionRepository.addHabitCompletion(anotherHabit, anotherCompletion);

        assertTrue(result);

        HabitCompletion retrievedCompletion = habitCompletionRepository.getHabitCompletion(anotherHabit, anotherCompletion.getSerialNumber());
        assertNotNull(retrievedCompletion);
        assertEquals(anotherCompletion.getSerialNumber(), retrievedCompletion.getSerialNumber());
    }


    @Test
    void testUpdateHabitCompletion_NonExistentSerialNumber() {
        HabitCompletion updatedCompletion = new HabitCompletion(LocalDate.now().plusDays(1), testHabit);

        boolean result = habitCompletionRepository.updateHabitCompletion(testHabit, 999, updatedCompletion);

        assertFalse(result);
    }

    @Test
    void testUpdateHabitCompletion_EmptyCompletions() {
        HabitCompletionRepositoryImpl emptyRepository = new HabitCompletionRepositoryImpl();

        boolean result = emptyRepository.updateHabitCompletion(testHabit, testCompletion.getSerialNumber(), testCompletion);

        assertFalse(result);
    }

    @Test
    void testDeleteHabitCompletion_Success() {
        habitCompletionRepository.addHabitCompletion(testHabit, testCompletion);
        boolean result = habitCompletionRepository.deleteHabitCompletion(testHabit, testCompletion.getSerialNumber());

        assertTrue(result);

        HabitCompletion retrievedCompletion = habitCompletionRepository.getHabitCompletion(testHabit, testCompletion.getSerialNumber());
        assertNull(retrievedCompletion);
    }

    @Test
    void testDeleteHabitCompletion_NonExistentSerialNumber() {
        boolean result = habitCompletionRepository.deleteHabitCompletion(testHabit, 999);

        assertFalse(result);
    }

    @Test
    void testDeleteHabitCompletion_EmptyCompletions() {
        HabitCompletionRepositoryImpl emptyRepository = new HabitCompletionRepositoryImpl();
        boolean result = emptyRepository.deleteHabitCompletion(testHabit, testCompletion.getSerialNumber());

        assertFalse(result);
    }

    @Test
    void testDeleteAllHabitCompletion_Success() {
        boolean result = habitCompletionRepository.deleteAllHabitCompletion(testHabit);

        assertTrue(result);

        HabitCompletion retrievedCompletion = habitCompletionRepository.getHabitCompletion(testHabit, testCompletion.getSerialNumber());
        assertNull(retrievedCompletion);
    }

    @Test
    void testGetAllHabitCompletion_Success() {
       HabitCompletion testCompletion1 = new HabitCompletion(LocalDate.now(), testHabit);
        habitCompletionRepository.addHabitCompletion(testHabit, testCompletion1);

        HabitCompletion testCompletion2 = new HabitCompletion(LocalDate.now().minusDays(1), testHabit);
        habitCompletionRepository.addHabitCompletion(testHabit, testCompletion2);

        List<HabitCompletion> completions = habitCompletionRepository.getAllHabitCompletion(testHabit);

        assertEquals(2, completions.size());
        assertTrue(completions.contains(testCompletion1));
        assertTrue(completions.contains(testCompletion2));
    }

    @Test
    void testGetAllHabitCompletion_EmptyList() {
        Habit anotherHabit = new Habit("Another Habit", "Description", Habit.Frequency.WEEKLY, new User("Another User", "another@example.com", "password", User.Role.USER));

        List<HabitCompletion> completions = habitCompletionRepository.getAllHabitCompletion(anotherHabit);

        assertTrue(completions.isEmpty());
    }

    @Test
    void testGetAllHabitCompletionByDate_Success() {
        habitCompletionRepository.addHabitCompletion(testHabit, new HabitCompletion(LocalDate.now(), testHabit));
        List<HabitCompletion> completions = habitCompletionRepository.getAllHabitCompletionByDate(testHabit, LocalDate.now());

        assertEquals(1, completions.size());
    }

    @Test
    void testGetAllHabitCompletionByDate_WithFilteredResults() {

        habitCompletionRepository.addHabitCompletion(testHabit, new HabitCompletion(LocalDate.now().minusDays(3), testHabit));
        habitCompletionRepository.addHabitCompletion(testHabit, new HabitCompletion(LocalDate.now().minusDays(1), testHabit));
        habitCompletionRepository.addHabitCompletion(testHabit, new HabitCompletion(LocalDate.now(), testHabit));
        List<HabitCompletion> completions = habitCompletionRepository.getAllHabitCompletionByDate(testHabit, LocalDate.now().minusDays(2));

        assertEquals(2, completions.size());
    }

    @Test
    void testGetAllHabitCompletionByDate_NoResults() {
        List<HabitCompletion> completions = habitCompletionRepository.getAllHabitCompletionByDate(testHabit, LocalDate.now().plusDays(1));

        assertTrue(completions.isEmpty());
    }

}

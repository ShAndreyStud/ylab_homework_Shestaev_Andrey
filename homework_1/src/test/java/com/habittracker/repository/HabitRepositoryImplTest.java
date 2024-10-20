package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class HabitRepositoryImplTest {

    private HabitRepositoryImpl habitRepository;
    private User testUser;

    @BeforeEach
    void setUp() {
        habitRepository = new HabitRepositoryImpl();
        testUser = new User("Test User", "test@example.com", "password", User.Role.USER);
    }

    @Test
    void testGetHabit_UserHasHabit() {
        Habit habit = new Habit("Test Habit", "Description", Habit.Frequency.DAILY, testUser);
        habitRepository.addHabit(testUser, habit);

        Habit retrievedHabit = habitRepository.getHabit(testUser, "Test Habit");

        assertNotNull(retrievedHabit);
        assertEquals("Test Habit", retrievedHabit.getName());
        assertEquals("Description", retrievedHabit.getDescription());
        assertEquals(Habit.Frequency.DAILY, retrievedHabit.getFrequency());
    }

    @Test
    void testGetHabit_UserDoesNotHaveHabit() {
        Habit retrievedHabit = habitRepository.getHabit(testUser, "Nonexistent Habit");

        assertNull(retrievedHabit);
    }

    @Test
    void testGetHabit_UserDoesNotExist() {
        User anotherUser = new User("Another User", "another@example.com", "password", User.Role.USER);
        Habit habit = new Habit("Another Habit", "Description", Habit.Frequency.WEEKLY, testUser);
        habitRepository.addHabit(anotherUser, habit);

        Habit retrievedHabit = habitRepository.getHabit(testUser, "Another Habit");

        assertNull(retrievedHabit);
    }

    @Test
    void testAddHabit_NewHabit() {
        Habit habit = new Habit("New Habit", "Description", Habit.Frequency.DAILY, testUser);

        boolean result = habitRepository.addHabit(testUser, habit);

        assertTrue(result);

        Habit retrievedHabit = habitRepository.getHabit(testUser, "New Habit");
        assertNotNull(retrievedHabit);
        assertEquals("New Habit", retrievedHabit.getName());
    }

    @Test
    void testAddHabit_HabitAlreadyExists() {
        Habit habit = new Habit("Existing Habit", "Description", Habit.Frequency.DAILY, testUser);
        habitRepository.addHabit(testUser, habit); // Добавляем привычку

        boolean result = habitRepository.addHabit(testUser, habit);

        assertFalse(result);
    }

    @Test
    void testAddHabit_NewUser() {
        User newUser = new User("New User", "newuser@example.com", "password", User.Role.USER);
        Habit habit = new Habit("User Habit", "Description", Habit.Frequency.WEEKLY, newUser);

        boolean result = habitRepository.addHabit(newUser, habit);

        assertTrue(result);

        Habit retrievedHabit = habitRepository.getHabit(newUser, "User Habit");
        assertNotNull(retrievedHabit);
        assertEquals("User Habit", retrievedHabit.getName());
    }

    @Test
    void testUpdateHabit_Success() {
        Habit habit = new Habit("Old Habit", "Description", Habit.Frequency.DAILY, testUser);
        habitRepository.addHabit(testUser, habit); // Добавляем привычку

        boolean result = habitRepository.updateHabit(testUser, habit, "Updated Habit", "New Description", Habit.Frequency.WEEKLY);

        assertTrue(result);

        Habit updatedHabit = habitRepository.getHabit(testUser, "Updated Habit");
        assertNotNull(updatedHabit);
        assertEquals("Updated Habit", updatedHabit.getName());
        assertEquals("New Description", updatedHabit.getDescription());
        assertEquals(Habit.Frequency.WEEKLY, updatedHabit.getFrequency());
    }

    @Test
    void testUpdateHabit_HabitDoesNotExist() {
        Habit habit = new Habit("Nonexistent Habit", "Description", Habit.Frequency.DAILY, testUser);

        boolean result = habitRepository.updateHabit(testUser, habit, "Updated Habit", "New Description", Habit.Frequency.WEEKLY);

        assertFalse(result);
    }

    @Test
    void testUpdateHabit_UserDoesNotExist() {
        User anotherUser = new User("Another User", "another@example.com", "password", User.Role.USER);
        Habit habit = new Habit("Habit for Another User", "Description", Habit.Frequency.WEEKLY, anotherUser);
        habitRepository.addHabit(anotherUser, habit); // Добавляем привычку

        boolean result = habitRepository.updateHabit(testUser, habit, "Updated Habit", "New Description", Habit.Frequency.DAILY);

        assertFalse(result);
    }

    @Test
    void testUpdateHabit_ChangeName() {
        Habit habit = new Habit("Habit to Change", "Description", Habit.Frequency.DAILY, testUser);
        habitRepository.addHabit(testUser, habit);

        boolean result = habitRepository.updateHabit(testUser, habit, "New Habit Name", "New Description", Habit.Frequency.WEEKLY);

        assertTrue(result);

        assertNull(habitRepository.getHabit(testUser, "Habit to Change"));
        Habit updatedHabit = habitRepository.getHabit(testUser, "New Habit Name");
        assertNotNull(updatedHabit);
    }

    @Test
    void testDeleteHabit_Success() {
        Habit habit = new Habit("Habit to Delete", "Description", Habit.Frequency.DAILY, testUser);
        habitRepository.addHabit(testUser, habit); // Adding the habit

        boolean result = habitRepository.deleteHabit(testUser, "Habit to Delete");

        assertTrue(result);

        assertNull(habitRepository.getHabit(testUser, "Habit to Delete"));
    }

    @Test
    void testDeleteHabit_HabitDoesNotExist() {
        boolean result = habitRepository.deleteHabit(testUser, "Nonexistent Habit");

        assertFalse(result);
    }

    @Test
    void testDeleteHabit_UserDoesNotExist() {
        User anotherUser = new User("Another User", "another@example.com", "password", User.Role.USER);
        Habit habit = new Habit("Habit for Another User", "Description", Habit.Frequency.WEEKLY, anotherUser);
        habitRepository.addHabit(anotherUser, habit);
        boolean result = habitRepository.deleteHabit(testUser, "Habit for Another User");

        assertFalse(result);
    }

    @Test
    void testDeleteHabit_UserHasNoHabits() {
        boolean result = habitRepository.deleteHabit(testUser, "Any Habit");

        assertFalse(result);
    }

    @Test
    void testGetAllHabits_UserHasHabits() {
        Habit habit1 = new Habit("Habit 1", "Description 1", Habit.Frequency.DAILY, testUser);
        Habit habit2 = new Habit("Habit 2", "Description 2", Habit.Frequency.WEEKLY, testUser);
        habitRepository.addHabit(testUser, habit1);
        habitRepository.addHabit(testUser, habit2);

        List<Habit> retrievedHabits = habitRepository.getAllHabits(testUser);

        assertEquals(2, retrievedHabits.size());
        assertTrue(retrievedHabits.contains(habit1));
        assertTrue(retrievedHabits.contains(habit2));
    }

    @Test
    void testGetAllHabits_UserHasNoHabits() {
        List<Habit> retrievedHabits = habitRepository.getAllHabits(testUser);

        assertTrue(retrievedHabits.isEmpty());
    }

    @Test
    void testGetAllHabits_UserDoesNotExist() {
        User anotherUser = new User("Another User", "another@example.com", "password", User.Role.USER);
        Habit habit = new Habit("Habit for Another User", "Description", Habit.Frequency.WEEKLY, anotherUser);
        habitRepository.addHabit(anotherUser, habit); // Adding habit for another user

        List<Habit> retrievedHabits = habitRepository.getAllHabits(testUser);

        assertTrue(retrievedHabits.isEmpty());
    }

}

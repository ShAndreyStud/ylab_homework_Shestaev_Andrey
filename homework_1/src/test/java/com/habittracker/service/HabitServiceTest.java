package com.habittracker.service;

import com.habittracker.model.Habit;
import com.habittracker.model.User;
import com.habittracker.model.HabitCompletion;
import com.habittracker.repository.HabitCompletionRepository;
import com.habittracker.repository.HabitRepository;
import com.habittracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

class HabitServiceTest {
    private HabitService habitService;
    private HabitRepository habitRepository;
    private UserRepository userRepository; // Инициализация UserRepository
    private HabitCompletionRepository habitCompletionRepository;
    private User testUser;

    @BeforeEach
    public void setUp() {
        habitRepository = mock(HabitRepository.class);
        userRepository = mock(UserRepository.class);
        habitCompletionRepository = mock(HabitCompletionRepository.class);
        habitService = new HabitService(habitCompletionRepository, habitRepository);

        testUser = new User("Test User", "test@example.com", "password", User.Role.USER);
    }

    @Test
    public void testCreateHabit_Success() {
        String habitName = "New Habit";
        String description = "This is a new habit.";
        Habit.Frequency frequency = Habit.Frequency.DAILY;

        when(habitRepository.getHabit(testUser, habitName)).thenReturn(null);
        when(habitRepository.addHabit(eq(testUser), any(Habit.class))).thenReturn(true);

        String result = habitService.createHabit(testUser, habitName, description, frequency);

        assertEquals("Habit successfully added.", result);

        ArgumentCaptor<Habit> habitCaptor = forClass(Habit.class);
        verify(habitRepository).addHabit(eq(testUser), habitCaptor.capture());

        Habit capturedHabit = habitCaptor.getValue();
        assertEquals(habitName, capturedHabit.getName());
        assertEquals(description, capturedHabit.getDescription());
        assertEquals(frequency, capturedHabit.getFrequency());
    }

    @Test
    public void testCreateHabit_AlreadyExists() {
        String habitName = "Existing Habit";
        String description = "This habit already exists.";
        Habit.Frequency frequency = Habit.Frequency.DAILY;

        when(habitRepository.getHabit(testUser, habitName)).thenReturn(new Habit(habitName, description, frequency, testUser));

        String result = habitService.createHabit(testUser, habitName, description, frequency);

        assertEquals("Error: you already have a habit by that name.", result);

        verify(habitRepository, never()).addHabit(any(), any());
    }

    @Test
    public void testCreateHabit_AdditionFails() {
        String habitName = "Failed Habit";
        String description = "This habit should fail to add.";
        Habit.Frequency frequency = Habit.Frequency.DAILY;

        when(habitRepository.getHabit(testUser, habitName)).thenReturn(null);
        when(habitRepository.addHabit(eq(testUser), any(Habit.class))).thenReturn(false);

        String result = habitService.createHabit(testUser, habitName, description, frequency);

        assertEquals("Error.", result);

        verify(habitRepository).addHabit(eq(testUser), any(Habit.class));
    }

    @Test
    public void testGetAllHabits_Success() {
        Habit habit1 = new Habit("Habit 1", "Description 1", Habit.Frequency.DAILY, testUser);
        Habit habit2 = new Habit("Habit 2", "Description 2", Habit.Frequency.WEEKLY, testUser);
        List<Habit> expectedHabits = Arrays.asList(habit1, habit2);

        when(habitRepository.getAllHabits(testUser)).thenReturn(expectedHabits);

        List<Habit> result = habitService.getAllHabits(testUser);

        assertEquals(expectedHabits, result);

        verify(habitRepository).getAllHabits(testUser);
    }

    @Test
    public void testGetAllHabits_NoHabits() {
        when(habitRepository.getAllHabits(testUser)).thenReturn(Collections.emptyList());

        List<Habit> result = habitService.getAllHabits(testUser);

        assertTrue(result.isEmpty());

        verify(habitRepository).getAllHabits(testUser);
    }

    @Test
    public void testUpdateHabit_Success() {
        Habit existingHabit = new Habit("Old Habit", "Old Description", Habit.Frequency.DAILY, testUser);
        String newName = "Updated Habit";
        String newDescription = "Updated Description";
        Habit.Frequency newFrequency = Habit.Frequency.WEEKLY;

        when(habitRepository.updateHabit(eq(testUser), eq(existingHabit), eq(newName), eq(newDescription), eq(newFrequency))).thenReturn(true);

        boolean result = habitService.updateHabit(testUser, existingHabit, newName, newDescription, newFrequency);

        assertTrue(result);

        verify(habitRepository).updateHabit(eq(testUser), eq(existingHabit), eq(newName), eq(newDescription), eq(newFrequency));
    }

    @Test
    public void testUpdateHabit_Failure() {
        Habit existingHabit = new Habit("Old Habit", "Old Description", Habit.Frequency.DAILY, testUser);
        String newName = "Updated Habit";
        String newDescription = "Updated Description";
        Habit.Frequency newFrequency = Habit.Frequency.WEEKLY;

        when(habitRepository.updateHabit(eq(testUser), eq(existingHabit), eq(newName), eq(newDescription), eq(newFrequency))).thenReturn(false);

        boolean result = habitService.updateHabit(testUser, existingHabit, newName, newDescription, newFrequency);

        assertFalse(result);

        verify(habitRepository).updateHabit(eq(testUser), eq(existingHabit), eq(newName), eq(newDescription), eq(newFrequency));
    }

    @Test
    public void testDeleteHabit_Success() {
        String habitName = "Habit to Delete";

        when(habitRepository.deleteHabit(eq(testUser), eq(habitName))).thenReturn(true);

        boolean result = habitService.deleteHabit(testUser, habitName);

        assertTrue(result);

        verify(habitRepository).deleteHabit(eq(testUser), eq(habitName));
    }

    @Test
    public void testDeleteHabit_Failure() {
        String habitName = "Nonexistent Habit";

        when(habitRepository.deleteHabit(eq(testUser), eq(habitName))).thenReturn(false);

        boolean result = habitService.deleteHabit(testUser, habitName);

        assertFalse(result);

        verify(habitRepository).deleteHabit(eq(testUser), eq(habitName));
    }

    @Test
    public void testGetHabitsByDate_Success() {
        Habit habit1 = new Habit("Habit 1", "Description 1", Habit.Frequency.DAILY, testUser);
        habit1.setCreateDate(LocalDate.of(2023, 10, 18)); // старая дата

        Habit habit2 = new Habit("Habit 2", "Description 2", Habit.Frequency.DAILY, testUser);
        habit2.setCreateDate(LocalDate.of(2024, 10, 19)); // новая дата

        Habit habit3 = new Habit("Habit 3", "Description 3", Habit.Frequency.DAILY, testUser);
        habit3.setCreateDate(LocalDate.of(2024, 1, 1)); // дата между

        List<Habit> habitList = Arrays.asList(habit1, habit2, habit3);

        List<Habit> sortedHabits = habitService.getHabitsByDate(habitList);

        assertEquals(habit1, sortedHabits.get(0)); // habit1 должен быть первым
        assertEquals(habit3, sortedHabits.get(1)); // habit3 должен быть вторым
        assertEquals(habit2, sortedHabits.get(2)); // habit2 должен быть последним
    }

    @Test
    public void testGetHabitsByDate_EmptyList() {
        List<Habit> sortedHabits = habitService.getHabitsByDate(Collections.emptyList());

        assertTrue(sortedHabits.isEmpty());
    }

    @Test
    public void testGetAvailableHabits_DailyHabit_NotCompletedToday() {
        Habit dailyHabit = new Habit("Daily Habit", "Daily description", Habit.Frequency.DAILY, testUser);
        dailyHabit.setCreateDate(LocalDate.now().minusDays(1)); // Создана вчера

        List<HabitCompletion> completions = new ArrayList<>();
        when(habitCompletionRepository.getAllHabitCompletion(dailyHabit)).thenReturn(completions);

        List<Habit> availableHabits = habitService.getAvailableHabits(Collections.singletonList(dailyHabit));

        assertTrue(availableHabits.contains(dailyHabit));
    }

    @Test
    public void testGetAvailableHabits_DailyHabit_CompletedToday() {
        Habit dailyHabit = new Habit("Daily Habit", "Daily description", Habit.Frequency.DAILY, testUser);
        dailyHabit.setCreateDate(LocalDate.now().minusDays(1)); // Создана вчера

        List<HabitCompletion> completions = new ArrayList<>();
        HabitCompletion completion = new HabitCompletion(LocalDate.now(), dailyHabit);
        completions.add(completion);
        when(habitCompletionRepository.getAllHabitCompletion(dailyHabit)).thenReturn(completions);

        List<Habit> availableHabits = habitService.getAvailableHabits(Collections.singletonList(dailyHabit));

        assertFalse(availableHabits.contains(dailyHabit));
    }

    @Test
    public void testGetAvailableHabits_WeeklyHabit_NoCompletions() {
        Habit weeklyHabit = new Habit("Weekly Habit", "Weekly description", Habit.Frequency.WEEKLY, testUser);
        weeklyHabit.setCreateDate(LocalDate.now().minusWeeks(2)); // Создана 2 недели назад

        when(habitCompletionRepository.getAllHabitCompletion(weeklyHabit)).thenReturn(Collections.emptyList());

        List<Habit> availableHabits = habitService.getAvailableHabits(Collections.singletonList(weeklyHabit));

        assertTrue(availableHabits.contains(weeklyHabit));
    }

    @Test
    public void testGetAvailableHabits_WeeklyHabit_CompletedLastWeek() {
        Habit weeklyHabit = new Habit("Weekly Habit", "Weekly description", Habit.Frequency.WEEKLY, testUser);
        weeklyHabit.setCreateDate(LocalDate.now().minusWeeks(2)); // Создана 2 недели назад

        List<HabitCompletion> completions = new ArrayList<>();
        HabitCompletion completion = new HabitCompletion(LocalDate.now(), weeklyHabit);
        completions.add(completion);
        when(habitCompletionRepository.getAllHabitCompletion(weeklyHabit)).thenReturn(completions);

        List<Habit> availableHabits = habitService.getAvailableHabits(Collections.singletonList(weeklyHabit));

        assertFalse(availableHabits.contains(weeklyHabit));
    }

    @Test
    public void testCountHabitStreak_DailyHabit_CompleteToday() {
        Habit dailyHabit = new Habit("Daily Habit", "Daily description", Habit.Frequency.DAILY, testUser);
        dailyHabit.setCreateDate(LocalDate.now().minusDays(4)); // Создана 4 дня назад

        List<HabitCompletion> completions = Arrays.asList(
                new HabitCompletion(LocalDate.now(), dailyHabit), // Завершение сегодня
                new HabitCompletion(LocalDate.now().minusDays(1), dailyHabit), // Завершение вчера
                new HabitCompletion(LocalDate.now().minusDays(2), dailyHabit) // Завершение позавчера
        );

        when(habitCompletionRepository.getAllHabitCompletion(dailyHabit)).thenReturn(completions);

        int streak = habitService.countHabitStreak(dailyHabit);

        assertEquals(3, streak);
    }


    @Test
    public void testCountHabitStreak_WeeklyHabit_CompleteThisWeek() {
        Habit weeklyHabit = new Habit("Weekly Habit", "Weekly description", Habit.Frequency.WEEKLY, testUser);
        weeklyHabit.setCreateDate(LocalDate.now().minusWeeks(3)); // Создана 3 недели назад

        List<HabitCompletion> completions = Arrays.asList(
                new HabitCompletion(LocalDate.now().minusWeeks(1), weeklyHabit), // Завершение на прошлой неделе
                new HabitCompletion(LocalDate.now(), weeklyHabit) // Завершение на этой неделе
        );

        when(habitCompletionRepository.getAllHabitCompletion(weeklyHabit)).thenReturn(completions);

        int streak = habitService.countHabitStreak(weeklyHabit);

        assertEquals(2, streak);
    }


    @Test
    public void testCountHabitStreak_NoHabitCompletions() {
        Habit dailyHabit = new Habit("Daily Habit", "Daily description", Habit.Frequency.DAILY, testUser);
        dailyHabit.setCreateDate(LocalDate.now().minusDays(4)); // Создана 4 дня назад

        when(habitCompletionRepository.getAllHabitCompletion(dailyHabit)).thenReturn(Collections.emptyList());

        int streak = habitService.countHabitStreak(dailyHabit);

        assertEquals(0, streak);
    }

    @Test
    public void testCountPercentage_DailyHabit_Success() {
        LocalDate startDate = LocalDate.now().minusDays(9);
        Habit dailyHabit = new Habit("Daily Habit", "Description", Habit.Frequency.DAILY, testUser, startDate);
        List<HabitCompletion> completions = Arrays.asList(
                new HabitCompletion(LocalDate.now().minusDays(2), dailyHabit),
                new HabitCompletion(LocalDate.now().minusDays(5), dailyHabit)
        );

        when(habitCompletionRepository.getAllHabitCompletionByDate(dailyHabit, startDate)).thenReturn(completions);

        int result = habitService.countPercentage(startDate, dailyHabit);

        assertEquals(20, result);
    }

    @Test
    public void testCountPercentage_WeeklyHabit_Success() {
        LocalDate startDate = LocalDate.now().minusWeeks(4);
        Habit weeklyHabit = new Habit("Weekly Habit", "Description", Habit.Frequency.WEEKLY, testUser, startDate);
        List<HabitCompletion> completions = Arrays.asList(
                new HabitCompletion(LocalDate.now().minusWeeks(1), weeklyHabit),
                new HabitCompletion(LocalDate.now().minusWeeks(3), weeklyHabit)
        );

        when(habitCompletionRepository.getAllHabitCompletionByDate(weeklyHabit, startDate)).thenReturn(completions);

        int result = habitService.countPercentage(startDate, weeklyHabit);

        assertEquals(40, result);
    }

    @Test
    public void testCountPercentage_NoCompletions() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        Habit dailyHabit = new Habit("Daily Habit", "Description", Habit.Frequency.DAILY, testUser);

        when(habitCompletionRepository.getAllHabitCompletionByDate(dailyHabit, startDate)).thenReturn(Arrays.asList());

        int result = habitService.countPercentage(startDate, dailyHabit);

        assertEquals(0, result);
    }

    @Test
    public void testCountPercentage_CreateDateAfterStartDate() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        Habit dailyHabit = new Habit("Daily Habit", "Description", Habit.Frequency.DAILY, testUser);

        int result = habitService.countPercentage(startDate, dailyHabit);

        assertEquals(0, result);
    }

    @Test
    public void testCountPercentage_NullCompletions() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        Habit dailyHabit = new Habit("Daily Habit", "Description", Habit.Frequency.DAILY, testUser);

        when(habitCompletionRepository.getAllHabitCompletionByDate(dailyHabit, startDate)).thenReturn(null);

        int result = habitService.countPercentage(startDate, dailyHabit);

        assertEquals(0, result);
    }
}

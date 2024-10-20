package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.User;

import java.util.List;


public interface HabitRepository {
    Habit getHabit(User user, String habitName);
    boolean addHabit(User user, Habit habit);
    boolean updateHabit(User user, Habit habit, String newName, String newDescription, Habit.Frequency newFrequency);
    boolean deleteHabit(User user, String habit);
    List<Habit> getAllHabits(User user);
}

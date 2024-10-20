package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;


import java.time.LocalDate;
import java.util.List;

public interface HabitCompletionRepository {
    HabitCompletion getHabitCompletion(Habit habit, Integer serialNumber);
    boolean addHabitCompletion(Habit habit, HabitCompletion newCompletion);
    boolean updateHabitCompletion(Habit habit, int serialNumber, HabitCompletion updatedCompletion);
    boolean deleteHabitCompletion(Habit habit, int serialNumber);
    public boolean deleteAllHabitCompletion(Habit habit);
    public List<HabitCompletion> getAllHabitCompletion(Habit habit);
    public List<HabitCompletion> getAllHabitCompletionByDate(Habit habit, LocalDate date);
}

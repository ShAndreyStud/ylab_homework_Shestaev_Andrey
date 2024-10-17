package com.habittracker.service;
/*
import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;
import com.habittracker.model.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HabitService {


    // Создание привычки для конкретного пользователя
    public String createHabit(User user, String name, String description, String frequency) {
        List<Habit> habits = user.getHabits();
        for (Habit habit : habits) {
            if (habit.getName().equals(name)) {
                return "Error: A habit with this name already exists.";
            }
        }
        Habit habit = new Habit(name, description, frequency);
        habits.add(habit);
        return "The habit has been successfully created.";
    }

    // Редактирование привычки пользователя
    public String updateHabit(User user, String name, String newName, String newDescription, String newFrequency) {
        List<Habit> habits = user.getHabits();
        for (Habit habit : habits) {
            if (habit.getName().equals(name)) {
                habit.setName(newName);
                habit.setDescription(newDescription);
                habit.setFrequency(newFrequency);
                return "The habit has been successfully renewed.";
            }
        }
        return "Habit is not found.";
    }

    // Удаление привычки пользователя и её статистики
    public String deleteHabit(User user, String name) {
        List<Habit> habits = user.getHabits();
        Habit habitToRemove = null;
        for (Habit habit : habits) {
            if (habit.getName().equals(name)) {
                habitToRemove = habit;
                break;
            }
        }
        if (habitToRemove != null) {
            habits.remove(habitToRemove);
            return "The habit has been successfully removed along with its completion history.";
        }
        return "Habit is not found.";
    }

    // Просмотр всех привычек пользователя
    public List<Habit> getAllHabits(User user) {
        return user.getHabits();
    }

    // Поиск привычки по имени для конкретного пользователя
    public Habit findHabitByName(User user, String name) {
        List<Habit> habits = user.getHabits();
        for (Habit habit : habits) {
            if (habit.getName().equalsIgnoreCase(name)) {
                return habit;
            }
        }
        return null;
    }

    // Получение доступных привычек для отметки
    public List<Habit> getAvailableHabits(User user) {
        List<Habit> availableHabits = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Habit habit : user.getHabits()) {
            if (habit.getCompletions().isEmpty()) {
                availableHabits.add(habit);
            } else {
                HabitCompletion lastCompletion = habit.getCompletions().get(habit.getCompletions().size()-1);
                LocalDate nextMark = lastCompletion.getNextMarkDate();
                if (nextMark != null && (nextMark.isBefore(today) || nextMark.isEqual(today))) {
                    availableHabits.add(habit);
                }
            }
        }
        return availableHabits;
    }

    public int countStreak(Habit habit) {
        int streak = 0;
        List<HabitCompletion> reverseCompletions = new ArrayList<>(habit.getCompletions());
        Collections.reverse(reverseCompletions);
        LocalDate tempDate = LocalDate.now();
        if(habit.getFrequency().equals("daily")){
            for (int i = 0; i < reverseCompletions.size(); i++) {
                if(reverseCompletions.get(i).getMarkDate().isEqual(tempDate)){
                    streak++;
                    tempDate = tempDate.minusDays(1);
                } else{
                    break;
                }
            }
        } else {
            for (int i = 0; i < reverseCompletions.size(); i++) {
                LocalDate nextMark = reverseCompletions.get(i).getNextMarkDate();
                if(tempDate.isBefore(nextMark) && (tempDate.isAfter(nextMark.minusWeeks(i+1))) || tempDate.isEqual(nextMark.minusWeeks(i+1))){
                    streak++;
                    tempDate = tempDate.minusWeeks(i+1);
                }

            }

        }
        return streak;
    }

    public int countPercentage(LocalDate startDate, Habit habit) {
        double percentage = 0;
        List<HabitCompletion> completions = habit.getCompletionsByStartDate(startDate);
        LocalDate tempDate = LocalDate.now();
        if(habit.getFrequency().equals("daily")){
            int max;
            if(startDate.isEqual(LocalDate.now())){
                max = 1;
            } else {
                if(habit.getCreateDate().isBefore(startDate)){
                    max = (int) ChronoUnit.DAYS.between(startDate, LocalDate.now());
                } else {
                    max = (int) ChronoUnit.DAYS.between(habit.getCreateDate(), LocalDate.now()) + 1;
                }
            }
            int complete = completions.size();
            System.out.println("max = " + max + " complete = " + complete);
            percentage = (double) complete / max;
        } else {
            int max = 0;
            tempDate = habit.getCreateDate().plusWeeks(1);
            while(true){
                if(startDate.isBefore(tempDate)){
                    max++;
                }
                if(LocalDate.now().isBefore(tempDate)){
                    break;
                }
                tempDate = tempDate.plusWeeks(1);
            }
            percentage = (double) completions.size() / max;
        }

        return (int) Math.round(percentage * 100);
    }
}*/
package com.habittracker.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Класс, представляющий завершение привычки пользователем.
 * Содержит информацию о дате отметки завершения, серийном номере завершения и
 * ссылке на связанную привычку.
 */
public class HabitCompletion {
    private LocalDate markDate;
    private int serialNumber;

    private Habit habit;

    /**
     * Конструктор класса HabitCompletion.
     *
     * @param markDate Дата завершения привычки.
     * @param habit Связанная привычка.
     */
    public HabitCompletion(LocalDate markDate, Habit habit) {
        this.markDate = markDate;
        this.habit = habit;
        if(habit.getFrequency() == Habit.Frequency.DAILY){
            this.serialNumber = (int) ChronoUnit.DAYS.between(habit.getCreateDate(), markDate) + 1;
        } else {
            this.serialNumber = (int) ChronoUnit.WEEKS.between(habit.getCreateDate(), markDate) + 1;
        }
    }


    public LocalDate getMarkDate() {
        return markDate;
    }

    public void setMarkDate(LocalDate markDate) {
        this.markDate = markDate;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }
}

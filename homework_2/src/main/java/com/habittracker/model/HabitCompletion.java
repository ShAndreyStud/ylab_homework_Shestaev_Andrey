package com.habittracker.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Класс, представляющий завершение привычки пользователем.
 * Содержит информацию о дате отметки завершения, серийном номере завершения и
 * ссылке на связанную привычку.
 */
public class HabitCompletion {
    /** Дата завершения привычки */
    private LocalDate markDate;

    /** Последовательный номер выполнения привычки */
    private int serialNumber;

    /** Связанная привычка */
    private Habit habit;

    /**
     * Конструктор по умолчанию класса HabitCompletion.
     */
    public HabitCompletion(){

    }

    /**
     * Конструктор класса HabitCompletion для добавления выполнения привычки пользователем.
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

    /**
     * Конструктор класса HabitCompletion с указанием даты завершения, привычки и последовательного номера для ручного создания выполнения.
     *
     * @param markDate Дата завершения привычки.
     * @param habit Связанная привычка.
     * @param serialNumber Последовательный номер завершения привычки.
     */
    public HabitCompletion(LocalDate markDate, Habit habit, int serialNumber) {
        this.markDate = markDate;
        this.habit = habit;
        this.serialNumber = serialNumber;
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

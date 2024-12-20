package com.habittracker.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий привычку пользователя.
 * Содержит информацию о названии, описании, частоте, дате создания,
 * пользователе и завершениях привычки.
 */
public class Habit {
    /** Уникальный идентификатор привычки */
    private int id;

    /** Название привычки */
    private String name;

    /** Описание привычки */
    private String description;

    /** Частота выполнения привычки (ежедневная или еженедельная) */
    private Frequency frequency;

    /** Дата создания привычки */
    private LocalDate createDate;

    /** Пользователь, которому принадлежит привычка */
    private User user;

    /** Список выполнений данной привычки */
    private List<HabitCompletion> completions;

    /**
     * Перечисление для частоты выполнения привычки.
     */
    public enum Frequency {
        /** Ежедневная привычка */
        DAILY,
        /** Еженедельная привычка */
        WEEKLY
    }

    /**
     * Конструктор класса Habit.
     * Инициализирует новый объект привычки с заданными значениями полей.
     */
    public Habit(){

    }

    /**
     * Конструктор класса Habit.
     *
     * @param name Название привычки.
     * @param description Описание привычки.
     * @param frequency Частота выполнения привычки.
     * @param user Пользователь, которому принадлежит привычка.
     */
    public Habit(String name, String description, Frequency frequency, User user) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.createDate = LocalDate.now();
        this.user = user;
        this.completions = new ArrayList<>();
    }

    public Habit(String name, String description, Frequency frequency, User user, int id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.createDate = LocalDate.now();
        this.user = user;
        this.completions = new ArrayList<>();
    }

    /**
     * Перегруженный конструктор класса Habit.
     *
     * @param name Название привычки.
     * @param description Описание привычки.
     * @param frequency Частота выполнения привычки.
     * @param user Пользователь, которому принадлежит привычка.
     * @param dateCreate Дата создания привычки.
     */
    public Habit(String name, String description, Frequency frequency, User user, LocalDate dateCreate) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.createDate = dateCreate;
        this.user = user;
        this.completions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public List<HabitCompletion> getCompletions() {
        return completions;
    }

    public void setCompletions(List<HabitCompletion> completions) {
        this.completions = completions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}




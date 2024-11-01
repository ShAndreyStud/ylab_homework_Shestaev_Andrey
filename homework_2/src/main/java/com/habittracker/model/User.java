package com.habittracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий пользователя.
 * Содержит информацию о пользователе, включая его имя, адрес электронной почты,
 * пароль, роль (пользователь или администратор), статус блокировки и список привычек.
 */
public class User {
    /** Имя пользователя */
    private String name;

    /** Электронная почта пользователя */
    private String email;

    /** Пароль пользователя */
    private String password;

    /** Роль пользователя (пользователь или администратор) */
    private Role role;

    /** Статус блокировки пользователя */
    private boolean isBlocked;

    /** Список привычек пользователя */
    private List<Habit> habits;

    /**
     * Перечисление ролей пользователя.
     */
    public enum Role {
        USER,
        ADMIN
    }

    /**
     * Конструктор по умолчанию класса User.
     */
    public User(){

    }

    /**
     * Конструктор класса User.
     *
     * @param name Имя пользователя.
     * @param email Адрес электронной почты.
     * @param password Пароль.
     * @param role Роль пользователя (USER или ADMIN).
     */
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.habits = new ArrayList<>();
        this.isBlocked = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
    }
}

package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.User;

import java.util.List;


/**
 * Интерфейс для репозитория привычек.
 * Определяет методы для управления привычками пользователя.
 */
public interface HabitRepository {

    /**
     * Получает привычку по заданному пользователю и имени привычки.
     *
     * @param user       Пользователь, которому принадлежит привычка.
     * @param habitName  Имя привычки, которую необходимо получить.
     * @return Привычка, если найдена; иначе null.
     */
    Habit getHabit(User user, String habitName);

    /**
     * Добавляет новую привычку для заданного пользователя.
     *
     * @param user   Пользователь, которому необходимо добавить привычку.
     * @param habit  Привычка, которую нужно добавить.
     * @return true, если привычка была успешно добавлена; иначе false.
     */
    boolean addHabit(User user, Habit habit);

    /**
     * Обновляет существующую привычку пользователя.
     *
     * @param user            Пользователь, которому принадлежит привычка.
     * @param habit           Привычка, которую нужно обновить.
     * @param newName         Новое имя привычки.
     * @param newDescription  Новое описание привычки.
     * @param newFrequency    Новая частота выполнения привычки.
     * @return true, если привычка была успешно обновлена; иначе false.
     */
    boolean updateHabit(User user, Habit habit, String newName, String newDescription, Habit.Frequency newFrequency);

    /**
     * Удаляет привычку пользователя по имени привычки.
     *
     * @param user   Пользователь, которому принадлежит привычка.
     * @param habit  Имя привычки, которую необходимо удалить.
     * @return true, если привычка была успешно удалена; иначе false.
     */
    boolean deleteHabit(User user, String habit);

    /**
     * Получает все привычки для заданного пользователя.
     *
     * @param user Пользователь, для которого необходимо получить все привычки.
     * @return Список привычек пользователя.
     */
    List<Habit> getAllHabits(User user);
}

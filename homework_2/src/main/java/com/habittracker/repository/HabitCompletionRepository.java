package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;


import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс для репозитория завершений привычек.
 * Определяет методы для управления завершениями привычек.
 */
public interface HabitCompletionRepository {

    /**
     * Получает завершение привычки по заданной привычке и ее порядковому номеру.
     *
     * @param habit        Привычка, для которой необходимо получить завершение.
     * @param serialNumber Порядковый номер завершения с момента регистрации привычки.
     * @return Завершение привычки, если найдено; иначе null.
     */
    HabitCompletion getHabitCompletion(Habit habit, Integer serialNumber);

    /**
     * Добавляет новое завершение привычки.
     *
     * @param habit          Привычка, к которой относится завершение.
     * @param newCompletion  Новое завершение привычки для добавления.
     * @return true, если завершение было успешно добавлено; иначе false.
     */
    boolean addHabitCompletion(Habit habit, HabitCompletion newCompletion);

    /**
     * Обновляет существующее завершение привычки.
     *
     * @param habit              Привычка, для которой необходимо обновить завершение.
     * @param serialNumber       Порядковый номер завершения для обновления.
     * @param updatedCompletion   Обновленное завершение привычки.
     * @return true, если завершение было успешно обновлено; иначе false.
     */
    boolean updateHabitCompletion(Habit habit, int serialNumber, HabitCompletion updatedCompletion);

    /**
     * Удаляет завершение привычки по заданной привычке и порядковому номеру.
     *
     * @param habit        Привычка, из которой необходимо удалить завершение.
     * @param serialNumber Порядковый номер завершения для удаления.
     * @return true, если завершение было успешно удалено; иначе false.
     */
    boolean deleteHabitCompletion(Habit habit, int serialNumber);

    /**
     * Удаляет все завершения для заданной привычки.
     *
     * @param habit Привычка, для которой необходимо удалить все завершения.
     * @return true, если все завершения были успешно удалены; иначе false.
     */
    boolean deleteAllHabitCompletion(Habit habit);

    /**
     * Получает все завершения привычки для заданной привычки.
     *
     * @param habit Привычка, для которой необходимо получить все завершения.
     * @return Список завершений привычки.
     */
    List<HabitCompletion> getAllHabitCompletion(Habit habit);

    /**
     * Получает все завершения привычки по заданной дате.
     *
     * @param habit Привычка, для которой необходимо получить завершения.
     * @param date  Дата, по которой нужно получить завершения.
     * @return Список завершений привычки для заданной даты.
     */
    List<HabitCompletion> getAllHabitCompletionByDate(Habit habit, LocalDate date);
}

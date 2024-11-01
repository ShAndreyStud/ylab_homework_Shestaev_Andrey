package com.habittracker.repository;

/**
 * Класс, содержащий SQL-запросы для операций с завершениями привычек.
 * Этот класс служит хранилищем строковых констант, представляющих
 * запросы, используемые для взаимодействия с таблицей
 * habit_completion в базе данных.
 */
public class HabitCompletionQueries {

    /**
     * Запрос для выборки завершения привычки по идентификатору привычки
     * и серийному номеру завершения.
     */
    public static final String SELECT_HABIT_COMPLETION =
            "SELECT * FROM app_schema.habit_completion WHERE habit_id = ? AND serial_number = ?";

    /**
     * Запрос для добавления нового завершения привычки в таблицу.
     */
    public static final String INSERT_HABIT_COMPLETION =
            "INSERT INTO app_schema.habit_completion (habit_id, serial_number, mark_date) VALUES (?, ?, ?)";

    /**
     * Запрос для обновления даты завершения привычки по идентификатору
     * привычки и серийному номеру завершения.
     */
    public static final String UPDATE_HABIT_COMPLETION =
            "UPDATE app_schema.habit_completion SET mark_date = ? WHERE habit_id = ? AND serial_number = ?";

    /**
     * Запрос для удаления завершения привычки по идентификатору привычки
     * и серийному номеру завершения.
     */
    public static final String DELETE_HABIT_COMPLETION =
            "DELETE FROM app_schema.habit_completion WHERE habit_id = ? AND serial_number = ?";

    /**
     * Запрос для удаления всех завершений привычки по идентификатору привычки.
     */
    public static final String DELETE_ALL_HABIT_COMPLETION =
            "DELETE FROM app_schema.habit_completion WHERE habit_id = ?";

    /**
     * Запрос для выборки всех завершений привычки по идентификатору привычки.
     */
    public static final String SELECT_ALL_HABIT_COMPLETIONS =
            "SELECT * FROM app_schema.habit_completion WHERE habit_id = ?";

    /**
     * Запрос для выборки завершений привычки по идентификатору привычки
     * и дате завершения, где дата завершения больше или равна указанной.
     */
    public static final String SELECT_ALL_HABIT_COMPLETIONS_BY_DATE =
            "SELECT * FROM app_schema.habit_completion WHERE habit_id = ? AND mark_date >= ?";
}

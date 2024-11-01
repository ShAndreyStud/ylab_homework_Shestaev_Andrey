package com.habittracker.repository;

/**
 * Класс, содержащий SQL-запросы для операций с привычками.
 * Этот класс служит хранилищем строковых констант, представляющих
 * запросы, используемые для взаимодействия с таблицей
 * habit в базе данных.
 */
public class HabitQueries {

    /**
     * Запрос для выборки привычки по названию и идентификатору пользователя.
     */
    public static final String SELECT_HABIT =
            "SELECT * FROM app_schema.habit WHERE name = ? AND user_id = (SELECT id FROM app_schema.user WHERE email = ?)";

    /**
     * Запрос для добавления новой привычки в таблицу.
     */
    public static final String INSERT_HABIT =
            "INSERT INTO app_schema.habit (name, description, frequency, create_date, user_id) VALUES (?, ?, ?, ?, (SELECT id FROM app_schema.user WHERE email = ?))";

    /**
     * Запрос для обновления существующей привычки по её названию и
     * идентификатору пользователя.
     */
    public static final String UPDATE_HABIT =
            "UPDATE app_schema.habit SET name = ?, description = ?, frequency = ? WHERE name = ? AND user_id = (SELECT id FROM app_schema.user WHERE email = ?)";

    /**
     * Запрос для удаления привычки по названию и идентификатору пользователя.
     */
    public static final String DELETE_HABIT =
            "DELETE FROM app_schema.habit WHERE name = ? AND user_id = (SELECT id FROM app_schema.user WHERE email = ?)";

    /**
     * Запрос для выборки всех привычек пользователя по его email.
     */
    public static final String SELECT_ALL_HABITS =
            "SELECT * FROM app_schema.habit WHERE user_id = (SELECT id FROM app_schema.user WHERE email = ?)";
}
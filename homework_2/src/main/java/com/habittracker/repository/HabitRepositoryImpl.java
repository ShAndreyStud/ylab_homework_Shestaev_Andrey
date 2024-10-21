package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.User;

import java.sql.*;
import java.sql.Date;
import java.util.*;


public class HabitRepositoryImpl implements HabitRepository{
    private final Connection connection;

    public HabitRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Возвращает привычку пользователя по её названию.
     *
     * @param user пользователь, для которого ищется привычка
     * @param habitName название привычки
     * @return объект {@link Habit}, если привычка найдена, или {@code null}, если привычка отсутствует
     */
    @Override
    public Habit getHabit(User user, String habitName) {
        String sql = "SELECT * FROM app_schema.habit WHERE name = ? AND user_id = (SELECT id FROM app_schema.user WHERE email = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, habitName);
            stmt.setString(2, user.getEmail());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return mapRowToHabit(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Добавляет новую привычку для пользователя.
     * Если привычка с таким названием уже существует, метод возвращает {@code false}.
     *
     * @param user пользователь, для которого добавляется привычка
     * @param newHabit новая привычка {@link Habit}, которую нужно добавить
     * @return {@code true}, если привычка была успешно добавлена, или {@code false}, если привычка с таким названием уже существует
     */
    @Override
    public boolean addHabit(User user, Habit newHabit) {
        String sql = "INSERT INTO app_schema.habit (name, description, frequency, create_date, user_id) VALUES (?, ?, ?, ?, (SELECT id FROM app_schema.user WHERE email = ?))";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newHabit.getName());
            stmt.setString(2, newHabit.getDescription());
            stmt.setString(3, newHabit.getFrequency().toString());
            stmt.setDate(4, Date.valueOf(newHabit.getCreateDate()));
            stmt.setString(5, user.getEmail());

            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Обновляет существующую привычку для пользователя.
     * Метод изменяет название, описание и частоту привычки, создавая обновленный объект.
     *
     * @param user пользователь, у которого обновляется привычка
     * @param habit старая привычка {@link Habit}, которую нужно обновить
     * @param newName новое название привычки
     * @param newDescription новое описание привычки
     * @param newFrequency новая частота привычки {@link Habit.Frequency}
     * @return {@code true}, если обновление прошло успешно, или {@code false}, если привычка не найдена
     */
    @Override
    public boolean updateHabit(User user, Habit habit, String newName, String newDescription, Habit.Frequency newFrequency){
        String sql = "UPDATE app_schema.habit SET name = ?, description = ?, frequency = ? WHERE name = ? AND user_id = (SELECT id FROM app_schema.user WHERE email = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setString(3, newFrequency.toString());
            stmt.setString(4, habit.getName());
            stmt.setString(5, user.getEmail());

            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Удаляет привычку пользователя по её названию.
     *
     * @param user пользователь, для которого удаляется привычка
     * @param habitName название привычки, которую нужно удалить
     * @return {@code true}, если привычка была успешно удалена, или {@code false}, если привычка не найдена
     */
    @Override
    public boolean deleteHabit(User user, String habitName) {
        String sql = "DELETE FROM app_schema.habit WHERE name = ? AND user_id = (SELECT id FROM app_schema.user WHERE email = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, habitName);
            stmt.setString(2, user.getEmail());

            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Возвращает список всех привычек пользователя.
     *
     * @param user пользователь, для которого нужно получить список привычек
     * @return список объектов {@link Habit}, или пустой список, если у пользователя нет привычек
     */
    @Override
    public List<Habit> getAllHabits(User user) {
        List<Habit> habitsList = new ArrayList<>();
        String sql = "SELECT * FROM app_schema.habit WHERE user_id = (SELECT id FROM app_schema.user WHERE email = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                habitsList.add(mapRowToHabit(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return habitsList;
    }

    private Habit mapRowToHabit(ResultSet resultSet) throws SQLException {
        Habit habit = new Habit();
        habit.setName(resultSet.getString("name"));
        habit.setDescription(resultSet.getString("description"));
        habit.setFrequency(Habit.Frequency.valueOf(resultSet.getString("frequency")));
        habit.setCreateDate(resultSet.getDate("create_date").toLocalDate());
        habit.setId(resultSet.getInt("id"));

        return habit;
    }

}

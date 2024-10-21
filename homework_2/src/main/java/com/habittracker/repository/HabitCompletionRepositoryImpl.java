package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class HabitCompletionRepositoryImpl implements HabitCompletionRepository{
    private final Connection connection;

    public HabitCompletionRepositoryImpl(Connection connection) {
        this.connection = connection;
    }
    /**
     * Получает выполнение привычки по заданному серийному номеру для указанной привычки.
     *
     * @param habit привычка {@link Habit}, для которой нужно получить выполнение
     * @param serialNumber серийный номер выполнения привычки
     * @return выполнение привычки {@link HabitCompletion}, если найдено, или {@code null}, если отсутствует
     */
    @Override
    public HabitCompletion getHabitCompletion(Habit habit, Integer serialNumber) {
        String query = "SELECT * FROM app_schema.habit_completion WHERE habit_id = ? AND serial_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, habit.getId());
            statement.setInt(2, serialNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToHabitCompletion(resultSet, habit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Добавляет новое выполнение привычки для указанной привычки.
     * Если серийный номер уже существует, метод возвращает {@code false}.
     *
     * @param habit привычка {@link Habit}, для которой добавляется выполнение
     * @param newCompletion новое выполнение {@link HabitCompletion}
     * @return {@code true}, если выполнение было успешно добавлено, или {@code false}, если серийный номер уже существует
     */
    @Override
    public boolean addHabitCompletion(Habit habit, HabitCompletion newCompletion) {
        String query = "INSERT INTO app_schema.habit_completion (habit_id, serial_number, mark_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, habit.getId());
            statement.setInt(2, newCompletion.getSerialNumber());
            statement.setDate(3, Date.valueOf(newCompletion.getMarkDate()));
            int rowsAffected = statement.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Обновляет существующее выполнение привычки по заданному серийному номеру.
     *
     * @param habit привычка {@link Habit}, для которой нужно обновить выполнение
     * @param serialNumber серийный номер выполнения
     * @param updatedCompletion обновленное выполнение {@link HabitCompletion}
     * @return {@code true}, если обновление прошло успешно, или {@code false}, если выполнение не было найдено
     */
    @Override
    public boolean updateHabitCompletion(Habit habit, int serialNumber, HabitCompletion updatedCompletion) {
        String query = "UPDATE app_schema.habit_completion SET mark_date = ? WHERE habit_id = ? AND serial_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(updatedCompletion.getMarkDate()));
            statement.setInt(2, habit.getId());
            statement.setInt(3, serialNumber);
            int rowsAffected = statement.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Удаляет выполнение привычки по серийному номеру.
     *
     * @param habit привычка {@link Habit}, из которой нужно удалить выполнение
     * @param serialNumber серийный номер выполнения, которое нужно удалить
     * @return {@code true}, если удаление прошло успешно, или {@code false}, если выполнение не найдено
     */
    @Override
    public boolean deleteHabitCompletion(Habit habit, int serialNumber) {
        String query = "DELETE FROM app_schema.habit_completion WHERE habit_id = ? AND serial_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, habit.getId());
            statement.setInt(2, serialNumber);
            int rowsAffected = statement.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Удаляет все выполнения для указанной привычки.
     *
     * @param habit привычка {@link Habit}, для которой нужно удалить все выполнения
     * @return {@code true}, если все выполнения были удалены, или {@code false}, если для привычки нет выполнений
     */
    @Override
    public boolean deleteAllHabitCompletion(Habit habit) {
        String query = "DELETE FROM app_schema.habit_completion WHERE habit_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, habit.getId());
            int rowsAffected = statement.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Получает все выполнения привычки для указанной привычки.
     *
     * @param habit привычка {@link Habit}, для которой нужно получить все выполнения
     * @return список объектов {@link HabitCompletion}, или пустой список, если выполнения отсутствуют
     */
    @Override
    public List<HabitCompletion> getAllHabitCompletion(Habit habit) {
        List<HabitCompletion> completions = new ArrayList<>();
        String query = "SELECT * FROM app_schema.habit_completion WHERE habit_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, habit.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                completions.add(mapToHabitCompletion(resultSet, habit));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return completions;
    }

    /**
     * Получает все выполнения привычки для указанной привычки начиная с указанной даты.
     * Серийный номер вычисляется на основе частоты привычки (ежедневной или еженедельной).
     *
     * @param habit привычка {@link Habit}, для которой нужно получить выполнения
     * @param date дата {@link LocalDate}, после которой нужно получить выполнения
     * @return список объектов {@link HabitCompletion} для привычки на или после указанной даты
     */
    @Override
    public List<HabitCompletion> getAllHabitCompletionByDate(Habit habit, LocalDate date) {
        List<HabitCompletion> result = new ArrayList<>();
        String query = "SELECT * FROM app_schema.habit_completion WHERE habit_id = ? AND mark_date >= ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, habit.getId());
            statement.setDate(2, Date.valueOf(date));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(mapToHabitCompletion(resultSet, habit));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private HabitCompletion mapToHabitCompletion(ResultSet resultSet, Habit habit) throws SQLException {
        int serialNumber = resultSet.getInt("serial_number");
        LocalDate markDate = resultSet.getDate("mark_date").toLocalDate();
        return new HabitCompletion(markDate, habit, serialNumber);
    }
}

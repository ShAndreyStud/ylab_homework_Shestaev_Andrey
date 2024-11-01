package com.habittracker.repository;

import com.habittracker.config.DatabaseConfig;
import com.habittracker.infrastructure.db.DatabaseConnection;
import com.habittracker.model.User;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class UserRepositoryImpl implements UserRepository{
    private final DatabaseConfig config;

    public UserRepositoryImpl(DatabaseConfig config) {
        this.config = config;
    }

    /**
     * Возвращает пользователя по его email.
     *
     * @param email адрес электронной почты пользователя
     * @return объект {@link User}, если пользователь найден, или {@code null}, если пользователя нет
     */
    @Override
    public User getUser(String email) {
        try (DatabaseConnection dbConnection = new DatabaseConnection(config);
             Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserQueries.SELECT_USER_BY_EMAIL)) {

            statement.setString(1, email);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Добавляет нового пользователя в репозиторий.
     * Если пользователь с таким email уже существует, метод возвращает {@code false}.
     *
     * @param user новый пользователь {@link User}, который будет добавлен
     * @return {@code true}, если пользователь был успешно добавлен, или {@code false}, если пользователь с таким email уже существует
     */
    @Override
    public boolean addUser(User user) {
        try (DatabaseConnection dbConnection = new DatabaseConnection(config);
             Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserQueries.INSERT_USER)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole().name());
            statement.setBoolean(5, user.isBlocked());

            int rowsAffected = statement.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Обновляет имя и email пользователя.
     * Если новый email уже используется другим пользователем, обновление не происходит.
     *
     * @param user пользователь, для которого нужно обновить данные
     * @param newName новое имя пользователя
     * @param newEmail новый адрес электронной почты пользователя
     * @return {@code true}, если обновление прошло успешно, или {@code false}, если новый email уже существует или пользователь не найден
     */
    @Override
    public boolean updateUser(User user, String newName, String newEmail) {
        try (DatabaseConnection dbConnection = new DatabaseConnection(config);
             Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserQueries.UPDATE_USER)) {

            statement.setString(1, newName);
            statement.setString(2, newEmail);
            statement.setString(3, user.getEmail());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Блокирует пользователя.
     *
     * @param user пользователь, которого нужно заблокировать
     * @param block статус блокировки (true для блокировки)
     * @return {@code true}, если блокировка успешно обновлена, или {@code false}, если пользователь не найден
     */
    @Override
    public boolean blockUser(User user, Boolean block) {
        try (DatabaseConnection dbConnection = new DatabaseConnection(config);
             Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserQueries.UPDATE_BLOCK_USER)) {

            statement.setBoolean(1, block);
            statement.setString(2, user.getEmail());
            int rowsAffected = statement.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Удаляет пользователя из репозитория.
     *
     * @param user пользователь, которого нужно удалить
     * @return {@code true}, если пользователь был успешно удален, или {@code false}, если пользователя не существует
     */
    @Override
    public boolean deleteUser(User user) {
        try (DatabaseConnection dbConnection = new DatabaseConnection(config);
             Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserQueries.DELETE_USER)) {

            statement.setString(1, user.getEmail());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Обновляет пароль пользователя.
     *
     * @param user пользователь, для которого нужно обновить пароль
     * @param newPassword новый пароль пользователя
     * @return обновленный объект {@link User} с новым паролем
     */
    @Override
    public User updateUserPassword(User user, String newPassword) {
        try (DatabaseConnection dbConnection = new DatabaseConnection(config);
             Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserQueries.UPDATE_USER_PASSWORD)) {

            statement.setString(1, newPassword);
            statement.setString(2, user.getEmail());
            if (statement.executeUpdate() > 0) {
                connection.commit();
                user.setPassword(newPassword);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Возвращает список всех пользователей, исключая пользователей с ролью ADMIN.
     *
     * @return список всех пользователей {@link User}, за исключением администраторов
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (DatabaseConnection dbConnection = new DatabaseConnection(config);
             Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserQueries.SELECT_ALL_USERS)) {

            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    users.add(mapRowToUser(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setEmail(resultSet.getString("email"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setBlocked(resultSet.getBoolean("is_blocked"));
        user.setRole(User.Role.valueOf(resultSet.getString("role")));
        return user;
    }

    @Override
    public Integer getUserIdByEmail(String email) {
        try (DatabaseConnection dbConnection = new DatabaseConnection(config);
             Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserQueries.SELECT_USER_ID_BY_EMAIL)) {

            preparedStatement.setString(1, email);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

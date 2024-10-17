package com.habittracker.service;
/*
import com.habittracker.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, User> users = new HashMap<>();

    public UserService() {
        // Создаем администратора с заранее известными данными
        User admin = new User("Admin", "admin", "admin", true);
        users.put(admin.getEmail(), admin);
    }

    // Регистрация нового пользователя
    public String registerUser(String name, String email, String password) {
        if (users.containsKey(email)) {
            return "Error: A user with this email address is already registered.";
        }

        User user = new User(name, email, password);
        users.put(email, user);
        return "The registration was successful.";
    }

    // Авторизация пользователя
    public String loginUser(String email, String password) {
        User user = users.get(email);

        if (user == null) {
            return "Error: No user with this email was found.";
        }

        if (!user.getPassword().equals(password)) {
            return "Error: Incorrect password.";
        }

        return "Authorization was successful. Welcome, " + user.getName() + "!";
    }

    // Редактирование профиля пользователя
    public String updateUserProfile(String email, String newName, String newEmail) {
        User user = users.get(email);

        if (user == null) {
            return "Error: User not found.";
        }

        // Проверка на уникальность нового email
        if (!email.equals(newEmail) && users.containsKey(newEmail)) {
            return "Error: The new email is already taken by another user.";
        }

        // Обновление данных пользователя
        user.setName(newName);
        user.setEmail(newEmail);

        // Если email изменился, обновляем ключ в коллекции
        if (!email.equals(newEmail)) {
            users.remove(email);
            users.put(newEmail, user);
        }

        return "The profile has been successfully updated.";
    }

    // Удаление аккаунта пользователя
    public String deleteUser(String email) {
        if (users.remove(email) != null) {
            return "The user account has been successfully deleted.";
        } else {
            return "Error: User not found.";
        }
    }

    public String resetPassword(String email) {
        User user = users.get(email);

        if (user == null) {
            return "Error: User not found.";
        }

        // Генерация временного пароля
        String temporaryPassword = "temp1234";
        user.setPassword(temporaryPassword);

        // Симуляция отправки на email
        return "Temporary password sent to " + email + ": " + temporaryPassword;
    }


    public User getUserByEmail(String email) {
        return users.get(email);
    }

    public Map<String, User> getAllUsers() {
        Map <String, User> withoutAdmin = new HashMap<>(users);
        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (entry.getValue().isAdmin()) {
                withoutAdmin.remove(entry.getKey());
            }
        }
        return withoutAdmin;
    }
}
*/
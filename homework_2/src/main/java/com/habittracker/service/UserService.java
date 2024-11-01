package com.habittracker.service;


import com.habittracker.model.User;
import com.habittracker.repository.UserRepository;
import com.habittracker.repository.UserRepositoryImpl;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static com.habittracker.model.User.Role;

/**
 * Сервисный класс для управления операциями с пользователями, такими как регистрация,
 * вход в систему, обновление профиля и прочее.
 * Этот класс взаимодействует с UserRepository для выполнения CRUD операций с пользователями.
 */
public class UserService {
    /**
     * Репозиторий пользователей, который используется для доступа к данным пользователей.
     */
    public UserRepository userRepository;

    /**
     * Конструктор по умолчанию, который инициализирует сервис с администратором по умолчанию.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Регистрирует нового пользователя с указанными именем, электронной почтой и паролем.
     *
     * @param name     имя пользователя, которого нужно зарегистрировать.
     * @param email    электронная почта пользователя, которого нужно зарегистрировать.
     * @param password пароль пользователя, которого нужно зарегистрировать.
     * @return сообщение, указывающее на успех или неудачу.
     */
    public String registerUser(String name, String email, String password) {
        if (userRepository.getUser(email) != null) {
            return "Error: A user with this email address is already registered.";
        }

        User user = new User(name, email, password, Role.USER);
        userRepository.addUser(user);
        return "The registration was successful.";
    }

    /**
     * Выполняет вход пользователя, проверяя предоставленные электронную почту и пароль.
     *
     * @param email    электронная почта пользователя, который пытается войти.
     * @param password пароль пользователя, который пытается войти.
     * @return Optional, содержащий пользователя, если учетные данные верны, или пустой Optional, если нет.
     */
    public Optional<User> loginUser(String email, String password) {
        User user = userRepository.getUser(email);
        if (user == null || !user.getPassword().equals(password)) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    /**
     * Обновляет профиль пользователя, изменяя его имя и/или электронную почту.
     * Проверяет, чтобы новая электронная почта была уникальной.
     *
     * @param user     пользователь, чей профиль нужно обновить.
     * @param newName  новое имя для пользователя.
     * @param newEmail новая электронная почта для пользователя.
     * @return Optional, содержащий обновленного пользователя, если операция успешна, или пустой Optional, если почта уже занята.
     */
    public Optional<User> updateUserProfile(User user, String newName, String newEmail) {
        if (!user.getEmail().equals(newEmail) && userRepository.getUser(newEmail) != null) {
            return Optional.empty();
        }

        userRepository.updateUser(user, newName, newEmail);
        return Optional.of(user);
    }

    /**
     * Блокирует указанного пользователя.
     *
     * @param user  пользователь, которого нужно заблокировать или разблокировать.
     * @param block статус блокировки (true для блокировки).
     * @return Optional, содержащий обновленного пользователя.
     */
    public Optional<User> updateUserProfile(User user, Boolean block) {

        userRepository.blockUser(user, block);

        return Optional.of(user);
    }

    /**
     * Удаляет указанного пользователя из системы.
     *
     * @param currentUser пользователь, которого нужно удалить.
     */
    public void deleteUser(User currentUser) {
        userRepository.deleteUser(currentUser);
    }

    /**
     * Сбрасывает пароль указанного пользователя.
     *
     * @param currentUser пользователь, чей пароль нужно сбросить.
     * @param newPassword новый пароль для установки.
     * @return обновленный пользователь с новым паролем.
     */
    public User resetPassword(User currentUser, String newPassword) {
        return userRepository.updateUserPassword(currentUser, newPassword);
    }

    /**
     * Возвращает список всех пользователей, за исключением администраторов.
     *
     * @return список всех пользователей (кроме администраторов) в системе.
     */
    public List<User> getAllUsers(){
        return userRepository.getAllUsers();
    }
}
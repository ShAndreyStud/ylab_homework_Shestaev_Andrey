package com.habittracker.repository;

import com.habittracker.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link UserRepository}, предоставляющая методы для управления пользователями.
 * Пользователи хранятся в памяти с использованием {@link Map}, где ключом является email пользователя,
 * а значением - объект {@link User}.
 *
 * <p>Класс предоставляет функциональность для:
 * <ul>
 *     <li>Получения пользователя по email</li>
 *     <li>Добавления нового пользователя</li>
 *     <li>Обновления данных пользователя</li>
 *     <li>Блокировки пользователя</li>
 *     <li>Удаления пользователя</li>
 *     <li>Обновления пароля пользователя</li>
 *     <li>Получения списка всех пользователей</li>
 * </ul>
 */
public class UserRepositoryImpl implements UserRepository{
    private Map<String, User> users = new HashMap<>();

    /**
     * Возвращает пользователя по его email.
     *
     * @param email адрес электронной почты пользователя
     * @return объект {@link User}, если пользователь найден, или {@code null}, если пользователя нет
     */
    @Override
    public User getUser(String email) {
        return users.get(email);
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

        if (user == null || users.containsKey(user.getEmail())) {
            return false;
        }

        users.put(user.getEmail(), user);
        return true;
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
        User existingUser = users.get(user.getEmail());

        if (existingUser == null || newEmail.isEmpty()) {
            return false;
        }

        if (users.containsKey(newEmail) && !newEmail.equals(user.getEmail())) {
            return false;
        }

        existingUser.setName(newName);
        users.remove(existingUser.getEmail());
        existingUser.setEmail(newEmail);
        users.put(newEmail, existingUser);
        return true;
    }

    /**
     * Блокирует пользователя.
     *
     * @param user пользователь, которого нужно заблокировать
     * @param block статус блокировки (true для блокировки)
     * @return {@code true}, если блокировка успешно обновлена, или {@code false}, если пользователь не найден
     */
    public boolean blockUser(User user, Boolean block){
        User existingUser = users.get(user.getEmail());

        if (existingUser == null) {
            return false;
        }

        existingUser.setBlocked(true);
        users.remove(user.getEmail());
        users.put(existingUser.getEmail(), existingUser);
        return true;
    }

    /**
     * Удаляет пользователя из репозитория.
     *
     * @param user пользователь, которого нужно удалить
     * @return {@code true}, если пользователь был успешно удален, или {@code false}, если пользователя не существует
     */
    @Override
    public boolean deleteUser(User user) {

        if (!users.containsKey(user.getEmail())) {
            return false;
        }

        users.remove(user.getEmail());
        return true;
    }

    /**
     * Обновляет пароль пользователя.
     *
     * @param user пользователь, для которого нужно обновить пароль
     * @param newPassword новый пароль пользователя
     * @return обновленный объект {@link User} с новым паролем
     */
    @Override
    public User updateUserPassword(User user, String newPassword){
        users.get(user.getEmail()).setPassword(newPassword);
        return users.get(user.getEmail());
    }

    /**
     * Возвращает список всех пользователей, исключая пользователей с ролью ADMIN.
     *
     * @return список всех пользователей {@link User}, за исключением администраторов
     */
    @Override
    public List<User> getAllUsers() {
        return users.values().stream()
                .filter(user -> !user.getRole().equals(User.Role.ADMIN))
                .collect(Collectors.toList());
    }
}

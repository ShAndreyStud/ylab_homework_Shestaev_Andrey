package com.habittracker.repository;


import com.habittracker.model.User;

import java.util.List;


/**
 * Интерфейс для репозитория пользователей.
 * Определяет методы для управления данными пользователей.
 */
public interface UserRepository {

    /**
     * Получает пользователя по заданному адресу электронной почты.
     *
     * @param email Адрес электронной почты пользователя.
     * @return Пользователь, если найден; иначе null.
     */
    User getUser(String email);

    /**
     * Добавляет нового пользователя в репозиторий.
     *
     * @param user Пользователь, которого необходимо добавить.
     * @return true, если пользователь был успешно добавлен; иначе false.
     */
    boolean addUser(User user);

    /**
     * Обновляет данные пользователя.
     *
     * @param user      Пользователь, данные которого необходимо обновить.
     * @param newName   Новое имя пользователя.
     * @param newEmail  Новый адрес электронной почты пользователя.
     * @return true, если данные пользователя были успешно обновлены; иначе false.
     */
    boolean updateUser(User user, String newName, String newEmail);

    /**
     * Удаляет пользователя из репозитория.
     *
     * @param user Пользователь, которого необходимо удалить.
     * @return true, если пользователь был успешно удален; иначе false.
     */
    boolean deleteUser(User user);

    /**
     * Обновляет пароль пользователя.
     *
     * @param user        Пользователь, пароль которого необходимо обновить.
     * @param newPassword Новый пароль пользователя.
     * @return Обновленный пользователь с новым паролем.
     */
    User updateUserPassword(User user, String newPassword);

    /**
     * Получает список всех пользователей.
     *
     * @return Список всех пользователей в репозитории.
     */
    List<User> getAllUsers();

    /**
     * Блокирует пользователя.
     *
     * @param user Пользователь, которого необходимо заблокировать или разблокировать.
     * @param block true, чтобы заблокировать пользователя; false, чтобы разблокировать.
     * @return true, если операция блокировки была успешной; иначе false.
     */
    boolean blockUser(User user, Boolean block);

    /**
     * Получает идентификатор пользователя по его адресу электронной почты.
     *
     * @param email Адрес электронной почты пользователя.
     * @return Идентификатор пользователя, если найден; иначе null.
     */
    Integer getUserIdByEmail(String email);
}

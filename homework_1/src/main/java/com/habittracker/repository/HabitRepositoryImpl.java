package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация интерфейса {@link HabitRepository}, предназначенная для управления привычками пользователей.
 * Хранит привычки пользователей в памяти с использованием {@link Map}, где ключом является объект {@link User},
 * а значением - карта, сопоставляющая названия привычек с объектами {@link Habit}.
 *
 * <p>Класс предоставляет функциональность для:
 * <ul>
 *     <li>Получения привычки по имени</li>
 *     <li>Добавления новой привычки</li>
 *     <li>Обновления существующей привычки</li>
 *     <li>Удаления привычки</li>
 *     <li>Получения всех привычек пользователя</li>
 * </ul>
 */
public class HabitRepositoryImpl implements HabitRepository{
    private Map<User, Map <String, Habit>> habits = new HashMap<>();

    /**
     * Возвращает привычку пользователя по её названию.
     *
     * @param user пользователь, для которого ищется привычка
     * @param habitName название привычки
     * @return объект {@link Habit}, если привычка найдена, или {@code null}, если привычка отсутствует
     */
    @Override
    public Habit getHabit(User user, String habitName) {
        Map<String, Habit> userHabits = habits.get(user);

        if (userHabits == null) {
            return null;
        }

        return habits.get(user).get(habitName);
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
        Map<String, Habit> userHabits = habits.get(user);

        if (userHabits == null) {
            userHabits = new HashMap<>();
            habits.put(user, userHabits);
        }

        if (userHabits.containsKey(newHabit.getName())) {
            return false;
        }

        userHabits.put(newHabit.getName(), newHabit);
        return true;
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
    public boolean updateHabit(User user, Habit habit, String newName, String newDescription, Habit.Frequency newFrequency) {
        Map<String, Habit> userHabits = habits.get(user);

        if (userHabits == null || !userHabits.containsKey(habit.getName())) {
            return false;
        }

        Habit existingHabit = new Habit(habit.getName(), habit.getDescription(), habit.getFrequency(), habit.getUser());
        existingHabit.setName(newName);
        existingHabit.setDescription(newDescription);
        existingHabit.setFrequency(newFrequency);

        userHabits.remove((habit.getName()));
        userHabits.put(newName, existingHabit);

        return true;
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
        Map<String, Habit> userHabits = habits.get(user);

        if (userHabits == null || !userHabits.containsKey(habitName)) {
            return false;
        }
        userHabits.remove(habitName);
        return true;
    }

    /**
     * Возвращает список всех привычек пользователя.
     *
     * @param user пользователь, для которого нужно получить список привычек
     * @return список объектов {@link Habit}, или пустой список, если у пользователя нет привычек
     */
    @Override
    public List<Habit> getAllHabits(User user) {
        Map<String, Habit> userHabits = habits.get(user);

        if (userHabits == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(userHabits.values());
    }
}

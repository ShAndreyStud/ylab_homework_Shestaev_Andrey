package com.habittracker.repository;

import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Реализация интерфейса {@link HabitCompletionRepository}, которая управляет записями о выполнении привычек пользователей.
 * Этот класс использует {@link Map} для хранения записей выполнения привычек для каждого пользователя.
 * Каждая привычка сопоставляется с коллекцией выполнений, идентифицируемых их серийными номерами.
 * Серийный номер представляет собой последовательный номер, соответствующий номеру возможной отметки со дня добавления привычки в список.
 *
 * <p>Класс предоставляет следующие методы:
 * <ul>
 *   <li>Получение конкретного выполнения привычки по серийному номеру</li>
 *   <li>Добавление новой записи о выполнении привычки</li>
 *   <li>Обновление или удаление существующей записи</li>
 *   <li>Получение всех выполнений для определенной привычки</li>
 *   <li>Фильтрация выполнений по конкретной дате</li>
 * </ul>
 *
 * <p>Все выполнения хранятся в памяти внутри класса и сопоставляются с объектом {@link Habit}.
 */
public class HabitCompletionRepositoryImpl implements HabitCompletionRepository{
    private Map<Habit, Map<Integer, HabitCompletion>> completions = new HashMap<>();

    /**
     * Получает выполнение привычки по заданному серийному номеру для указанной привычки.
     *
     * @param habit привычка {@link Habit}, для которой нужно получить выполнение
     * @param serialNumber серийный номер выполнения привычки
     * @return выполнение привычки {@link HabitCompletion}, если найдено, или {@code null}, если отсутствует
     */
    @Override
    public HabitCompletion getHabitCompletion(Habit habit, Integer serialNumber) {
        Map<Integer, HabitCompletion> habitCompletions = completions.get(habit);

        if (habitCompletions == null) {
            return null;
        }
        return completions.get(habit).get(serialNumber);
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
        Map<Integer, HabitCompletion> habitCompletions = completions.get(habit);

        if (habitCompletions == null) {
            habitCompletions = new HashMap<>();
            habitCompletions.put(newCompletion.getSerialNumber(), newCompletion);
            completions.put(habit, habitCompletions);
            return true;
        }

        if(habitCompletions.isEmpty()){
            habitCompletions.put(newCompletion.getSerialNumber(), newCompletion);
            return true;
        }

        if (habitCompletions.containsKey(newCompletion.getSerialNumber())) {
            return false;
        }

        habitCompletions.put(newCompletion.getSerialNumber(), newCompletion);
        return true;
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
        Map<Integer, HabitCompletion> habitCompletions = completions.get(habit);

        if (habitCompletions == null || !habitCompletions.containsKey(serialNumber)) {
            return false;
        }
        habitCompletions.remove(serialNumber, habitCompletions.get(serialNumber));
        habitCompletions.put(serialNumber, updatedCompletion);
        return true;
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
        Map<Integer, HabitCompletion> habitCompletions = completions.get(habit);

        if (habitCompletions == null || !habitCompletions.containsKey(serialNumber)) {
            return false;
        }

        habitCompletions.remove(serialNumber);
        return true;
    }

    /**
     * Удаляет все выполнения для указанной привычки.
     *
     * @param habit привычка {@link Habit}, для которой нужно удалить все выполнения
     * @return {@code true}, если все выполнения были удалены, или {@code false}, если для привычки нет выполнений
     */
    @Override
    public boolean deleteAllHabitCompletion(Habit habit) {
        Map<Integer, HabitCompletion> habitCompletions = completions.get(habit);

        if(habitCompletions == null){
            return true;
        }

        if(!habitCompletions.containsKey(habit)){
            return false;
        }
        habitCompletions = null;
        return true;
    }

    /**
     * Получает все выполнения привычки для указанной привычки.
     *
     * @param habit привычка {@link Habit}, для которой нужно получить все выполнения
     * @return список объектов {@link HabitCompletion}, или пустой список, если выполнения отсутствуют
     */
    @Override
    public List<HabitCompletion> getAllHabitCompletion(Habit habit){
        Map<Integer, HabitCompletion> habitCompletions = completions.get(habit);

        if (habitCompletions == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(habitCompletions.values());
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
    public List<HabitCompletion> getAllHabitCompletionByDate(Habit habit, LocalDate date){
        List<HabitCompletion> result = new ArrayList<>();
        Map<Integer, HabitCompletion> habitCompletions = completions.get(habit);

        if(habitCompletions == null){
            return result;
        }

        List<HabitCompletion> listHabitComp = new ArrayList<>(habitCompletions.values());

        int currentSerialNumber;
        if(habit.getFrequency().equals(Habit.Frequency.DAILY)){
            currentSerialNumber = (int) ChronoUnit.DAYS.between(habit.getCreateDate(), date) + 1;
        } else {
            currentSerialNumber = (int) ChronoUnit.WEEKS.between(habit.getCreateDate(), date) + 1;
        }

        if (habitCompletions == null) {
            return new ArrayList<>();
        }

        for(HabitCompletion comp : listHabitComp){
            if( !(comp.getSerialNumber() < currentSerialNumber) ){
                result.add(comp);
            }
        }

        return result;
    }
}

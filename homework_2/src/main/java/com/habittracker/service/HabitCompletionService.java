package com.habittracker.service;

import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;
import com.habittracker.repository.HabitCompletionRepository;
import com.habittracker.repository.HabitCompletionRepositoryImpl;
import com.habittracker.repository.UserRepositoryImpl;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс {@code HabitCompletionService} предоставляет сервисные методы для работы с завершениями привычек.
 * Он взаимодействует с репозиторием {@link HabitCompletionRepository}, чтобы сохранять, удалять и получать данные о выполненных привычках.
 */
public class HabitCompletionService {

    /**
     * Репозиторий завершений привычек, который используется для доступа к данным завершений привычек.
     */
    public HabitCompletionRepository habitCompletionRepository;

    /**
     * Конструктор класса HabitCompletionService.
     *
     * @param habitCompletionRepository Репозиторий завершений привычек, который будет использоваться в сервисе.
     */
    public HabitCompletionService(HabitCompletionRepository habitCompletionRepository) {
        this.habitCompletionRepository = habitCompletionRepository;
    }
    /**
     * Создает и сохраняет завершение привычки на заданную дату.
     * Если завершение привычки успешно добавлено в репозиторий, возвращает сообщение об успехе.
     * Если произошла ошибка (например, завершение уже существует), возвращает сообщение об ошибке.
     *
     * @param localDate дата завершения привычки
     * @param habit привычка, для которой создается завершение
     * @return сообщение о результате операции: "Habit successfully marked." при успехе или "Error." при ошибке
     */
    public String createHabitCompletion(LocalDate localDate, Habit habit) {

        HabitCompletion habitCompletion = new HabitCompletion(localDate, habit);
        if (habitCompletionRepository.addHabitCompletion(habit, habitCompletion)) {
            return "Habit successfully marked.";
        } else {
            return "Error.";
        }
    }

    /**
     * Удаляет все завершения привычки для указанной привычки.
     * Если переданная привычка является {@code null}, метод завершает выполнение без действия.
     *
     * @param habit привычка, для которой необходимо удалить все завершения
     */
    public void deleteAllHabitCompletion(Habit habit){
        if(habit == null){
            return;
        }
        habitCompletionRepository.deleteAllHabitCompletion(habit);
    }


    /**
     * Возвращает список всех завершений привычки для указанной привычки.
     * Если привычка является {@code null}, возвращается пустой список.
     *
     * @param habit привычка, для которой нужно получить все завершения
     * @return список завершений привычки {@link HabitCompletion}, или пустой список, если привычка не существует
     */
    public List<HabitCompletion> getAllHabitCompletions(Habit habit) {
        if(habit == null){
            return new ArrayList<>();
        }
        return habitCompletionRepository.getAllHabitCompletion(habit);
    }
}


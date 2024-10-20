package com.habittracker.service;

import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;
import com.habittracker.model.User;

import com.habittracker.repository.HabitCompletionRepository;
import com.habittracker.repository.HabitRepository;
import com.habittracker.repository.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс {@code HabitService} предоставляет сервисные методы для работы с привычками пользователей.
 * Он взаимодействует с репозиториями привычек и завершений привычек для выполнения операций CRUD (создание, обновление, удаление) и другой бизнес-логики.
 */
public class HabitService {
    public HabitRepository habitRepository;
    HabitCompletionRepository habitCompletionRepository;

    /**
     * Конструктор для инициализации сервиса привычек с репозиториями пользователей, завершений привычек и привычек.
     *
     * @param habitCompletionRepository репозиторий завершений привычек
     * @param habitRepository репозиторий привычек
     */
    public HabitService(HabitCompletionRepository habitCompletionRepository, HabitRepository habitRepository) {
        this.habitCompletionRepository = habitCompletionRepository;
        this.habitRepository = habitRepository;
    }


    /**
     * Создает новую привычку для указанного пользователя. Если привычка с таким именем уже существует, возвращает сообщение об ошибке.
     *
     * @param user пользователь, создающий привычку
     * @param name имя привычки
     * @param description описание привычки
     * @param frequency частота выполнения привычки (ежедневная, еженедельная)
     * @return строка с результатом операции: "Habit successfully added." при успехе или "Error." при ошибке
     */
    public String createHabit(User user, String name, String description, Habit.Frequency frequency) {
        if (habitRepository.getHabit(user, name) != null) {
            return "Error: you already have a habit by that name.";
        }

        Habit habit = new Habit(name, description, frequency, user);
        if (habitRepository.addHabit(user, habit)) {
            return "Habit successfully added.";
        } else {
            return "Error.";
        }
    }

    /**
     * Возвращает все привычки для указанного пользователя.
     *
     * @param user пользователь, для которого нужно получить все привычки
     * @return список всех привычек пользователя
     */
    public List<Habit> getAllHabits(User user) {
        return habitRepository.getAllHabits(user);
    }

    /**
     * Обновляет существующую привычку пользователя с новыми данными.
     *
     * @param currentUser текущий пользователь
     * @param habit привычка, которую нужно обновить
     * @param newName новое имя привычки
     * @param newDescription новое описание привычки
     * @param newFrequency новая частота выполнения привычки
     * @return true, если обновление прошло успешно, иначе false
     */
    public boolean updateHabit(User currentUser, Habit habit, String newName, String newDescription, Habit.Frequency newFrequency) {
        return habitRepository.updateHabit(currentUser, habit, newName, newDescription, newFrequency);
    }

    /**
     * Удаляет привычку пользователя по имени.
     *
     * @param currentUser текущий пользователь
     * @param habitName имя привычки, которую нужно удалить
     * @return true, если удаление прошло успешно, иначе false
     */
    public boolean deleteHabit(User currentUser, String habitName) {
        return habitRepository.deleteHabit(currentUser, habitName);
    }

    /**
     * Сортирует список привычек по дате их создания.
     *
     * @param habitList список привычек
     * @return отсортированный список привычек
     */
    public List<Habit> getHabitsByDate(List<Habit> habitList) {
        return habitList.stream()
                .sorted(Comparator.comparing(Habit::getCreateDate))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список доступных для выполнения привычек, основываясь на их частоте и завершениях.
     * Ежедневные привычки доступны, если они не были выполнены сегодня. Еженедельные привычки доступны, если они не были выполнены на этой неделе.
     *
     * @param habitList список привычек
     * @return список доступных привычек
     */
    public List<Habit> getAvailableHabits(List<Habit> habitList) {
        List<Habit> availableHabits = new ArrayList<>();

        for(Habit habit : habitList){
            List<HabitCompletion> habitCompletions = habitCompletionRepository.getAllHabitCompletion(habit);

            if(habit.getFrequency() == Habit.Frequency.DAILY){

                if( habitCompletions.isEmpty() || !(habitCompletions.get(habitCompletions.size() - 1).getMarkDate().isEqual(LocalDate.now())) ){
                    availableHabits.add(habit);
                }

            } else {
                if( habitCompletions.isEmpty() ){
                    availableHabits.add(habit);
                } else {
                    int lastWeekCompletion = habitCompletions.get(habitCompletions.size() - 1).getSerialNumber();
                    int todayWeek = (int) ChronoUnit.WEEKS.between(habit.getCreateDate(), LocalDate.now()) + 1;
                    if( !(todayWeek == lastWeekCompletion) ){
                        availableHabits.add(habit);
                    }
                }
            }
        }

        return availableHabits;
    }

    /**
     * Подсчитывает текущую серию успешных выполнений привычки.
     * Метод проверяет последовательные выполненные привычки начиная с последнего дня или недели.
     *
     * @param habit привычка, для которой нужно подсчитать серию
     * @return количество дней или недель в серии
     */
    public int countHabitStreak(Habit habit){
        int result = 0;
        int currentSerialNumber;
        if(habit.getFrequency().equals(Habit.Frequency.DAILY)){
            currentSerialNumber = (int) ChronoUnit.DAYS.between(habit.getCreateDate(), LocalDate.now()) + 1;
        } else {
            currentSerialNumber = (int) ChronoUnit.WEEKS.between(habit.getCreateDate(), LocalDate.now()) + 1;
        }
        List<HabitCompletion> habitCompletions = habitCompletionRepository.getAllHabitCompletion(habit);
        List<HabitCompletion> sortedCompletions = habitCompletions.stream()
                .sorted(Comparator.comparingInt(HabitCompletion::getSerialNumber).reversed())
                .collect(Collectors.toList());
        for(HabitCompletion comp : sortedCompletions){
            if(comp.getSerialNumber() == currentSerialNumber){
                result++;
                currentSerialNumber--;
            } else {
                break;
            }
        }

        return result;
    }

    /**
     * Рассчитывает процент выполнения привычки за указанный период.
     * Подсчитывает количество успешных завершений привычки и делит на максимальное возможное количество завершений.
     *
     * @param startDate начальная дата периода
     * @param habit привычка, для которой рассчитывается процент выполнения
     * @return процент успешных выполнений привычки за указанный период
     */
    public int countPercentage(LocalDate startDate, Habit habit) {
        double percentage = 0;
        List<HabitCompletion> habitCompletions = habitCompletionRepository.getAllHabitCompletionByDate(habit, startDate);

        if(habitCompletions == null){
            return 0;
        }

        int count = habitCompletions.size();
        LocalDate createDate = habit.getCreateDate();

        int max;
        if(habit.getFrequency().equals(Habit.Frequency.DAILY)){
            if(startDate.isBefore(createDate)) {
                max = (int) ChronoUnit.DAYS.between(createDate, LocalDate.now()) + 1;
            } else {
                max = (int) ChronoUnit.DAYS.between(startDate, LocalDate.now()) + 1;
            }
        } else {
            if(startDate.isBefore(createDate)) {
                max = (int) ChronoUnit.WEEKS.between(createDate, LocalDate.now()) + 1;
            } else {
                int serialNumberByDate = (int) ChronoUnit.WEEKS.between(createDate, startDate);
                int currentSerialNumber = (int) ChronoUnit.WEEKS.between(createDate, LocalDate.now()) + 1;
                max = currentSerialNumber - serialNumberByDate;
            }
        }
        percentage = (double) count / max;
        return (int) Math.round(percentage * 100);

    }
}



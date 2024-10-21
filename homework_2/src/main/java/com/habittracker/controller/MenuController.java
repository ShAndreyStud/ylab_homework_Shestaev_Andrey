package com.habittracker.controller;

import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;
import com.habittracker.model.User;
import com.habittracker.service.HabitCompletionService;
import com.habittracker.service.HabitService;
import com.habittracker.service.UserService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Контроллер меню, который управляет взаимодействием с пользователем через консоль.
 * Позволяет пользователю регистрироваться, входить в систему, управлять профилем и привычками.
 * Для администратора доступны действия по управлению пользователями.
 */
public class MenuController {

    private User currentUser;
    UserService userService;
    HabitService habitService;
    HabitCompletionService habitCompletionService;
    String choice;
    int intChoice;
    List<User> allUsers;
    Scanner scanner;
    String email;
    String name;
    String password;
    Habit.Frequency frequency;

    List<Habit> allHabits;

    /**
     * Конструктор, который инициализирует контроллер с необходимыми сервисами.
     *
     * @param userService            сервис для управления пользователями
     * @param habitService           сервис для управления привычками
     * @param habitCompletionService сервис для управления выполнением привычек
     */
    public MenuController(UserService userService, HabitService habitService, HabitCompletionService habitCompletionService) {
        this.userService = userService;
        this.habitService = habitService;
        this.habitCompletionService = habitCompletionService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает меню программы. В зависимости от статуса авторизации пользователя
     * показывает меню для неавторизованных пользователей, меню пользователя или администратора.
     * Цикл продолжается до тех пор, пока пользователь явно не завершит программу.
     */
    public void start() {

        while (true) {
            if (currentUser == null) {
                currentUser = showStartMenu();
            } else {
                if(currentUser.getRole() == User.Role.USER){
                    currentUser = showUserMenu();
                } else {
                    currentUser = showAdminMenu();
                }
            }
        }
    }

    /**
     * Отображает стартовое меню для неавторизованных пользователей. Позволяет зарегистрироваться,
     * войти в систему или завершить программу.
     *
     * @return объект пользователя, если пользователь успешно авторизован, или null в случае выхода или ошибки.
     */
    private User showStartMenu() {

        System.out.println("-------------------------------------------------");
        System.out.println("\nSelect an action:");
        System.out.println("1. Registration");
        System.out.println("2. Authorisation");
        System.out.println("3. Exit");
        System.out.println("-------------------------------------------------");

        choice = scanner.nextLine();
        switch (choice) {
            case "1":
                handleRegistration();
                return null;
            case "2":
                return handleAutorisation();
            case "3":
                System.out.println("Goodbye!");
                scanner.close();
                System.exit(0);
                return null;
            default:
                System.out.println("Wrong command. Try again.");
                return null;
        }
    }

    /**
     * Отображает меню для пользователя. Дает возможность управлять профилем и привычками, а также выйти из системы.
     *
     * @return объект пользователя, если действия выполнены успешно, или null в случае выхода или блокировки.
     */
    private User showUserMenu() {

        if(currentUser.isBlocked()){
            System.out.println("You're blocked!");
            return null;
        }

        System.out.println("-------------------------------------------------");
        System.out.println("\nSelect an action:");
        System.out.println("1. Editing a profile");
        System.out.println("2. Habit management");
        System.out.println("3. Log out");
        System.out.println("-------------------------------------------------");

        choice = scanner.nextLine();
        switch (choice) {
            case "1":
                return showEditUserMenu();
            case "2":
                showHabitManageMenu();
                return currentUser;
            case "3":
                System.out.println("You have successfully logged out of your account.");
                return null;
            default:
                System.out.println("Wrong command. Try again.");
                return currentUser;
        }
    }

    /**
     * Отображает меню редактирования профиля пользователя. Позволяет изменять данные профиля,
     * удалять аккаунт, обновлять пароль или вернуться в предыдущее меню.
     *
     * @return объект текущего пользователя или null в случае удаления пользователя.
     */
    private User showEditUserMenu (){
        while(true) {

            System.out.println("-------------------------------------------------");
            System.out.println("\nProfile Editing:");
            System.out.println("1. Change profile data");
            System.out.println("2. Delete account");
            System.out.println("3. Update password");
            System.out.println("4. Return to the menu");
            System.out.println("-------------------------------------------------");

            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleChangeProfile();
                    break;

                case "2":
                    return handleDeleteUser();

                case "3":
                    handleResetPassword();
                    break;

                case "4":
                    return currentUser;

                default:
                    System.out.println("Wrong command. Try again.");
            }

        }
    }

    /**
     * Отображает меню администратора, предоставляющее функции блокировки пользователя,
     * удаления пользователя, просмотра всех пользователей с их привычками или выхода из системы.
     *
     * @return null в случае выхода из аккаунта администратора.
     */
    private User showAdminMenu() {
        while (true) {
            System.out.println("-------------------------------------------------");
            System.out.println("\nSelect an action:");
            System.out.println("1. Block user");
            System.out.println("2. Delete user");
            System.out.println("3. Show users with habits");
            System.out.println("4. Log out");
            System.out.println("-------------------------------------------------");


            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                   handleBlockUser();

                    break;
                case "2":
                    handleDeleteUserByAdmin();

                    break;
                case "3":
                    handleShowUsersWithHabits();

                    break;
                case "4":
                    return null;
            }
        }
    }

    /**
     * Отображает меню управления привычками пользователя. Позволяет создавать, редактировать,
     * удалять привычки, а также отмечать их выполнение, просматривать историю и статистику,
     * а также генерировать отчеты.
     */
    private void showHabitManageMenu(){
        while (true) {
            System.out.println("-------------------------------------------------");
            System.out.println("\nHabit Management:");
            System.out.println("0. Return to the menu");
            System.out.println("1. Create a habit");
            System.out.println("2. Edit habit");
            System.out.println("3. Remove the habit");
            System.out.println("4. View all habits");
            System.out.println("5. Mark habit as completed");
            System.out.println("6. View the history of habit fulfillment");
            System.out.println("7. View statistics on habit fulfillment");
            System.out.println("8. Report generation");
            System.out.println("-------------------------------------------------");

            choice = scanner.nextLine();
            switch (choice) {
                case "0":
                    return;
                case "1":
                    handleCreateHabit();

                    break;
                case "2":
                    handleEditHabit();

                    break;
                case "3":
                    handleRemoveHabit();

                    break;
                case "4":
                    handleViewAllHabit();

                    break;
                case "5":
                    handleMarkHabit();

                    break;
                case "6":
                   handleViewHistory();

                    break;
                case "7":
                    handleViewStatistic();

                    break;
                case "8":
                    handleCreateReport();

                    break;
                default:
                    System.out.println("Wrong command. Try again.");
            }

        }
    }

    /**
     * Генерирует имя файла отчета на основе текущей даты.
     *
     * @return имя файла отчета в формате "report_yyyy-MM-dd.txt".
     */
    public static String generateFileName() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "report_" + now.format(formatter) + ".txt";
    }

    /**
     * Обрабатывает регистрацию нового пользователя.
     * Запрашивает у пользователя имя, email и пароль, после чего регистрирует его через userService.
     */
    public void handleRegistration(){

        System.out.print("Enter name: ");
        name = scanner.nextLine();

        System.out.print("Enter email: ");
        email = scanner.nextLine();

        System.out.print("Enter your password: ");
        password = scanner.nextLine();
        System.out.println(userService.registerUser(name, email, password));
    }

    /**
     * Обрабатывает авторизацию пользователя.
     * Запрашивает у пользователя email и пароль, затем проверяет корректность данных через userService.
     *
     * @return объект User, если авторизация успешна, или null в случае ошибки.
     */
    public User handleAutorisation(){
        System.out.print("Enter email: ");
        email = scanner.nextLine();

        System.out.print("Enter your password: ");
        password = scanner.nextLine();

        Optional<User> optionalUser = userService.loginUser(email, password);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println("\nAuthorization was successful. Welcome, " + user.getName() + "!");
            return user;
        } else {
            System.out.println("Error: Incorrect email or password."); // Сообщение об ошибке
            return null;
        }
    }

    /**
     * Обрабатывает изменение профиля пользователя.
     * Пользователь может изменить свое имя и email. В случае успешного обновления данных, профиль обновляется.
     */
    public void handleChangeProfile(){
        System.out.println("Enter a new name or skip it to keep it the same:");
        String newName = scanner.nextLine();
        if(newName.isEmpty()){
            newName = currentUser.getName();
        }
        System.out.println("Enter a new email or skip it to keep it the same:");
        String newEmail = scanner.nextLine();
        if(newName.isEmpty()){
            newEmail = currentUser.getEmail();
        }
        Optional<User> updatedUser = userService.updateUserProfile(currentUser, newName, newEmail);
        if (updatedUser.isPresent()){
            System.out.println("Your data has been successfully updated!");
            currentUser = updatedUser.get();
        } else {
            System.out.println("There is already a user with that email.");
        }
    }

    /**
     * Обрабатывает удаление аккаунта пользователя.
     * Запрашивает подтверждение удаления, затем удаляет аккаунт через userService.
     *
     * @return null, если аккаунт удален, или текущий объект пользователя в случае отказа.
     */
    public User handleDeleteUser(){
        System.out.print("Are you sure you want to delete your account? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            userService.deleteUser(currentUser);
            System.out.println("Account deleted. Return to the start menu.");
            return null;
        } else {
            return currentUser;
        }
    }

    /**
     * Обрабатывает сброс пароля пользователя.
     * Запрашивает текущий пароль, затем новый пароль. В случае успеха пароль обновляется через userService.
     */
    public void handleResetPassword(){
        System.out.println("Enter your current password:");
        String password = scanner.nextLine();
        if(currentUser.getPassword().equals(password))
        {
            System.out.println("Enter a new password:");
            String newPassword = scanner.nextLine();
            currentUser = userService.resetPassword(currentUser, newPassword);
            System.out.println("Password has been successfully updated.");
        } else {
            System.out.println("Wrong password.");
        }
    }

    /**
     * Обрабатывает блокировку пользователя администратором.
     * Отображает список всех пользователей и предоставляет возможность выбрать пользователя для блокировки.
     */
    public void handleBlockUser(){
        allUsers = userService.getAllUsers();

        if (allUsers.isEmpty()) {
            System.out.println("There are no registered users.");
            return;
        }

        System.out.println("Select a user to block:");
        for (int i = 0; i < allUsers.size(); i++) {
            System.out.println((i + 1) + ". " + allUsers.get(i).getName());
        }

        System.out.print("Enter the user number or type 0 to exit: ");
        while(true){
            try {
                choice = scanner.nextLine();
                intChoice = Integer.parseInt(choice);
                if (intChoice < 0 || intChoice > allUsers.size()) {
                    System.out.println("Wrong user number. Try again.");
                } else{
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }
        if(intChoice == 0) {
            return;
        } else {
            if(userService.updateUserProfile(allUsers.get(intChoice-1), true).isPresent()){
                System.out.println("The user has been successfully blocked.");
            } else {
                System.out.println("Error.");
            }
        }
    }

    /**
     * Обрабатывает удаление пользователя администратором.
     * Отображает список всех пользователей и предоставляет возможность выбрать пользователя для удаления.
     */
    public void handleDeleteUserByAdmin(){
        allUsers = userService.getAllUsers();

        if (allUsers.isEmpty()) {
            System.out.println("There are no registered users.");
            return;
        }

        System.out.println("Select a user to delete:");
        for (int i = 0; i < allUsers.size(); i++) {
            System.out.println((i + 1) + ". " + allUsers.get(i).getName());
        }

        System.out.print("Enter the user number or type 0 to exit: ");
        while(true){
            try {
                choice = scanner.nextLine();
                intChoice = Integer.parseInt(choice);
                if (intChoice < 0 || intChoice > allUsers.size()) {
                    System.out.println("Wrong user number. Try again.");
                } else{
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }
        if(intChoice == 0) {
            return;
        } else {
            userService.deleteUser(allUsers.get(intChoice-1));
            System.out.println("The user has been successfully deleted.");
        }
    }

    /**
     * Обрабатывает команду показа всех пользователей и их привычек. Выводит на консоль список пользователей
     * и их привычек, полученных с помощью сервисов пользователей и привычек.
     */
    public void handleShowUsersWithHabits(){
        allUsers = userService.getAllUsers();
        for(User user : allUsers){
            System.out.println("User: " + user.getName());
            allHabits = habitService.getAllHabits(user);
            int count = 1;
            for(Habit habit : allHabits){
                System.out.println("   " + count + ". " + habit.getName());
                count++;
            }
        }
    }

    /**
     * Обрабатывает команду создания новой привычки. Вводит название, описание и частоту выполнения
     * привычки с консоли и передает их в сервис для создания привычки.
     */
    public void handleCreateHabit(){
        System.out.print("Enter the name of the habit: ");
        String name = scanner.nextLine();

        System.out.print("Enter a description of the habit: ");
        String description = scanner.nextLine();

        System.out.println("Enter the frequency of execution: ");
        System.out.println("1. daily");
        System.out.println("2. weekly");

        while(true){
            try{
                choice = scanner.nextLine();
                intChoice = Integer.parseInt(choice);
                if (intChoice < 1 || intChoice > 2) {
                    System.out.println("Wrong number. Try again.");
                } else{
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }

        }


        if(intChoice == 1){
            frequency = Habit.Frequency.DAILY;
        } else {
            frequency = Habit.Frequency.WEEKLY;
        }

        String createResult = habitService.createHabit(currentUser, name, description, frequency);
        System.out.println(createResult);
    }

    /**
     * Обрабатывает команду редактирования привычки. Позволяет выбрать существующую привычку, изменить ее
     * название, описание и частоту выполнения.
     */
    public void handleEditHabit(){
        allHabits = habitService.getAllHabits(currentUser);

        if (allHabits.isEmpty()) {
            System.out.println("You don't have editing habits.");
            return;
        }

        System.out.println("Select a habit to edit:");
        for (int i = 0; i < allHabits.size(); i++) {
            System.out.println((i + 1) + ". " + allHabits.get(i).getName());
        }

        System.out.print("Enter the habit number or type 0 to exit: ");
        while(true){
            try {
                choice = scanner.nextLine();
                intChoice = Integer.parseInt(choice);
                if (intChoice < 0 || intChoice > allHabits.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else{
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }
        if(intChoice == 0){
            return;
        } else{
            Habit habitToUpdate = allHabits.get(intChoice - 1);
            System.out.print("Enter a new habit name (leave blank to keep the current one): ");
            String newNameHabit = scanner.nextLine();
            if(newNameHabit.isEmpty()){
                newNameHabit = habitToUpdate.getName();
            }

            System.out.print("Enter a new habit description (leave blank to keep the current one): ");
            String newDescription = scanner.nextLine();
            if(newDescription.isEmpty()){
                newDescription = habitToUpdate.getDescription();
            }

            System.out.print("Enter a new run frequency: ");
            System.out.println("1. daily");
            System.out.println("2. weekly");

            while(true){
                try{
                    choice = scanner.nextLine();
                    intChoice = Integer.parseInt(choice);
                    if (intChoice < 1 || intChoice > 2) {
                        System.out.println("Wrong number. Try again.");
                    } else{
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter right number.");
                }

            }

            if(intChoice == 1){
                frequency = Habit.Frequency.DAILY;
            } else {
                frequency = Habit.Frequency.WEEKLY;
            }

            if(habitService.updateHabit(currentUser, habitToUpdate, newNameHabit, newDescription, frequency)){
                System.out.println("Success.");
            } else {
                System.out.println("Error");
            }
        }
    }

    /**
     * Обрабатывает команду удаления привычки. Позволяет пользователю выбрать существующую привычку
     * и удалить её, вместе со всеми связанными с ней отметками выполнения.
     */
    public void handleRemoveHabit(){
        allHabits = habitService.getAllHabits(currentUser);

        if (allHabits.isEmpty()) {
            System.out.println("You don't have removal habits.");
            return;
        }

        System.out.println("Select a habit to delete:");
        for (int i = 0; i < allHabits.size(); i++) {
            System.out.println((i + 1) + ". " + allHabits.get(i).getName());
        }

        while(true){
            try {
                choice = scanner.nextLine();
                intChoice = Integer.parseInt(choice);
                if (intChoice < 0 || intChoice > allHabits.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
                return;
            }
        }

        if(intChoice == 0){
            return;
        } else{
            String habitName = allHabits.get(intChoice - 1).getName();
            if(!allHabits.get(intChoice - 1).getCompletions().isEmpty()){
                habitCompletionService.deleteAllHabitCompletion(allHabits.get(intChoice - 1));
            }
            if(habitService.deleteHabit(currentUser, habitName)){
                System.out.println("The habit has been successfully removed.");
            } else {
                System.out.println("Error");
            }
        }
    }

    /**
     * Отображает все привычки пользователя, с возможностью фильтрации по дате добавления
     * или по доступности для отметки на сегодня.
     */
    public void handleViewAllHabit(){
        allHabits = habitService.getAllHabits(currentUser);

        if (allHabits.isEmpty()) {
            System.out.println("You don't have habits.");
            return;
        }
        System.out.println("0. Exit");
        System.out.println("1. Show habits filtered by date added.");
        System.out.println("2. Show habits available to mark today.");

        while (true) {
            try {
                choice = scanner.nextLine();
                intChoice = Integer.parseInt(choice);
                if (intChoice < 0 || intChoice > 2) {
                    System.out.println("Wrong habit number. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }
        if (intChoice == 0) {
            return;
        } else if (intChoice == 1) {
            List<Habit> filteredByDate = habitService.getHabitsByDate(allHabits);
            System.out.println("A list of your habits:");
            for (int i = 0; i < filteredByDate.size(); i++) {
                System.out.println((i + 1) + ". " + filteredByDate.get(i).getName());
            }
        } else {
            List<Habit> availableHabits = habitService.getAvailableHabits(allHabits);
            System.out.println("A list of habits available for marking: ");
            if (availableHabits.isEmpty()) {
                System.out.println("You don't have any available habits for today.");
                return;
            }
            for (int i = 0; i < availableHabits.size(); i++) {
                System.out.println((i + 1) + ". " + availableHabits.get(i).getName());
            }
        }
    }

    /**
     * Обрабатывает отметку выполнения привычки. Позволяет пользователю выбрать привычку из списка
     * доступных для отметки на текущий день и пометить её как выполненную.
     */
    public void handleMarkHabit(){
        allHabits = habitService.getAllHabits(currentUser);
        List<Habit> availableHabits = habitService.getAvailableHabits(allHabits);

        if (availableHabits.isEmpty()) {
            System.out.println("You don't have habits to mark.");
            return;
        }

        System.out.println("List of habits available for marking, select the one you want:");
        for (int i = 0; i < availableHabits.size(); i++) {
            System.out.println((i + 1) + ". " + availableHabits.get(i).getName());
        }

        System.out.print("Enter the habit number or type 0 to exit: ");

        while(true){
            try {
                choice = scanner.nextLine();
                intChoice = Integer.parseInt(choice);
                if (intChoice < 0 || intChoice > availableHabits.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }

        if(intChoice == 0){
            return;
        } else{

            String result = habitCompletionService.createHabitCompletion(LocalDate.now(), availableHabits.get(intChoice-1));
            System.out.println(result);
        }
    }

    /**
     * Отображает историю выполнения всех привычек пользователя. Для каждой привычки выводится
     * список дат выполнения.
     */
    public void handleViewHistory(){
        allHabits = habitService.getAllHabits(currentUser);

        if (allHabits.isEmpty()) {
            System.out.println("You don't have habits.");
            return;
        }

        for(Habit habit : allHabits){
            List <HabitCompletion> completions = habitCompletionService.getAllHabitCompletions(habit);
            System.out.println("Habit: " + habit.getName() + ": ");
            if(completions.isEmpty()){
                System.out.println("   There are no fulfillment marks.");
            } else {
                for (int i = 0; i < completions.size(); i++) {
                    System.out.println("   " + (i + 1) + ". " + completions.get(i).getMarkDate());
                }
            }
        }
    }

    /**
     * Отображает статистику выполнения привычек пользователя. Выводит общие данные о привычках,
     * например, текущие серии выполнения или процент выполнения за выбранное время.
     */
    public void handleViewStatistic(){
        List<Habit> allHabits = habitService.getAllHabits(currentUser);
        if (allHabits.isEmpty()) {
            System.out.println("You have no habits.");
            return;
        } else {
            System.out.println("0. Exit");
            System.out.println("1. Counting the current streaks of habit fulfillment.");
            System.out.println("2. Calculate the percentage of successful completion of habits over a period of time.");
            while (true) {
                try {
                    intChoice = Integer.parseInt(scanner.nextLine());
                    if (intChoice < 0 || intChoice > 2) {
                        System.out.println("Wrong habit number. Try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter right number.");
                }
            }
            if (intChoice == 0) {
                return;
            } else if (intChoice == 1) {
                for (Habit habit : allHabits) {
                    System.out.println("Habit: " + habit.getName() + " current streak = " + habitService.countHabitStreak(habit));
                }
            } else {
                System.out.println("1. Completion percentage for today.");
                System.out.println("2. Completion percentage for week.");
                System.out.println("3. Completion percentage for month.");
                while (true) {
                    try {
                        intChoice = Integer.parseInt(scanner.nextLine());
                        if (intChoice < 0 || intChoice > 3) {
                            System.out.println("Wrong habit number. Try again.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Enter right number.");
                    }
                }
                if (intChoice == 1) {
                    for (Habit habit : allHabits) {
                        System.out.println("The percentage of successful execution of the habit \"" + habit.getName() + "\" for the day: " + habitService.countPercentage(LocalDate.now(), habit) + "%");
                    }
                } else if (intChoice == 2) {
                    for (Habit habit : allHabits) {
                        System.out.println("The percentage of successful execution of the habit \"" + habit.getName() + "\" for the week: " + habitService.countPercentage(LocalDate.now().minusWeeks(1), habit) + "%");
                    }
                } else {
                    for (Habit habit : allHabits) {
                        System.out.println("The percentage of successful execution of the habit \"" + habit.getName() + "\" for the month: " + habitService.countPercentage(LocalDate.now().minusMonths(1), habit) + "%");
                    }
                }
            }
        }
    }

/**
 * Создаёт отчёт для пользователя о выполнении привычек и сохраняет его в файл.
 * Отчёт включает информацию о всех привычках, таких как текущая серия, процент выполнения за всё время,
 * за день, неделю и месяц. Если директория для отчётов ещё не создана, она создаётся автоматически.
 */
 public void handleCreateReport(){
        String baseDirectory = "homework_1";
        String directoryPath = baseDirectory + File.separator + "reports";
        String filePath = directoryPath + File.separator + generateFileName();
        allHabits = habitService.getAllHabits(currentUser);

        File baseDir = new File(baseDirectory);
        if (!baseDir.exists()) {
            boolean createdBaseDir = baseDir.mkdir();
            if (!createdBaseDir) {
                System.out.println("Error: Could not create base directory.");
                return;
            }
        }
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean createdReportsDir = directory.mkdir();
            if (!createdReportsDir) {
                System.out.println("Error: Could not create reports directory.");
                return;
            }
        }

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        StringBuilder report = new StringBuilder();
        report.append("Hello, ").append(currentUser.getName()).append("!\n");
        report.append("This is your report on keeping your habits:\n");
        if (allHabits.isEmpty()) {
            report.append("You don't have habits.");
        } else {
            for(Habit habit : allHabits){
                report.append(habit.getFrequency() + " habbit ").append(habit.getName() + ":\n");
                report.append("   Current streak: ").append(habitService.countHabitStreak(habit) + "\n");
                report.append("   For all time fulfilled ").append(habitService.countPercentage(habit.getCreateDate(), habit) + "% \n");
                report.append("   Today's accomplished ").append(habitService.countPercentage(LocalDate.now(), habit) + "% \n");
                report.append("   In the last week, accomplished ").append(habitService.countPercentage(LocalDate.now().minusWeeks(1), habit) + "% \n");
                report.append("   In the last month, accomplished ").append(habitService.countPercentage(LocalDate.now().minusMonths(1), habit) + "% \n\n");
            }
        }
        report.append("Good luck!\n");

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(String.valueOf(report));
            System.out.println("Report saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

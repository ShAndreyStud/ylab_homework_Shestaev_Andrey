package com.habittracker;

import com.habittracker.model.HabitCompletion;
import com.habittracker.service.AdminService;
import com.habittracker.service.UserService;
import com.habittracker.service.HabitService;
import com.habittracker.model.User;
import com.habittracker.model.Habit;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        HabitService habitService = new HabitService();
        AdminService adminService = new AdminService(userService);
        User currentUser = null;

        System.out.println("Welcome!");

        while (true) {
            if (currentUser == null) {
                currentUser = handleUnauthenticatedUser(scanner, userService);
            } else {
                currentUser = handleAuthenticatedUser(scanner, userService, adminService, habitService, currentUser);
            }
        }

    }

    private static User handleUnauthenticatedUser(Scanner scanner, UserService userService) {
        System.out.println("-------------------------------------------------");
        System.out.println("\nSelect an action:");
        System.out.println("1. Registration");
        System.out.println("2. Authorisation");
        System.out.println("3. Exit");
        System.out.println("-------------------------------------------------");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                registerUser(scanner, userService);
                return null; // Остаемся неавторизованными
            case "2":
                return loginUser(scanner, userService);
            case "3":
                System.out.println("Goodbye!");
                System.exit(0); // Завершение программы
                return null;
            default:
                System.out.println("Wrong command. Try again.");
                return null;
        }
    }

    private static void registerUser(Scanner scanner, UserService userService) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        String registrationResult = userService.registerUser(name, email, password);
        System.out.println(registrationResult);
    }

    private static User loginUser(Scanner scanner, UserService userService) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        String loginResult = userService.loginUser(email, password);
        System.out.println(loginResult);

        if (loginResult.startsWith("Authorization was successful")) {
            return userService.getUserByEmail(email); // Возвращаем авторизованного пользователя
        }
        return null; // Остаемся неавторизованными
    }

    private static User handleAuthenticatedUser(Scanner scanner, UserService userService, AdminService adminService, HabitService habitService, User currentUser) {
        if(currentUser.isAdmin()){
            System.out.println("-------------------------------------------------");
            System.out.println("\nSelect an action:");
            System.out.println("1. Block user");
            System.out.println("2. Delete user");
            System.out.println("3. Show users with habits");
            System.out.println("4. Log out");
            System.out.println("-------------------------------------------------");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    blockUser(scanner, userService);
                    return currentUser;
                case "2":
                    deleteUser(scanner, userService);
                    return currentUser;
                case "3":
                    showUsers(adminService);
                    return currentUser;
                case "4":
                    System.out.println("You have successfully logged out of your account.");
                    return null; // Очищаем текущего пользователя
                default:
                    System.out.println("Wrong command. Try again.");
                    return currentUser;
            }
        } else if (!currentUser.isBlocked()){

            System.out.println("-------------------------------------------------");
            System.out.println("\nSelect an action:");
            System.out.println("1. Editing a profile");
            System.out.println("2. Habit management");
            System.out.println("3. Log out");
            System.out.println("-------------------------------------------------");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    return editProfile(scanner, userService, currentUser);
                case "2":
                    manageHabits(scanner, habitService, currentUser);
                    return currentUser;
                case "3":
                    System.out.println("You have successfully logged out of your account.");
                    return null; // Очищаем текущего пользователя
                default:
                    System.out.println("Wrong command. Try again.");
                    return currentUser;
            }
        } else {
            System.out.println("You're blocked!");
            return null;
        }

    }

    private static void blockUser(Scanner scanner, UserService userService){
        Map<String, User> users = userService.getAllUsers();
        List<User> usersList = users.values().stream().toList();
        if(usersList.isEmpty()){
            System.out.println("No active users.");
            return;
        }
        for (int i = 0; i < usersList.size(); i++) {
            System.out.println(i+1 + ". " + usersList.get(i).getName());
        }
        System.out.println("Select user to block, or type 0 to exit:");
        int choice;
        while(true){
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 0 || choice > usersList.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }
        if(choice == 0) {
            return;
        } else {
            usersList.get(choice-1).setBlocked(true);
            System.out.println("User has been blocked.");
        }
    }

    private static void showUsers(AdminService adminService){
        List<User> usersList = adminService.getAllUsersWithHabits();
        StringBuilder result = new StringBuilder();
        for (User user : usersList) {
            if(!user.isAdmin()) {
                result.append("User: ").append(user.getName()).append("\n");
                result.append("Habits: \n");
                user.getHabits().forEach(habit -> result.append(" - ").append(habit.getName()).append("\n"));
            }
        }
        System.out.println(result);
    }

    private static void deleteUser(Scanner scanner, UserService userService){
        Map<String, User> users = userService.getAllUsers();
        List<User> usersList = users.values().stream().toList();
        if(usersList.isEmpty()){
            System.out.println("No active users.");
            return;
        }
        for (int i = 0; i < usersList.size(); i++) {
            System.out.println(i+1 + ". " + usersList.get(i).getName());
        }
        System.out.println("Select user to delete, or type 0 to exit:");
        int choice;
        while(true){
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 0 || choice > usersList.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }
        if(choice == 0) {
            return;
        } else {
            userService.deleteUser(usersList.get(choice-1).getEmail());
            System.out.println("User has been deleted.");
        }
    }

    private static User editProfile(Scanner scanner, UserService userService, User currentUser) {
        while (true) {
            System.out.println("-------------------------------------------------");
            System.out.println("\nProfile Editing:");
            System.out.println("1. Change profile data");
            System.out.println("2. Delete account");
            System.out.println("3. Password reset");
            System.out.println("4. Return to the menu");
            System.out.println("-------------------------------------------------");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    updateProfile(scanner, userService, currentUser);
                    break;
                case "2":
                    return deleteAccount(scanner, userService, currentUser);
                case "3":
                    resetPassword(scanner, userService, currentUser);
                    break;
                case "4":
                    return currentUser; // Возврат в меню
                default:
                    System.out.println("Wrong command. Try again.");
            }
        }
    }

    private static void updateProfile(Scanner scanner, UserService userService, User currentUser) {
        System.out.print("Enter a new name: ");
        String newName = scanner.nextLine();

        System.out.print("Enter a new email: ");
        String newEmail = scanner.nextLine();


        String updateResult = userService.updateUserProfile(currentUser.getEmail(), newName, newEmail);

        if (updateResult.equals("The profile has been successfully updated.")) {
            currentUser = userService.getUserByEmail(newEmail);
        }

        System.out.println(updateResult);
    }

    private static User deleteAccount(Scanner scanner, UserService userService, User currentUser) {
        System.out.print("Are you sure you want to delete the account? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            String deleteResult = userService.deleteUser(currentUser.getEmail());
            System.out.println(deleteResult);

            if (deleteResult.equals("The user account has been successfully deleted.")) {
                System.out.println("You are returned to the authorization screen.");
                return null; // Возвращаем null, чтобы перейти в неавторизованное состояние
            }
        }
        return currentUser; // Если удаление не произошло, возвращаем текущего пользователя
    }

    private static void resetPassword(Scanner scanner, UserService userService, User currentUser) {
        String resetResult = userService.resetPassword(currentUser.getEmail());
        System.out.println(resetResult);
    }

    private static void manageHabits(Scanner scanner, HabitService habitService, User currentUser) {
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

            String choice = scanner.nextLine();

            switch (choice) {
                case "0":
                    return;
                case "1":
                    createHabit(scanner, habitService, currentUser);
                    break;
                case "2":
                    updateHabit(scanner, habitService, currentUser);
                    break;
                case "3":
                    deleteHabit(scanner, habitService, currentUser);
                    break;
                case "4":
                    listHabits(scanner, habitService, currentUser);
                    break;
                case "5":
                    markHabitAsCompleted(scanner, habitService, currentUser);
                    break;
                case "6":
                    viewCompletionHistory(habitService, currentUser);
                    break;
                case "7":
                    viewStatistics(scanner, habitService, currentUser);
                    break;
                case "8":
                    generateReport(currentUser, habitService);
                    break;
                default:
                    System.out.println("Wrong command. Try again.");
            }
        }
    }

    private static void markHabitAsCompleted(Scanner scanner, HabitService habitService, User currentUser) {
        List<Habit> availableHabits = habitService.getAvailableHabits(currentUser);

        if (availableHabits.isEmpty()) {
            System.out.println("You don't have habits to mark.");
            return; // Возвращаемся в меню
        }

        System.out.println("List of habits available for marking, select the one you want:");
        for (int i = 0; i < availableHabits.size(); i++) {
            System.out.println((i + 1) + ". " + availableHabits.get(i).getName());
        }

        System.out.print("Enter the habit number or type 0 to exit: ");
        int choice;
        while(true){
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 0 || choice > availableHabits.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }

        if(choice == 0){
            return;
        } else{
            HabitCompletion newCompletion = new HabitCompletion(availableHabits.get(choice - 1));
            availableHabits.get(choice - 1).getCompletions().add(newCompletion);
        }
    }

    private static void viewCompletionHistory(HabitService habitService, User currentUser) {
        List<Habit> allHabits = currentUser.getHabits();
        if (allHabits.isEmpty()) {
            System.out.println("You don't have habits.");
            return;
        }

        for(Habit habit : allHabits){
            List <HabitCompletion> completion = habit.getCompletions();
            System.out.println("Habit: " + habit.getName() + ": ");
            if(completion.isEmpty()){
                System.out.println("   There are no fulfillment marks.");
            } else {
                for (int i = 0; i < completion.size(); i++) {
                    System.out.println("   " + (i + 1) + ". " + completion.get(i).getMarkDate());
                }
            }
        }
    }

    private static void createHabit(Scanner scanner, HabitService habitService, User currentUser) {
        System.out.print("Enter the name of the habit: ");
        String name = scanner.nextLine();

        System.out.print("Enter a description of the habit: ");
        String description = scanner.nextLine();

        System.out.println("Enter the frequency of execution: ");
        System.out.println("1. daily");
        System.out.println("2. weekly");
        int choice;
        while(true){
            try{
                choice = scanner.nextInt();
                if (choice < 1 || choice > 2) {
                    System.out.println("Wrong number. Try again.");
                } else{
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }

        }
        String frequency;
        if(choice == 1){
            frequency = "daily";
        } else {
            frequency = "weekly";
        }


        String createResult = habitService.createHabit(currentUser, name, description, frequency);
        System.out.println(createResult);
    }

    private static void updateHabit(Scanner scanner, HabitService habitService, User currentUser) {
        List<Habit> allHabits = habitService.getAllHabits(currentUser);

        if (allHabits.isEmpty()) {
            System.out.println("You don't have editing habits.");
            return; // Возвращаемся в меню
        }

        System.out.println("Select a habit to edit:");
        for (int i = 0; i < allHabits.size(); i++) {
            System.out.println((i + 1) + ". " + allHabits.get(i).getName());
        }

        System.out.print("Enter the habit number or type 0 to exit: ");
        int choice;
        while(true){
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 0 || choice > allHabits.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else{
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }
        if(choice == 0){
            return;
        } else{
            Habit habitToUpdate = allHabits.get(choice - 1); // Получаем привычку по номеру

            System.out.print("Enter a new habit name (leave blank to keep the current one): ");
            String newName = scanner.nextLine();
            if (newName.isEmpty()) {
                newName = habitToUpdate.getName(); // Сохраняем текущее имя, если новое не введено
            }

            System.out.print("Enter a new habit description (leave blank to keep the current one): ");
            String newDescription = scanner.nextLine();
            if (newDescription.isEmpty()) {
                newDescription = habitToUpdate.getDescription(); // Сохраняем текущее описание
            }

            System.out.print("Enter a new run frequency (leave blank to save the current one): ");
            String newFrequency = scanner.nextLine();
            if (newFrequency.isEmpty()) {
                newFrequency = habitToUpdate.getFrequency(); // Сохраняем текущую частоту
            }

            String updateResult = habitService.updateHabit(currentUser, habitToUpdate.getName(), newName, newDescription, newFrequency);
            System.out.println(updateResult);
        }
    }

    private static void deleteHabit(Scanner scanner, HabitService habitService, User currentUser) {
        List<Habit> allHabits = habitService.getAllHabits(currentUser);

        if (allHabits.isEmpty()) {
            System.out.println("You don't have removal habits.");
            return; // Возвращаемся в меню
        }

        System.out.println("Select a habit to delete:");
        for (int i = 0; i < allHabits.size(); i++) {
            System.out.println((i + 1) + ". " + allHabits.get(i).getName());
        }

        System.out.print("Enter the habit number or type 0 to exit: ");
        int choice;
        while(true){
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 0 || choice > allHabits.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else {
                  break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
                return; // Возвращаемся в меню
            }
        }
        if(choice == 0){
            return;
        } else{
            String habitName = allHabits.get(choice - 1).getName(); // Получаем имя привычки по номеру
            String deleteResult = habitService.deleteHabit(currentUser, habitName);
            System.out.println(deleteResult);
        }
    }

    private static void listHabits(Scanner scanner, HabitService habitService, User currentUser) {
        List<Habit> allHabits = habitService.getAllHabits(currentUser);
        if (allHabits.isEmpty()) {
            System.out.println("You have no habits.");
        } else {
            System.out.println("0. Exit");
            System.out.println("1. Show habits filtered by date added.");
            System.out.println("2. Show habits available to mark today.");
            int choice;
            while (true){
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 0 || choice > 2) {
                        System.out.println("Wrong habit number. Try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter right number.");
                }
            }
            if(choice == 0){
                return;
            }else if(choice == 1) {
                System.out.println("A list of your habits:");
                for (int i = 0; i < allHabits.size(); i++) {
                    System.out.println((i + 1) + ". " + allHabits.get(i).getName());
                }
            } else{
                System.out.println("A list of habits available for marking: ");
                List<Habit> availableHabits = habitService.getAvailableHabits(currentUser);
                if(availableHabits.isEmpty()){
                    System.out.println("You don't have any available habits for today.");
                    return;
                }
                for (int i = 0; i < availableHabits.size(); i++) {
                    System.out.println((i + 1) + ". " + availableHabits.get(i).getName());
                }
            }

        }
    }

    private static void viewStatistics(Scanner scanner, HabitService habitService, User currentUser) {
        List<Habit> allHabits = habitService.getAllHabits(currentUser);
        if (allHabits.isEmpty()) {
            System.out.println("You have no habits.");
            return;
        } else {
            System.out.println("0. Exit");
            System.out.println("1. Counting the current streaks of habit fulfillment.");
            System.out.println("2. Calculate the percentage of successful completion of habits over a period of time.");
            int choice;
            while (true) {
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 0 || choice > 2) {
                        System.out.println("Wrong habit number. Try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter right number.");
                }
            }
            if (choice == 0) {
                return;
            } else if (choice == 1) {
                for(Habit habit : allHabits){
                    System.out.println("Habit: " + habit.getName() + " current streak = " + habitService.countStreak(habit));
                }
            } else {
                System.out.println("1. Completion percentage for today.");
                System.out.println("2. Completion percentage for week.");
                System.out.println("3. Completion percentage for month.");
                while (true) {
                    try {
                        choice = Integer.parseInt(scanner.nextLine());
                        if (choice < 0 || choice > 3) {
                            System.out.println("Wrong habit number. Try again.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Enter right number.");
                    }
                }
                if (choice == 1) {
                    for(Habit habit : allHabits){
                        System.out.println("The percentage of successful execution of the habit \"" + habit.getName() + "\" for the day: " + habitService.countPercentage(LocalDate.now(), habit) + "%");
                    }
                } else if(choice == 2) {
                    for(Habit habit : allHabits){
                        System.out.println("The percentage of successful execution of the habit \"" + habit.getName() + "\" for the week: " + habitService.countPercentage(LocalDate.now().minusWeeks(1), habit) + "%");
                    }
                } else {
                    for(Habit habit : allHabits){
                        System.out.println("The percentage of successful execution of the habit \"" + habit.getName() + "\" for the month: " + habitService.countPercentage(LocalDate.now().minusMonths(1), habit) + "%");
                    }
                }


            }
        }
    }

    public static void generateReport(User currentUser, HabitService habitService){
        String filePath = "reports" + File.separator + generateFileName();
        List<Habit> allHabits = habitService.getAllHabits(currentUser);

        File directory = new File("reports");
        if (!directory.exists()) {
            directory.mkdir();
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
                report.append("   Current streak: ").append(habitService.countStreak(habit) + "\n");
                report.append("   For all time fulfilled ").append(habitService.countPercentage(habit.getCreateDate(), habit) + "% \n");
                report.append("   Today's accomplished ").append(habitService.countPercentage(habit.getCreateDate(), habit) + "% \n");
                report.append("   In the last week, accomplished ").append(habitService.countPercentage(habit.getCreateDate(), habit) + "% \n");
                report.append("   In the last month, accomplished ").append(habitService.countPercentage(habit.getCreateDate(), habit) + "% \n\n");
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

    public static String generateFileName() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "report_" + now.format(formatter) + ".txt";
    }
}

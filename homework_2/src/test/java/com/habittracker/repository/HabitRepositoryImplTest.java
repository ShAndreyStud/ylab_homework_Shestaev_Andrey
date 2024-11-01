package com.habittracker.repository;

import com.habittracker.config.DatabaseConfig;
import com.habittracker.model.Habit;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.habittracker.model.User;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DisplayName("Тестирование репозитория привычек")
public class HabitRepositoryImplTest {
    private Connection connection;
    private UserRepositoryImpl userRepository;
    private HabitRepositoryImpl habitRepository;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("habittracker")
            .withUsername("habitadmin")
            .withPassword("habit123");

    @BeforeAll
    static void setup() {
        postgresContainer.start();
        runLiquibaseMigrations();
    }

    private static void runLiquibaseMigrations() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase("db/db.changelog-master.yml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException("Failed to run Liquibase migrations", e);
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        DatabaseConfig config = new DatabaseConfig("homework_2/src/main/resources/application.properties");
        userRepository = new UserRepositoryImpl(config);
        habitRepository = new HabitRepositoryImpl(config);
        connection.setAutoCommit(false);
        clearDatabase(); // Clear database before each test
    }

    private void clearDatabase() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE app_schema.habit CASCADE;"); // Adjust if you have foreign key constraints
            stmt.execute("TRUNCATE TABLE app_schema.user CASCADE;"); // Adjust if you have foreign key constraints
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @AfterAll
    public static void tearDownClass() {
        postgresContainer.stop();
    }

    @Test
    @DisplayName("Проверка успешного добавления привычки")
    public void testAddHabit() {
        User user = new User("John Doe", "john@example.com", "password", User.Role.USER);
        userRepository.addUser(user);

        Habit newHabit = new Habit("Exercise", "Daily workout", Habit.Frequency.DAILY, user, LocalDate.now());
        boolean result = habitRepository.addHabit(user, newHabit);
        assertTrue(result);

        Habit retrievedHabit = habitRepository.getHabit(user, "Exercise");
        assertNotNull(retrievedHabit);
        assertEquals("Exercise", retrievedHabit.getName());
        habitRepository.deleteHabit(user, "Exercise"); // Clean up
    }

    @Test
    @DisplayName("Проверка успешного обновления данных привычки")
    public void testUpdateHabit() {
        User user = new User("John Doe", "john@example.com", "password", User.Role.USER);
        userRepository.addUser(user);

        Habit newHabit = new Habit("Exercise", "Daily workout", Habit.Frequency.DAILY, user, LocalDate.now());
        habitRepository.addHabit(user, newHabit);

        boolean updated = habitRepository.updateHabit(user, newHabit, "Yoga", "Daily yoga session", Habit.Frequency.DAILY);
        assertTrue(updated);

        Habit updatedHabit = habitRepository.getHabit(user, "Yoga");
        assertNotNull(updatedHabit);
        assertEquals("Yoga", updatedHabit.getName());
        habitRepository.deleteHabit(user, "Yoga"); // Clean up
    }

    @Test
    @DisplayName("Проверка успешного удаления привычки")
    public void testDeleteHabit() {
        User user = new User("John Doe", "john@example.com", "password", User.Role.USER);
        userRepository.addUser(user);

        Habit newHabit = new Habit("Exercise", "Daily workout", Habit.Frequency.DAILY, user, LocalDate.now());
        habitRepository.addHabit(user, newHabit);

        boolean deleted = habitRepository.deleteHabit(user, "Exercise");
        assertTrue(deleted);

        Habit retrievedHabit = habitRepository.getHabit(user, "Exercise");
        assertNull(retrievedHabit);
    }

    @Test
    @DisplayName("Проверка успешного получения всех привычек")
    public void testGetAllHabits() {
        User user = new User("John Doe", "john@example.com", "password", User.Role.USER);
        userRepository.addUser(user);

        Habit habit1 = new Habit("Exercise", "Daily workout", Habit.Frequency.DAILY, user, LocalDate.now());
        Habit habit2 = new Habit("Reading", "Read a book", Habit.Frequency.WEEKLY, user, LocalDate.now());

        habitRepository.addHabit(user, habit1);
        habitRepository.addHabit(user, habit2);

        List<Habit> habits = habitRepository.getAllHabits(user);
        assertEquals(2, habits.size());
        assertTrue(habits.stream().anyMatch(h -> h.getName().equals("Exercise")));
        assertTrue(habits.stream().anyMatch(h -> h.getName().equals("Reading")));
    }
}
package com.habittracker.repository;

import com.habittracker.config.DatabaseConfig;
import com.habittracker.model.Habit;
import com.habittracker.model.HabitCompletion;
import com.habittracker.model.User;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DisplayName("Тестирование репозитория выполнения привычек")
public class HabitCompletionRepositoryImplTest {
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    private static UserRepositoryImpl userRepository;
    private static HabitRepositoryImpl habitRepository;
    private Connection connection;
    private HabitCompletionRepositoryImpl habitCompletionRepository;
    private static User user = new User("John Doe", "john@example.com", "password", User.Role.USER);
    private static Habit testHabit = new Habit("Exercise", "Daily workout", Habit.Frequency.DAILY, user, 1);

    @BeforeAll
    public static void setUp() throws SQLException {
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
    public void resetDatabase() throws SQLException {
        DatabaseConfig config = new DatabaseConfig("homework_2/src/main/resources/application.properties");
        habitCompletionRepository = new HabitCompletionRepositoryImpl(config);
        userRepository = new UserRepositoryImpl(config);
        habitRepository = new HabitRepositoryImpl(config);
    }


    @AfterAll
    public static void tearDown() {
        postgresContainer.stop();
    }

    @Test
    @DisplayName("Проверка успешного добавления выполнения привычки")
    public void testAddHabitCompletion() {
        userRepository.addUser(user);
        habitRepository.addHabit(user, testHabit);
        HabitCompletion completion = new HabitCompletion(LocalDate.now(), testHabit, 1);
        boolean result = habitCompletionRepository.addHabitCompletion(testHabit, completion);
        assertTrue(result);

        HabitCompletion retrieved = habitCompletionRepository.getHabitCompletion(testHabit, 1);
        assertNotNull(retrieved);
        assertEquals(completion.getMarkDate(), retrieved.getMarkDate());
        assertEquals(completion.getSerialNumber(), retrieved.getSerialNumber());
        habitCompletionRepository.deleteHabitCompletion(testHabit, completion.getSerialNumber());
    }

    @Test
    @DisplayName("Проверка успешного удаления выполнения привычки")
    public void testDeleteHabitCompletion() {
        HabitCompletion completion = new HabitCompletion(LocalDate.now(), testHabit, 1);
        habitCompletionRepository.addHabitCompletion(testHabit, completion);

        boolean result = habitCompletionRepository.deleteHabitCompletion(testHabit, 1);
        assertTrue(result);

        HabitCompletion retrieved = habitCompletionRepository.getHabitCompletion(testHabit, 1);
        assertNull(retrieved);
        habitCompletionRepository.deleteHabitCompletion(testHabit, completion.getSerialNumber());
    }

    @Test
    @DisplayName("Проверка успешного получения списка выполнения привычки")
    public void testGetAllHabitCompletions() {
        HabitCompletion completion1 = new HabitCompletion(LocalDate.now(), testHabit, 1);
        HabitCompletion completion2 = new HabitCompletion(LocalDate.now().plusDays(1), testHabit, 2);
        habitCompletionRepository.addHabitCompletion(testHabit, completion1);
        habitCompletionRepository.addHabitCompletion(testHabit, completion2);

        List<HabitCompletion> completions = habitCompletionRepository.getAllHabitCompletion(testHabit);
        assertEquals(2, completions.size());
        habitCompletionRepository.deleteHabitCompletion(testHabit, completion1.getSerialNumber());
        habitCompletionRepository.deleteHabitCompletion(testHabit, completion2.getSerialNumber());
    }

}
package com.habittracker.repository;

import com.habittracker.config.DatabaseConfig;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DisplayName("Тестирование репозитория пользователя")
public class UserRepositoryImplTest {
    private Connection connection;
    private UserRepositoryImpl userRepository;

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
        connection.setAutoCommit(false);
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
    @DisplayName("Проверка успешного добавления пользователя")
    public void testAddUser() {
        User user = new User("John Doe", "john@example.com", "password", User.Role.USER);
        boolean result = userRepository.addUser(user);
        assertTrue(result);

        User retrievedUser = userRepository.getUser("john@example.com");
        assertNotNull(retrievedUser);
        assertEquals("John Doe", retrievedUser.getName());
        userRepository.deleteUser(user);
    }

    @Test
    @DisplayName("Проверка успешного обновления данных пользователя")
    public void testUpdateUser() {
        User user = new User("John Doe", "john@example.com", "password", User.Role.USER);
        userRepository.addUser(user);

        boolean updated = userRepository.updateUser(user, "John Updated", "johnupdated@example.com");
        assertTrue(updated);

        User updatedUser = userRepository.getUser("johnupdated@example.com");
        assertNotNull(updatedUser);
        assertEquals("John Updated", updatedUser.getName());
        userRepository.deleteUser(user);
    }

    @Test
    @DisplayName("Проверка успешного удаления пользователя")
    public void testDeleteUser() {
        User user = new User("John Doe", "john@example.com", "password", User.Role.USER);
        userRepository.addUser(user);

        boolean deleted = userRepository.deleteUser(user);
        assertTrue(deleted);

        User retrievedUser = userRepository.getUser("john@example.com");
        assertNull(retrievedUser);
    }

    @Test
    @DisplayName("Проверка успешной блокировки пользователя")
    public void testBlockUser() {
        User user = new User("John Doe", "john@example.com", "password", User.Role.USER);
        userRepository.addUser(user);

        boolean blocked = userRepository.blockUser(user, true);
        assertTrue(blocked);

        User blockedUser = userRepository.getUser("john@example.com");
        assertTrue(blockedUser.isBlocked());
        userRepository.deleteUser(user);
    }


}
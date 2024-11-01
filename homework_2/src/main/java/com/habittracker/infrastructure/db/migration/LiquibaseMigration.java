package com.habittracker.infrastructure.db.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.database.jvm.JdbcConnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Класс, отвечающий за запуск миграций базы данных с использованием Liquibase.
 */
public class LiquibaseMigration {

    /** Соединение с базой данных. */
    private final Connection connection;

    /** Путь к файлу changelog, содержащему миграции Liquibase. */
    private final String changelogPath;

    /**
     * Конструктор класса LiquibaseMigration. Загружает путь к файлу changelog
     * из конфигурационного файла и устанавливает соединение для проведения миграций.
     *
     * @param connection соединение с базой данных
     * @throws IOException если конфигурационный файл не найден или не удается его загрузить
     * @throws IllegalArgumentException если путь к файлу changelog не задан в конфигурации
     */
    public LiquibaseMigration(Connection connection) throws IOException{
        this.connection = connection;

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Unable to find application.properties");
            }
            properties.load(input);
        }

        this.changelogPath = properties.getProperty("liquibase.changelog.path");
        if (this.changelogPath == null) {
            throw new IllegalArgumentException("Changelog path not configured in application.properties");
        }
    }

    /**
     * Запускает миграции базы данных, используя путь к changelog из конфигурации.
     *
     * @throws Exception если возникает ошибка при выполнении миграций
     */
    public void runMigrations() throws Exception {
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                new JdbcConnection(connection)
        );

        Liquibase liquibase = new Liquibase(
                changelogPath,
                new ClassLoaderResourceAccessor(),
                database
        );

        liquibase.update("");
    }
}
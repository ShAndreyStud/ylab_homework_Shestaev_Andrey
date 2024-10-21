package com.habittracker.infrastructure.db.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.database.jvm.JdbcConnection;

import java.sql.Connection;

public class LiquibaseMigration {
    private final Connection connection;

    public LiquibaseMigration(Connection connection) {
        this.connection = connection;
    }

    public void runMigrations() throws Exception {
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                new JdbcConnection(connection)
        );

        Liquibase liquibase = new Liquibase(
                "db/db.changelog-master.yml",
                new ClassLoaderResourceAccessor(),
                database
        );

        liquibase.update("");
    }
}
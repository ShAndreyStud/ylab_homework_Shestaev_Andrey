package com.habittracker.repository;

public class UserQueries {
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM app_schema.user WHERE email = ?";
    public static final String INSERT_USER = "INSERT INTO app_schema.user (name, email, password, role, is_blocked) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_USER = "UPDATE app_schema.user SET name = ?, email = ? WHERE email = ?";
    public static final String UPDATE_BLOCK_USER = "UPDATE app_schema.user SET is_blocked = ? WHERE email = ?";
    public static final String DELETE_USER = "DELETE FROM app_schema.user WHERE email = ?";
    public static final String UPDATE_USER_PASSWORD = "UPDATE app_schema.user SET password = ? WHERE email = ?";
    public static final String SELECT_ALL_USERS = "SELECT * FROM app_schema.user WHERE role <> 'ADMIN'";
    public static final String SELECT_USER_ID_BY_EMAIL = "SELECT id FROM app_schema.user WHERE email = ?";

}

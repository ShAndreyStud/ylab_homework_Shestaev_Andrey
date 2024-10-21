package com.habittracker.repository;


import com.habittracker.model.User;

import java.util.List;


public interface UserRepository {
    User getUser(String email);
    boolean addUser(User user);
    boolean updateUser(User user, String newName, String newEmail);
    boolean deleteUser(User user);
    User updateUserPassword(User user, String newPassword);
    public List<User> getAllUsers();
    public boolean blockUser(User user, Boolean block);
    public Integer getUserIdByEmail(String email);
}

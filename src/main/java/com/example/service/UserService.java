package com.example.service;

import com.example.dao.UserDAO;
import com.example.model.User;
import java.sql.SQLException;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User getUserById(Long userId) throws SQLException {
        return userDAO.getUserById(userId);
    }

    public User updateUser(User user) throws SQLException {
        boolean updated = userDAO.updateUser(user);
        if (updated) {
            return userDAO.getUserById(user.getId());
        }
        return null;
    }
}
package com.example.service;

import com.example.dao.UserDAO;
import com.example.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User getUserById(Long userId) throws SQLException {
        return userDAO.getUserById(userId);
    }

    public User createUser(User user) throws SQLException {
        user.setPassword(hashPassword(user.getPassword()));
        return userDAO.createUser(user);
    }

    public User updateUser(User user) throws SQLException {
        User existingUser = userDAO.getUserById(user.getId());
        if (existingUser == null) {
            return null;
        }

        // Only update the password if it has changed
        if (!user.getPassword().equals(existingUser.getPassword())) {
            user.setPassword(hashPassword(user.getPassword()));
        }

        boolean updated = userDAO.updateUser(user);
        if (updated) {
            return userDAO.getUserById(user.getId());
        }
        return null;
    }

    public boolean checkPassword(Long userId, String password) throws SQLException {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
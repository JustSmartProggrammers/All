package com.example.service;

import com.example.dao.UserDAO;
import com.example.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean signup(String email, String password, String name, String phoneNumber) throws Exception {
        if (userDAO.isEmailExists(email)) {
            throw new Exception("Email already exists");
        }

        if (!isValidEmail(email)) {
            throw new Exception("Invalid email format");
        }

        if (!isValidPassword(password)) {
            throw new Exception("Invalid password. Password must be at least 8 characters long and contain at least one number and one letter.");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        return userDAO.insertUser(email, hashedPassword, name, phoneNumber, false);
    }

    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean isEmailAvailable(String email) {
        return !userDAO.isEmailExists(email);
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public boolean checkSession(User user) {
        return user != null;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*");
    }
}
package com.airtribe.payflow.service;

import com.airtribe.payflow.entity.User;
import com.airtribe.payflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        if (user.getBalance() == null) {
            user.setBalance(0.0);
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUpiId(String upiId) {
        return userRepository.findByUpiId(upiId);
    }

    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersWithBalanceAbove(Double amount) {
        return userRepository.findUsersWithBalanceAbove(amount);
    }

    public User updateUserBalance(Long userId, Double newBalance) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setBalance(newBalance);
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}

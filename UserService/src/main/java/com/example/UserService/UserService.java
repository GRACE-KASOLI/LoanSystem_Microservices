package com.example.UserService;

import com.example.UserService.Config.LoginQueueProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginQueueProducer loginQueueProducer;

    public User register(User user) {
        // In a real application, hash the password here
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            LoginRequest loginRequest = new LoginRequest(email, user.getRole().toString());
            loginQueueProducer.sendLoginRequest(loginRequest, user.getRole().toString());
            return user;
        }
        return null;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // ✅ Add this method to retrieve all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword()); // Consider hashing passwords
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        }).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

package com.example.demo.services;


import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void saveTarget(User user) {
        user.setRoles(Arrays.asList(roleRepository.findByRole("target")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
    }
    public void saveHunter(User user) {
        user.setRoles(Arrays.asList(roleRepository.findByRole("hunter")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
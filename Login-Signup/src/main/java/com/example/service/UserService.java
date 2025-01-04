package com.example.service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.google.api.client.json.JsonFactory;

import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }


    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }


    public void sendPasswordResetEmail(String email) {
        
        System.out.println("Password reset email sent to " + email);
    }
    public Optional<User> findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }
    

}

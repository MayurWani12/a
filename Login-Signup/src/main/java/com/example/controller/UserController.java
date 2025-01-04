



package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;

import jakarta.validation.Valid;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    private static final String CLIENT_ID = "window is not defined\r\n"
    		+ "    at eval (C:\\Users\\Sai\\Desktop\\ANG\\Fijghjgfnal\\practice\\Skills-India\\skills-india\\.angular\\cache\\18.2.12\\skills-india\\vite\\deps_ssr\\gapi-script.js:7:12)\r\n"
    		+ "    at async instantiatejModule (file:///C:/Users/Sai/hDkesktop/ANG/Final/practcaice/Skills-India/skills-india/node_modules/vite/dist/node/chunks/dep-DyBnyoVI.js:52914:5\r\n"
    		+ "Click outside, press Esc key, or fix the code to dismiss.";


    
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Validated @RequestBody User user, BindingResult result) {
   
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        
        if (user.getEmail() == null || !user.getEmail().endsWith("@gmail.com")) {
            return ResponseEntity.badRequest().body("Only Gmail email addresses are allowed");
        }

      
        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

      
        user.setRole("USER");
        userService.saveUser(user);
        return ResponseEntity.ok("Signup successful");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
          
            return ResponseEntity.ok(existingUser.get());
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }





    @PostMapping("/google-signup")
    public ResponseEntity<?> googleSignup(@RequestBody Map<String, String> requestBody) {
        try {
            String token = requestBody.get("token");
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();


            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    jsonFactory
            )
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                Optional<User> existingUser = userService.findByEmail(email);
                if (existingUser.isEmpty()) {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setRole("USER");
                    userService.saveUser(newUser);
                    return ResponseEntity.ok("Google signup successful");
                }
                return ResponseEntity.ok("User already exists");
            } else {
                return ResponseEntity.badRequest().body("Invalid Google Token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during Google signup");
        }
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println("Received email: " + email);

        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("User found: " + user);

            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setTokenExpiration(LocalDateTime.now().plusHours(1));
            userService.saveUser(user);

            String resetLink = "http://localhost:8083/reset-password?token=" + resetToken;
            System.out.println("Generated Reset Link: " + resetLink);

         
            try {
                System.out.println("Simulating email sending...");
                return ResponseEntity.ok("Reset link sent to your email.");
            } catch (Exception e) {
                System.err.println("Error sending email: " + e.getMessage());
                return ResponseEntity.status(500).body("Failed to send reset link.");
            }
        } else {
            System.out.println("No user found with email: " + email);
            return ResponseEntity.badRequest().body("User with the given email does not exist.");
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("password");

       
        Optional<User> userOptional = userService.findByResetToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

          
            if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("Token has expired.");
            }

        
            user.setPassword(newPassword); 
            user.setResetToken(null);        
            user.setTokenExpiration(null);  

          
            userService.saveUser(user);

            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }


}

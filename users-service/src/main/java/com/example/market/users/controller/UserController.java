package com.example.market.users.controller;

import com.example.market.users.model.User;
import com.example.market.users.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getBalance() == null) {
            user.setBalance(BigDecimal.ZERO);
        }
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<Void> updateBalance(@PathVariable Long id, @RequestParam BigDecimal amount) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        BigDecimal newBalance = user.getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().build();
        }
        
        user.setBalance(newBalance);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}

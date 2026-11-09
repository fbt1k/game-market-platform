package com.example.market.notifications.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody String message) {
        System.out.println("\u001B[33m[УВЕДОМЛЕНИЕ СО СДЕЛКИ]: " + message + "\u001B[0m");
        return ResponseEntity.ok().build();
    }
}

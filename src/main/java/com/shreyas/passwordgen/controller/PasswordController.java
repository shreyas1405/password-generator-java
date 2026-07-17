package com.shreyas.passwordgen.controller;

import com.shreyas.passwordgen.model.AnalyzeRequest;
import com.shreyas.passwordgen.model.GenerateRequest;
import com.shreyas.passwordgen.model.PasswordResponse;
import com.shreyas.passwordgen.service.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@CrossOrigin
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generatePassword(@RequestBody GenerateRequest request) {
        try {
            PasswordResponse response = passwordService.generatePassword(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzePassword(@RequestBody AnalyzeRequest request) {
        try {
            PasswordResponse response = passwordService.analyzePassword(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

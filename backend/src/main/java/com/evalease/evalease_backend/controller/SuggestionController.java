package com.evalease.evalease_backend.controller;

import com.evalease.evalease_backend.dto.TrainerSuggestionDTO;
import com.evalease.evalease_backend.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forms/suggestions")
@CrossOrigin(origins = "${FRONTEND_URL}") // dynamic CORS from application.properties
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @GetMapping("/{formId}")
    public ResponseEntity<?> getSuggestions(@PathVariable Long formId) {
        try {
            List<TrainerSuggestionDTO> suggestions = suggestionService.getSuggestions(formId);
            return ResponseEntity.ok(suggestions);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to fetch suggestions: " + ex.getMessage()));
        }
    }
}



package com.evalease.evalease_backend.controller;

import com.evalease.evalease_backend.dto.FormDTO;
import com.evalease.evalease_backend.dto.RecentFormDTO;
import com.evalease.evalease_backend.entity.Form;
import com.evalease.evalease_backend.service.FormService;
import com.evalease.evalease_backend.repository.FormRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forms")
@CrossOrigin(origins = "${FRONTEND_URL}") // dynamic CORS from properties
public class FormController {

    @Autowired
    private FormService formService;

    @Autowired
    private FormRepository formRepository;

    // Create a new form
    @PostMapping
    public ResponseEntity<?> createForm(@RequestBody FormDTO formDTO) {
        try {
            Form savedForm = formService.saveForm(formDTO);
            return ResponseEntity.ok(savedForm);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create form: " + ex.getMessage()));
        }
    }

    // Get all forms
    @GetMapping
    public ResponseEntity<List<Form>> getAllForms() {
        List<Form> forms = formService.getAllForms();
        return ResponseEntity.ok(forms);
    }

    // Get form by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getFormById(@PathVariable Long id) {
        try {
            FormDTO formDTO = formService.getFormDTOById(id);
            return ResponseEntity.ok(formDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // Get recent forms
    @GetMapping("/recent")
    public ResponseEntity<List<RecentFormDTO>> getRecentForms() {
        List<RecentFormDTO> recentForms = formService.getRecentForms();
        return ResponseEntity.ok(recentForms);
    }
}


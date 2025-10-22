package com.evalease.evalease_backend.controller;

import com.evalease.evalease_backend.dto.SubmitResponseDTO;
import com.evalease.evalease_backend.entity.*;
import com.evalease.evalease_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/responses")
@CrossOrigin(origins = "${FRONTEND_URL}") // dynamic CORS
public class ResponseController {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SubmittedFormRepository submittedFormRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @PostMapping
    public ResponseEntity<?> submitResponses(@RequestBody SubmitResponseDTO payload) {
        try {
            Long formId = payload.getFormId();
            Long employeeId = payload.getEmployeeId();
            Map<String, Object> responsesMap = payload.getResponses();

            // Fetch Form & Employee
            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new RuntimeException("Form not found with id: " + formId));
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

            // Save SubmittedForm
            SubmittedForm submittedForm = SubmittedForm.builder()
                    .form(form)
                    .employee(employee)
                    .submittedAt(LocalDateTime.now())
                    .build();
            SubmittedForm savedSubmittedForm = submittedFormRepository.save(submittedForm);

            // Process Responses
            List<Response> responses = new ArrayList<>();
            for (Map.Entry<String, Object> entry : responsesMap.entrySet()) {
                Long questionId = Long.parseLong(entry.getKey());
                String answerValue = entry.getValue().toString();

                Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new RuntimeException(
                                "Question not found with id: " + questionId));

                Response response = Response.builder()
                        .question(question)
                        .answer(answerValue)
                        .submittedForm(savedSubmittedForm)
                        .build();

                responses.add(response);
            }

            responseRepository.saveAll(responses);

            return ResponseEntity.ok("Responses saved successfully!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save responses: " + ex.getMessage()));
        }
    }
}

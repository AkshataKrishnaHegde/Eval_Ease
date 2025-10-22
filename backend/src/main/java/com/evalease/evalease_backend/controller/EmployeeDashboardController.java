package com.evalease.evalease_backend.controller;

import com.evalease.evalease_backend.dto.DashboardFormDTO;
import com.evalease.evalease_backend.dto.GenericResponseDTO;
import com.evalease.evalease_backend.service.EmployeeDashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee-dashboard")
@CrossOrigin(origins = "${FRONTEND_URL}") // dynamic CORS
public class EmployeeDashboardController {

    @Autowired
    private EmployeeDashboardService employeeDashboardService;

    @GetMapping("/forms/{employeeId}")
    public ResponseEntity<GenericResponseDTO<Map<String, List<DashboardFormDTO>>>> getFormsForEmployee(
            @PathVariable Long employeeId) {
        try {
            Map<String, List<DashboardFormDTO>> forms = employeeDashboardService.getFormsForEmployee(employeeId);

            return ResponseEntity.ok(
                GenericResponseDTO.<Map<String, List<DashboardFormDTO>>>builder()
                    .success(true)
                    .data(forms)
                    .error(null)
                    .build()
            );
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                GenericResponseDTO.<Map<String, List<DashboardFormDTO>>>builder()
                    .success(false)
                    .data(null)
                    .error(ex.getMessage())
                    .build()
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                GenericResponseDTO.<Map<String, List<DashboardFormDTO>>>builder()
                    .success(false)
                    .data(null)
                    .error("Failed to retrieve forms for employee: " + ex.getMessage())
                    .build()
            );
        }
    }
}


package com.evalease.evalease_backend.controller;

import com.evalease.evalease_backend.entity.Employee;
import com.evalease.evalease_backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "${FRONTEND_URL}") 
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;


    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (existingEmployee.isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email already exists. Please login.");
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);
        if (employeeOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Employee not found");
        }
        return ResponseEntity.ok(employeeOpt.get());
    }

    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }
}

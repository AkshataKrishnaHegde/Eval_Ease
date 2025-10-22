package com.evalease.evalease_backend.controller;

import com.evalease.evalease_backend.dto.AdminDashboardStatsDTO;
import com.evalease.evalease_backend.service.FormService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    private final FormService dashboardService;
    private final String frontendUrl;

    public AdminDashboardController(FormService dashboardService, 
                                    @Value("${FRONTEND_URL}") String frontendUrl) {
        this.dashboardService = dashboardService;
        this.frontendUrl = frontendUrl;
    }

    @CrossOrigin(origins = "${FRONTEND_URL}")
@GetMapping("/stats")
public ResponseEntity<AdminDashboardStatsDTO> getDashboardStats() {
    try {
        AdminDashboardStatsDTO stats = dashboardService.getAdminDashboardStats();
        return ResponseEntity.ok(stats);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).build();
    }
}

}

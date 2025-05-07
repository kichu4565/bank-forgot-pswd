package com.bms.controller;

import com.bms.dto.DashboardResponse;
import com.bms.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard management APIs")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Get dashboard data", description = "Retrieves account summary, transaction summary, and recent transactions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<DashboardResponse> getAccountDashboard(
            @Parameter(description = "Account number", required = true)
            @PathVariable String accountNumber) {
        return ResponseEntity.ok(dashboardService.getAccountDashboard(accountNumber));
    }
} 
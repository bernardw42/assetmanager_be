package com.example.asset_manager.controller.auth;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.asset_manager.dto.assignment.AssignmentResponse;
import com.example.asset_manager.entity.Employee;
import com.example.asset_manager.exception.NotFoundException;
import com.example.asset_manager.repository.EmployeeRepository;
import com.example.asset_manager.security.CustomUserDetails;
import com.example.asset_manager.service.AssetAssignmentService;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final AssetAssignmentService assignmentService;
    private final EmployeeRepository employeeRepository;

    public MeController(AssetAssignmentService assignmentService, EmployeeRepository employeeRepository) {
        this.assignmentService = assignmentService;
        this.employeeRepository = employeeRepository;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/laptops")
    public List<AssignmentResponse> myLaptops(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Employee emp = employeeRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("Employee not found for email: " + user.getEmail()));

        return assignmentService.listCurrentByEmployee(emp.getId());
    }
}
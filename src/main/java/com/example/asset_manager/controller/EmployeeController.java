package com.example.asset_manager.controller;

import com.example.asset_manager.dto.employee.EmployeeCreateRequest;
import com.example.asset_manager.dto.employee.EmployeeResponse;
import com.example.asset_manager.dto.employee.EmployeeUpdateRequest;
import com.example.asset_manager.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','ASSET_ADMIN')")
    @GetMapping
    public List<EmployeeResponse> list() {
        return employeeService.list();
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','ASSET_ADMIN')")
    @GetMapping("/{id}")
    public EmployeeResponse get(@PathVariable Long id) {
        return employeeService.get(id);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping
    public EmployeeResponse create(@Valid @RequestBody EmployeeCreateRequest req) {
        return employeeService.create(req);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequest req) {
        return employeeService.update(id, req);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }
}

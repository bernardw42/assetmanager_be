package com.example.asset_manager.repository;

import com.example.asset_manager.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(String employeeId);
    Optional<Employee> findByEmail(String email);
    boolean existsByEmployeeIdIgnoreCase(String employeeId);
    boolean existsByEmployeeIdIgnoreCaseAndIdNot(String employeeId, Long id);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}

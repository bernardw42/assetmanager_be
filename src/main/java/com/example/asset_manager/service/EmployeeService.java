package com.example.asset_manager.service;

import com.example.asset_manager.dto.employee.EmployeeCreateRequest;
import com.example.asset_manager.dto.employee.EmployeeResponse;
import com.example.asset_manager.dto.employee.EmployeeUpdateRequest;
import com.example.asset_manager.entity.Employee;
import com.example.asset_manager.entity.UserAccount;
import com.example.asset_manager.exception.ConflictException;
import com.example.asset_manager.exception.NotFoundException;
import com.example.asset_manager.repository.EmployeeRepository;
import com.example.asset_manager.repository.UserAccountRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<EmployeeResponse> list() {
        Map<Long, UserAccount> userAccountsByEmployeeId = userAccountRepository.findAll().stream()
                .collect(Collectors.toMap(UserAccount::getEmployeeId, Function.identity(), (left, right) -> left));

        return employeeRepository.findAll().stream()
                .map(employee -> toResponse(employee, userAccountsByEmployeeId.get(employee.getId())))
                .toList();
    }

    public EmployeeResponse get(Long id) {
        Employee employee = findEntity(id);
        return toResponse(employee, userAccountRepository.findByEmployeeId(id).orElse(null));
    }

    @Transactional
    public EmployeeResponse create(EmployeeCreateRequest req) {
        String employeeCode = normalizeRequiredText(req.getEmployeeId(), "Employee code");
        String fullName = normalizeRequiredText(req.getFullName(), "Full name");
        String department = normalizeRequiredText(req.getDepartment(), "Department");
        String email = normalizeEmail(req.getEmail());
        String role = normalizeRole(req.getRole());
        String tempPassword = requirePassword(req.getTempPassword(), "Password");
        ensureUniqueForCreate(employeeCode, email);

        Employee e = new Employee();
        e.setEmployeeId(employeeCode);
        e.setFullName(fullName);
        e.setDepartment(department);
        e.setEmail(email);
        e.setIsActive(req.getIsActive());
        Employee savedEmployee = saveEmployee(e);

        UserAccount userAccount = new UserAccount();
        userAccount.setEmployeeId(savedEmployee.getId());
        userAccount.setRole(role);
        userAccount.setIsActive(req.getIsActive());
        userAccount.setPasswordHash(passwordEncoder.encode(tempPassword));
        userAccount.setLastPasswordChangedAt(java.time.LocalDateTime.now());

        return toResponse(savedEmployee, saveUserAccount(userAccount));
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeUpdateRequest req) {
        Employee e = findEntity(id);
        String fullName = normalizeRequiredText(req.getFullName(), "Full name");
        String department = normalizeRequiredText(req.getDepartment(), "Department");
        String email = normalizeEmail(req.getEmail());
        String role = normalizeRole(req.getRole());
        String newPassword = normalizeOptionalPassword(req.getNewPassword());
        ensureUniqueForUpdate(id, email);

        e.setFullName(fullName);
        e.setDepartment(department);
        e.setEmail(email);
        e.setIsActive(req.getIsActive());
        Employee savedEmployee = saveEmployee(e);

        UserAccount userAccount = userAccountRepository.findByEmployeeId(id)
                .orElseThrow(() -> new NotFoundException("User account not found for employeeId=" + id));

        userAccount.setRole(role);
        userAccount.setIsActive(req.getIsActive());

        if (newPassword != null) {
            if (passwordEncoder.matches(newPassword, userAccount.getPasswordHash())) {
                throw new IllegalArgumentException("New password must be different from the current password");
            }
            userAccount.setPasswordHash(passwordEncoder.encode(newPassword));
            userAccount.setLastPasswordChangedAt(java.time.LocalDateTime.now());
        }

        return toResponse(savedEmployee, saveUserAccount(userAccount));
    }

    @Transactional
    public void delete(Long id) {
        Employee e = findEntity(id);
        userAccountRepository.findByEmployeeId(id).ifPresent(userAccountRepository::delete);
        employeeRepository.delete(e);
    }

    public Employee findEntity(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found: " + id));
    }

    private EmployeeResponse toResponse(Employee e, UserAccount userAccount) {
        EmployeeResponse r = new EmployeeResponse();
        r.setId(e.getId());
        r.setEmployeeId(e.getEmployeeId());
        r.setFullName(e.getFullName());
        r.setDepartment(e.getDepartment());
        r.setEmail(e.getEmail());
        r.setIsActive(e.getIsActive());
        r.setRole(userAccount != null ? userAccount.getRole() : null);
        r.setLastPasswordChangedAt(userAccount != null ? userAccount.getLastPasswordChangedAt() : null);
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        return r;
    }

    private void ensureUniqueForCreate(String employeeCode, String email) {
        var fieldErrors = new LinkedHashMap<String, String>();
        if (employeeRepository.existsByEmployeeIdIgnoreCase(employeeCode)) {
            fieldErrors.put("employeeId", "Employee code already exists.");
        }
        if (employeeRepository.existsByEmailIgnoreCase(email)) {
            fieldErrors.put("email", "Email already exists.");
        }
        if (!fieldErrors.isEmpty()) {
            throw new ConflictException("Validation failed", fieldErrors);
        }
    }

    private void ensureUniqueForUpdate(Long id, String email) {
        var fieldErrors = new LinkedHashMap<String, String>();
        if (employeeRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            fieldErrors.put("email", "Email already exists.");
        }
        if (!fieldErrors.isEmpty()) {
            throw new ConflictException("Validation failed", fieldErrors);
        }
    }

    private Employee saveEmployee(Employee employee) {
        try {
            return employeeRepository.save(employee);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Employee code or email already exists.");
        }
    }

    private UserAccount saveUserAccount(UserAccount userAccount) {
        try {
            return userAccountRepository.save(userAccount);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Linked user account already exists.");
        }
    }

    private String normalizeRequiredText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    private String normalizeEmail(String email) {
        String normalized = normalizeRequiredText(email, "Email").toLowerCase(Locale.ROOT);
        if (!normalized.endsWith("@jptomato.com")) {
            throw new IllegalArgumentException("Email must end with @jptomato.com.");
        }
        return normalized;
    }

    private String normalizeRole(String role) {
        String normalized = normalizeRequiredText(role, "Role").toUpperCase(Locale.ROOT);
        if (!java.util.Set.of("SYSTEM_ADMIN", "ASSET_ADMIN", "EMPLOYEE").contains(normalized)) {
            throw new IllegalArgumentException("Role must be SYSTEM_ADMIN, ASSET_ADMIN, or EMPLOYEE.");
        }
        return normalized;
    }

    private String requirePassword(String password, String fieldName) {
        String normalized = normalizeRequiredText(password, fieldName);
        if (normalized.length() > 255) {
            throw new IllegalArgumentException(fieldName + " must be 255 characters or fewer.");
        }
        return normalized;
    }

    private String normalizeOptionalPassword(String password) {
        if (password == null) {
            return null;
        }
        String trimmed = password.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (trimmed.length() > 255) {
            throw new IllegalArgumentException("Password must be 255 characters or fewer.");
        }
        return trimmed;
    }
}

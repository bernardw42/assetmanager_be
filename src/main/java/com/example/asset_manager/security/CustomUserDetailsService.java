package com.example.asset_manager.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.asset_manager.entity.Employee;
import com.example.asset_manager.entity.UserAccount;
import com.example.asset_manager.repository.EmployeeRepository;
import com.example.asset_manager.repository.UserAccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository, UserAccountRepository userAccountRepository) {
        this.employeeRepository = employeeRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee emp = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found for email: " + email));

        UserAccount ua = userAccountRepository.findByEmployeeId(emp.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User account not found for employeeId: " + emp.getId()));

        return new CustomUserDetails(
                emp.getId(),
                emp.getEmail(),
                ua.getPasswordHash(),
                ua.getRole(),
                Boolean.TRUE.equals(ua.getIsActive())
        );
    }
}
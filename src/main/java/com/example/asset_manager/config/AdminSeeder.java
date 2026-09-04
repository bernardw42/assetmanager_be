package com.example.asset_manager.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.asset_manager.entity.Employee;
import com.example.asset_manager.entity.UserAccount;
import com.example.asset_manager.repository.EmployeeRepository;
import com.example.asset_manager.repository.UserAccountRepository;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner seedSystemAdmin(
            EmployeeRepository employeeRepository,
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String email = "system@jptomato.com";

            Employee employee = employeeRepository.findByEmail(email)
                    .orElseGet(() -> {
                        Employee e = new Employee();
                        e.setEmployeeId("SYS001");
                        e.setFullName("System Admin");
                        e.setDepartment("IT");
                        e.setEmail(email);
                        e.setIsActive(true);
                        return employeeRepository.save(e);
                    });

            if (userAccountRepository.findByEmployeeId(employee.getId()).isEmpty()) {

                UserAccount user = new UserAccount();
                user.setEmployeeId(employee.getId());
                user.setPasswordHash(passwordEncoder.encode("12345"));
                user.setRole("SYSTEM_ADMIN");
                user.setIsActive(true);

                userAccountRepository.save(user);

                System.out.println("SYSTEM_ADMIN seeded.");
            }
        };
    }
}
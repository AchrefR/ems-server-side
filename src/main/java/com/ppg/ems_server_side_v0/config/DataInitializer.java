package com.ppg.ems_server_side_v0.config;

import com.ppg.ems_server_side_v0.domain.Department;
import com.ppg.ems_server_side_v0.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("DataInitializer starting...");
        initializeDepartments();
        log.info("DataInitializer completed.");
    }

    private void initializeDepartments() {
        if (departmentRepository.count() == 0) {
            log.info("Initializing departments...");
            
            String[] departmentNames = {
                "Engineering", "Human Resources", "Finance", "Marketing", 
                "Sales", "Operations", "IT", "Legal", "Design", "Product"
            };
            
            String[] departmentTypes = {
                "Technical", "Administrative", "Financial", "Commercial",
                "Commercial", "Operational", "Technical", "Administrative", "Creative", "Strategic"
            };
            
            for (int i = 0; i < departmentNames.length; i++) {
                Department department = Department.builder()
                    .departmentName(departmentNames[i])
                    .departmentType(departmentTypes[i])
                    .build();
                
                departmentRepository.save(department);
                log.info("Created department: {}", departmentNames[i]);
            }
            
            log.info("Departments initialization completed.");
        } else {
            log.info("Departments already exist, skipping initialization.");
        }
    }


}

package com.example.payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration  Indicates the class has @Bean definition methods.
 *                 Ensures the spring container can process the class.
 */
@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    /**
     * @Bean  Specifies the method returns a bean to be managed by the context.
     * @CommandLineRunner  An interface with a run method. The run method is automatically
     *                     called on all beans implementing the interface.
     */
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {

        return args -> {

            employeeRepository.save(new Employee("Bilbo", "burglar", "Baggins"));
            employeeRepository.save(new Employee("Frodo", "thief", "Baggins"));

            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

            orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };
    }

//@Bean
//CommandLineRunner commandLineRunner(EmployeeRepository repository) {
//
//    return args -> {
//        log.info("Preloading " + repository.save(new Employee("Bilbo", "Burglar", "Baggins")));
//        log.info("Preloading " + repository.save(new Employee("Frodo", "Thief", "Baggins")));
//    };
//}
}

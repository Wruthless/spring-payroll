package com.example.payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    // add EmployeeModelAssembler for EntityModel conversion
    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    /**
     * @CollectionModel  Creates a wrapper for a collection of Entities.
     * EntityModel was utilized int the get mapping for a single employee.
     * Here, we are creating a collection of these entity models.
     */
    CollectionModel<EntityModel<Employee>> all() {

        // Find all entities in repository. New code using assembler.
        List<EntityModel<Employee>> employees = repository.findAll().stream()
            .map(assembler::toModel).collect(Collectors.toList());

        // We have to manually build the aggregate root link
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());


        /* Old code replaced with assembler
        List<EntityModel<Employee>> employees = repository.findAll().stream()
            .map(employee -> EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
            .collect(Collectors.toList());

        return CollectionModel.of(employees,
            linkTo(methodOn(EmployeeController.class).all()).withSelfRel());

       List<Employee> all() {
               return repository.findAll();
        }
        */
    }

    /**
     * @ResponseEntity -- used to create an HTTP "201 Created" status message.
     * @param newEmployee
     * @return -- a model-based version of the saved object.
     */
    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {

        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

   // Employee newEmployee(@RequestBody Employee newEmployee) { return repository.save(newEmployee);}


    @GetMapping("/employee/{id}")
    /**
     * @EntityModel  Describes the relationship between an Entity and a table/tables in a DB.
     */
    EntityModel<Employee> one(@PathVariable Long id) {

        // Look for the employee by id and if not found throw an exception.
        Employee employee = repository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));

        // replace old code with assembler
        return assembler.toModel(employee);

        /* Old code before defining assembler.
        return EntityModel.of(employee,
            // Self-link.
            linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
            // Aggregate root link.
            linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));

        Employee one(@PathVariable Long id) {
            return repository.findById(id) .orElseThrow(() -> new  EmployeeNotFoundException(id));
        }
        */

    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        Employee updatedEmployee = repository.findById(id)
          .map(employee -> {
                employee.setName(newEmployee.getName());
                employee.setRole(newEmployee.getRole());
                return repository.save(employee);
            })
          .orElseGet(() -> {
              newEmployee.setId(id);
              return repository.save(newEmployee);
          });

        // Wrap Employee object from above with EmployeeModelAssembler
        // converting Employee object to EntityModel<Employee> objects.
        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        // getRequiredLink retrieves the Link created by the EmployeeModelAssembler with the SELF
        // rel. This returns a Link which must be turned into a URI with the toUri method.
        // The ResponseEntity wrapper provides a Location response header and will throw a 201 created
        // upon successful PUT request.
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

//    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//
//        return repository.findById(id)
//            .map(employee -> {
//                employee.setName(newEmployee.getName());
//                employee.setRole(newEmployee.getRole());
//                return repository.save(employee);
//            })
//            .orElseGet(() -> {
//                newEmployee.setId(id);
//                return repository.save(newEmployee);
//            });
//    }


    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);

        // return a 204 No Content response.
        return ResponseEntity.noContent().build();
    }
//    void deleteEmployee(@PathVariable Long id){
//        repository.deleteById(id);
//    }
}

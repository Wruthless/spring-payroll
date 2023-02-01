package com.example.payroll;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @RepresentationModelAssembler  Handles the conversion of Employee > EntityModel<Employee>
 *                                It is an interface with one method --toModal(//non-model object)
 */
@Component
class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    /**
     * Converts employee objects to EntityModel<Employee> objects.
     */
    @Override
    public EntityModel<Employee> toModel(Employee employee) {

        return EntityModel.of(employee,
            linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));

    }
}

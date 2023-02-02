package com.example.payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

/**
 * @Entity  JPA annotation to make objects read for storage in a JPA data store.
 */
@Entity
public class Employee {

    /**
     * @Id  Indicate primary key.
     * @GeneratedValue  Primary key is automatically generated value.
     */
    private @Id @GeneratedValue Long id;
    //private String name;  (deprecated)
    private String role;
    private String firstName;
    private String lastName;


    public Employee(){};

    // Removing name and replacing with firstName and lastName
    Employee(String firstName, String role, String lastName) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // A "virtual" getter for the old name property, uses firstName and lastName to produce a value.
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    // A "virtual" setter for the old name property. Parses incoming string and stores it into proper fields.
    public void setName(String name) {
        String[] parts = name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(role, employee.role) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", role='" + role + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }
}

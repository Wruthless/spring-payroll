package com.example.payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @ControllerAdvice  Allows exception handling across the entire application in
 *                    one global handling component. Intercepts exceptions thrown
 *                    by methods annotated with @RequestMapping and similar.
 */
@ControllerAdvice
public class EmployeeNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(EmployeeNotFoundException ex) {
        return ex.getMessage();
    }
}

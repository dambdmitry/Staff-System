package org.internship.system.models;

import java.util.Date;

public class ModelFactory {
    public static PersonnelOrder createOrder(Date orderDate, Long orderNumber, Long employeeId, String orderType, Long departmentId){
        return new PersonnelOrder(orderDate, orderNumber, employeeId, orderType, departmentId);
    }

    public static Employee createEmployee(Long id, Long departmentId, String lastName, String firstName, String patronymic){
        return new Employee(id, departmentId, lastName, firstName, patronymic);
    }

    public static Department createDepartment(Long id, Long parentId, String departmentName){
        return new Department(id, parentId, departmentName);
    }
}

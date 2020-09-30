package org.internship.system.models;

import org.internship.system.models.enums.Role;

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

    public static User createUser(String username, String password, String role){
        return role.equals("HR") ? new User(username, password, Role.HR) : new User(username, password, Role.EMPLOYEE);
    }

    public static UserAction createUserAction(String login, String action, String object){
        return new UserAction(login, action, object);
    }
}

package org.internship.system.organization;

import org.internship.system.db.OrganizationDatabase;
import org.internship.system.exceptions.NotFoundDepartmentException;
import org.internship.system.exceptions.NotFoundWorkerException;
import org.internship.system.models.Department;
import org.internship.system.models.Employee;
import org.internship.system.models.PersonnelOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.Date;
import java.util.Set;

@Component
public class Organization implements OrganizationManagment{
    @Autowired
    private OrganizationDatabase orgDb;

    @PreDestroy
    public void closeConnection(){
        orgDb.closeConnection();
    }

    @Override
    public Set<Employee> getAllEmployees() {
        return orgDb.getAllEmployees();
    }

    @Override
    public Set<Employee> getEmployeesByDepartment(Long departmentId) {
        return orgDb.getEmployeesByDepartment(departmentId);
    }

    @Override
    public Set<Employee> getEmployeesByDate(Date date) {
        return orgDb.getEmployeesWorkingByDate(date);
    }

    @Override
    public Set<PersonnelOrder> getAllOrders() {
        return orgDb.getAllOrders();
    }

    @Override
    public Set<Department> getAllDepartments() {
        return orgDb.getAllDepartmentsByHierarchy();
    }

    @Override
    public Department getDepartmentById(Long id) {
        if(orgDb.hasDepartment(id)){
            return orgDb.getDepartmentById(id);
        }else{
            return null;
        }
    }

    @Override
    public Employee getEmployeeById(Long id) {
        if(orgDb.hasEmployee(id)){
            return orgDb.getEmployeeById(id);
        }else{
            return null;
        }
    }

    @Override
    public void addEmployee(String lastName, String firstName, String patronymic) {
        orgDb.addEmployee(lastName, firstName, patronymic);
    }

    @Override
    public void addOrder(Date orderDate, Long orderNumber, String orderType, Long departmentId, Long employeeId) {
        orgDb.addPersonnelOrder(orderDate, orderNumber, orderType, departmentId, employeeId);
        if(departmentId != null){
            orgDb.updateEmployeeDepartmentId(employeeId, departmentId);
        }
    }

    @Override
    public void addDepartment(Long parentId, String departmentName) {
        orgDb.addDepartment(parentId, departmentName);
    }

    @Override
    public void addDepartment(String departmentName) {
        orgDb.addDepartment(departmentName);
    }

    @Override
    public void removeEmployee(Long id) {
        if(orgDb.hasEmployee(id) && !orgDb.hasEmployeeOrders(id)){
            orgDb.removeEmployee(id);
        }else{
            throw new NotFoundWorkerException("Невозможно удалить этого сотрудника");
        }
    }

    @Override
    public void removeDeparment(Long id) {
        if(orgDb.hasDepartment(id) && !orgDb.hasDepartmentOrders(id)){
            orgDb.removeDepartment(id);
        }else{
            throw new NotFoundDepartmentException("Невозможно удалить это подразделение");
        }
    }

    @Override
    public boolean hasOrderNumber(Long number) {
        return orgDb.hasOrderNumber(number);
    }

    @Override
    public boolean hasDepartmentOrders(Long departmentId) {
        return orgDb.hasDepartmentOrders(departmentId);
    }

    @Override
    public boolean hasEmployeeOrders(Long employeeId) {
        return orgDb.hasEmployeeOrders(employeeId);
    }
}

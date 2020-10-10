package edu.prctice.staff.organization;


import edu.prctice.staff.models.Department;
import edu.prctice.staff.models.Employee;
import edu.prctice.staff.models.PersonnelOrder;

import java.sql.Date;
import java.util.Set;

public interface OrganizationManagment {
    Set<Employee> getAllEmployees();

    Set<Employee> getEmployeesByDepartment(Long departmentId);

    Set<Employee> getEmployeesByDate(Date date);

    Set<PersonnelOrder> getAllOrders();

    Set<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    Employee getEmployeeById(Long id);

    void addEmployee(String lastName, String firstName, String patronymic);

    void addOrder(Date orderDate, Long orderNumber, String orderType, Long departmentId, Long employeeId);

    void addDepartment(Long parentId, String departmentName);

    /*
    * Добавить главное подразделение
    * */
    void addDepartment(String departmentName);

    void removeEmployee(Long id);

    void removeDeparment(Long id);

    boolean hasOrderNumber(Long number);

    /*
    * Упоминается ли подразделение и его дочерние подразделения в кадровых приказах.
    * */
    boolean hasDepartmentOrders(Long departmentId);

    /*
    * Есть ли кадровые приказы по сотруднику.
    * */
    boolean hasEmployeeOrders(Long employeeId);
}

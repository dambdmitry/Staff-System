package org.internship.system.db;

import org.internship.system.models.Department;
import org.internship.system.models.Employee;
import org.internship.system.models.PersonnelOrder;

import java.sql.Date;
import java.util.Set;

public interface OrganizationDatabaseManagment {
    /*
    * Добавить в таблицу employee сотрудника.
    *  */
    void addEmployee(String lastName, String firstName, String patronymic);

    /*
    * Добавить в таблицу orders кадровый приказ.
    * */
    void addPersonnelOrder(Date orderDate, Long orderNumber, String orderType, Long departmentId, Long employeeId);

    /*
    * Добавить в таблицу departments новое подразделение, учитывая его иерархическое положение.
    * */
    void addDepartment(Long parentId, String departmentName);

    /*
    * Добавить главное подразделение.
    * */
    void addDepartment(String departmentName);

    void removeEmployee(Long id);
    /*
    * Удалить само подразделение и все его дочерние подразделения.
    * */

    void removeDepartment(Long id);

    Set<Employee> getEmployeesWorkingByDate(Date date);
    /*
    * Вернуть работников данного подразделения и всех работников его дочерних подразделений.
    * */

    Set<Employee> getEmployeesByDepartment(Long departmentId);

    Set<Employee> getAllEmployees();

    Set<Department> getAllDepartmentsByHierarchy();

    Set<PersonnelOrder> getAllOrders();

    Employee getEmployeeById(Long id);

    Department getDepartmentById(Long id);

    PersonnelOrder getOrderByNumber(Long number);

    boolean hasEmployee(Long employeeId);
    /*
    * Имеются ли по работнику с таким id кадровые приказы.
    * */

    boolean hasEmployeeOrders(Long employeeId);

    boolean hasDepartment(Long departmentId);
    /*
    * Упоминается ли департамент с данным id в кадровых приказах.
    * */

    boolean hasDepartmentOrders(Long departmentId);
    /*
    * Имеется ли в базе приказ с таким номером.
    * */

    boolean hasOrderNumber(Long number);

    void updateEmployeeDepartmentId(Long employeeId, Long departmentId);
}

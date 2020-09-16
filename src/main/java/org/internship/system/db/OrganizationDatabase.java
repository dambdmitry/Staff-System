package org.internship.system.db;

import org.internship.system.db.config.Config;
import org.internship.system.exceptions.NotFoundDepartmentException;
import org.internship.system.exceptions.NotFoundOrderException;
import org.internship.system.exceptions.NotFoundWorkerException;
import org.internship.system.models.*;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class OrganizationDatabase implements OrganizationDatabaseManagment {
    private Connection connection = null;
    private final static String INSERT_DEPARTMENT = "INSERT INTO departments(" +
            "department_name, parent_id)" +
            "VALUES (?, ?);";
    private final static String INSERT_MAIN_DEPARTMENT = "INSERT INTO departments(department_name) VALUES (?);";
    private final static String INSERT_EMPLOYEE = "INSERT INTO employees(" +
            "last_name, first_name, patronymic)" +
            "VALUES (?, ?, ?);";
    private final static String INSERT_ORDER_WITH_DEPARTMENT = "INSERT INTO orders(" +
            "order_date, order_number, order_type, department_id, employee_id)" +
            "VALUES (?, ?, ?, ?, ?);";
    private final static String INSERT_ORDER_WITHOUT_DEPARTMENT = "INSERT INTO orders(" +
            "order_date, order_number, order_type, employee_id)" +
            "VALUES (?, ?, ?, ?);";
    private final static String DELETE_DEPARTMENT_BY_ID = "WITH RECURSIVE a AS(\n" +
            "            SELECT department_id, department_name, parent_id FROM departments\n" +
            "            WHERE department_id = ?\n" +
            "            UNION ALL\n" +
            "            SELECT d.department_id, d.department_name, d.parent_id FROM departments d\n" +
            "            JOIN a ON a.department_id = d.parent_id)\n" +
            "            DELETE FROM departments WHERE department_id IN (SELECT department_id FROM a);";
    private final static String DELETE_EMPLOYEE_BY_ID = "DELETE FROM employees WHERE employee_id = ?;";
    private final static String SELECT_ORDER_BY_EMPLOYEE_ID = "SELECT * FROM orders WHERE employee_id = ?;";
    private final static String SELECT_ORDER_BY_DEPARTMENT_ID = "SELECT * FROM orders WHERE department_id = ?;";
    private final static String SELECT_EMPLOYEES_BY_DEPARTMENT_ID = "WITH RECURSIVE a AS(\n" +
            "\tSELECT employee_id, last_name, first_name, patronymic, employee_department_id, department_id FROM employees\n" +
            "\t\tRIGHT OUTER JOIN departments ON (departments.department_id = employees.employee_department_id)\n" +
            "\tWHERE departments.department_id = ?\n" +
            "\tUNION ALL\n" +
            "\tSELECT e.employee_id, e.last_name, e.first_name, e.patronymic, e.employee_department_id, d.department_id FROM employees e\n" +
            "\t\tRIGHT OUTER JOIN departments d ON (d.department_id = e.employee_department_id)\t\t\t\n" +
            "\tJOIN a ON a.department_id IN (SELECT parent_id FROM departments WHERE department_id = d.department_id))\n" +
            "\n" +
            "SELECT a.employee_id, a.last_name, a.first_name, a.patronymic, a.employee_department_id FROM a WHERE (a.employee_id IS NOT NULL);";
    private final static String SELECT_EMPLOYEES_BY_DATE = "SELECT * FROM employees e\n" +
            "\tWHERE(e.employee_id IN (SELECT employee_id FROM orders \n" +
            "\t\tWHERE (order_date BETWEEN order_date AND ?) AND\n" +
            "\t\torder_type = 'Прием'))\n" +
            "\tAND (e.employee_id NOT IN (SELECT employee_id FROM orders\n" +
            "\t\tWHERE order_type = 'Увольнение'))";
    private final static String SELECT_ALL_EMPLOYEES = "SELECT * FROM employees;";
    private final static String SELECT_ALL_ORDERS = "SELECT * FROM orders;";
    private final static String SELECT_ALL_DEPARTMENTS = "WITH RECURSIVE a AS(\n" +
            "            SELECT department_id, department_name, parent_id FROM departments\n" +
            "            WHERE parent_id IS NULL\n" +
            "            UNION ALL\n" +
            "            SELECT d.department_id, d.department_name, d.parent_id FROM departments d\n" +
            "            JOIN a ON a.department_id = d.parent_id)\n" +
            "\t\t\tSELECT * FROM a;";
    private final static String SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employees WHERE employee_id = ?;";
    private final static String SELECT_DEPARTMENT_BY_ID = "SELECT * FROM departments WHERE department_id = ?;";
    private final static String SELECT_ORDER_BY_ORDER_NUMBER = "SELECT * FROM orders WHERE order_number = ?;";
    private final static String UPDATE_EMPLOYEE_DEPARTMENT = "UPDATE employees SET employee_department_id= ? WHERE employee_id = ?;";
    private final static String SELECT_ORDERS_FROM_DEPARTMENTS_TREE = "WITH RECURSIVE a AS(\n" +
            "            SELECT department_id, department_name, parent_id FROM departments\n" +
            "            WHERE department_id = ?\n" +
            "            UNION ALL\n" +
            "            SELECT d.department_id, d.department_name, d.parent_id FROM departments d\n" +
            "            JOIN a ON a.department_id = d.parent_id)\n" +
            "\t\t\tSELECT * FROM orders WHERE department_id IN (SELECT department_id FROM a);";

    private Connection getConnection() throws SQLException {
        if(connection == null) {
            connection = DriverManager.getConnection(
                    Config.getProperty(Config.DB_URL),
                    Config.getProperty(Config.DB_LOGIN),
                    Config.getProperty(Config.DB_PASSWORD));
        }
        return connection;
    }

    @PreDestroy
    public void closeConnection() {
        try {
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    private void closeStatment(PreparedStatement stmt){
        try {
            if(stmt != null && !stmt.isClosed()){
                stmt.close();
            }
        } catch (SQLException throwables) {
        }
    }

    @Override
    public void addEmployee(String lastName, String firstName, String patronymic) {
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(INSERT_EMPLOYEE);

            statement.setString(1, lastName);
            statement.setString(2, firstName);
            statement.setString(3, patronymic);

            statement.executeUpdate();

        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public void addPersonnelOrder(Date orderDate, Long orderNumber, String orderType, Long departmentId, Long employeeId) {
        //TODO или сделать еще один метод с переполнением без департамента.
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();

            if(departmentId != null){
                statement = connection.prepareStatement(INSERT_ORDER_WITH_DEPARTMENT);
                statement.setDate(1, orderDate);
                statement.setLong(2, orderNumber);
                statement.setString(3, orderType);
                statement.setLong(4, departmentId);
                statement.setLong(5, employeeId);
            }else{
                statement = connection.prepareStatement(INSERT_ORDER_WITHOUT_DEPARTMENT);
                statement.setDate(1, orderDate);
                statement.setLong(2, orderNumber);
                statement.setString(3, orderType);
                statement.setLong(4, employeeId);
            }
            statement.executeUpdate();

        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }

    }

    @Override
    public void addDepartment(Long parentId, String departmentName) {
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(INSERT_DEPARTMENT);

            statement.setString(1, departmentName);
            statement.setLong(2, parentId);

            statement.executeUpdate();

        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public void addDepartment(String departmentName) {
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(INSERT_MAIN_DEPARTMENT);

            statement.setString(1, departmentName);

            statement.executeUpdate();

        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public void removeEmployee(Long id) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(DELETE_EMPLOYEE_BY_ID);

            statement.setLong(1, id);
            int successfulRemove = statement.executeUpdate();
            if(successfulRemove == 0){
                throw new NotFoundWorkerException("Сотрудника с таким номером нет");
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public void removeDepartment(Long id) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(DELETE_DEPARTMENT_BY_ID);

            statement.setLong(1, id);
            int successfulRemove = statement.executeUpdate();
            if(successfulRemove == 0){
                throw new NotFoundDepartmentException("Сотрудника с таким номером нет");
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public Set<Employee> getEmployeesWorkingByDate(Date date) {
        Set<Employee> employees = new LinkedHashSet<>();
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_EMPLOYEES_BY_DATE);
            statement.setDate(1, date);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Long id = rs.getLong("employee_id");
                Long localDepartmentId = rs.getLong("employee_department_id");
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");
                String patronymic = rs.getString("patronymic");

                Employee employee = ModelFactory.createEmployee(id, localDepartmentId, lastName, firstName, patronymic);
                employees.add(employee);
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
        return employees;
    }

    @Override
    public Set<Employee> getEmployeesByDepartment(Long departmentId) {
        Set<Employee> employees = new LinkedHashSet<>();
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_EMPLOYEES_BY_DEPARTMENT_ID);
            statement.setLong(1, departmentId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Long id = rs.getLong("employee_id");
                Long localDepartmentId = rs.getLong("employee_department_id");
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");
                String patronymic = rs.getString("patronymic");

                Employee employee = ModelFactory.createEmployee(id, localDepartmentId, lastName, firstName, patronymic);
                employees.add(employee);
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
        return employees;
    }

    @Override
    public Set<Employee> getAllEmployees() {
        Set<Employee> employees = new LinkedHashSet<>();
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_ALL_EMPLOYEES);

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Long id = rs.getLong("employee_id");
                Long departmentId = rs.getLong("employee_department_id");
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");
                String patronymic = rs.getString("patronymic");

                Employee employee = ModelFactory.createEmployee(id, departmentId, lastName, firstName, patronymic);
                employees.add(employee);
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
        return employees;
    }

    @Override
    public Set<Department> getAllDepartmentsByHierarchy() {
        Set<Department> departments = new LinkedHashSet<>();
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_ALL_DEPARTMENTS);

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Long id = rs.getLong("department_id");
                Long parentId = rs.getLong("parent_id");
                String name = rs.getString("department_name");
                Department department = ModelFactory.createDepartment(id, parentId, name);
                departments.add(department);
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
        return departments;
    }

    @Override
    public Set<PersonnelOrder> getAllOrders() {
        Set<PersonnelOrder> orders = new LinkedHashSet<>();
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_ALL_ORDERS);

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Date orderDate = rs.getDate("order_date");
                Long orderNumber = rs.getLong("order_number");
                Long employeeId = rs.getLong("employee_id");
                String orderType = rs.getString("order_type");
                Long departmentId = rs.getLong("department_id");
                PersonnelOrder po = ModelFactory.createOrder(orderDate, orderNumber, employeeId, orderType, departmentId);
                orders.add(po);
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
        return orders;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_EMPLOYEE_BY_ID);

            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Long departmentId = rs.getLong("employee_department_id");
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");
                String patronymic = rs.getString("patronymic");

                Employee employee = ModelFactory.createEmployee(id, departmentId, lastName, firstName, patronymic);
                return employee;
            }else{
                throw new NotFoundWorkerException("Работника с таким номером нет");
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public Department getDepartmentById(Long id) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_DEPARTMENT_BY_ID);

            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Long parentId = rs.getLong("parent_id");
                String name = rs.getString("department_name");
                Department department = ModelFactory.createDepartment(id, parentId, name);
                return department;
            }else{
                throw new NotFoundDepartmentException("Департамент не найден");
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public PersonnelOrder getOrderByNumber(Long number) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_ORDER_BY_ORDER_NUMBER);

            statement.setLong(1, number);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Date orderDate = rs.getDate("order_date");
                Long employeeId = rs.getLong("employee_id");
                String orderType = rs.getString("order_type");
                Long departmentId = rs.getLong("department_id");
                PersonnelOrder po = ModelFactory.createOrder(orderDate, number, employeeId, orderType, departmentId);
                return po;
            }else{
                throw new NotFoundOrderException("Приказ не найден");
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public boolean hasEmployee(Long employeeId) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_EMPLOYEE_BY_ID);

            statement.setLong(1, employeeId);

            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public boolean hasEmployeeOrders(Long employeeId) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_ORDER_BY_EMPLOYEE_ID);

            statement.setLong(1, employeeId);

            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public boolean hasDepartment(Long departmentId) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_DEPARTMENT_BY_ID);

            statement.setLong(1, departmentId);

            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public boolean hasDepartmentOrders(Long departmentId) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_ORDERS_FROM_DEPARTMENTS_TREE);

            statement.setLong(1, departmentId);

            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public boolean hasOrderNumber(Long number) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_ORDER_BY_ORDER_NUMBER);

            statement.setLong(1, number);

            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public void updateEmployeeDepartmentId(Long employeeId, Long departmentId) {
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(UPDATE_EMPLOYEE_DEPARTMENT);

            statement.setLong(1, departmentId);
            statement.setLong(2, employeeId);

            statement.executeUpdate();

        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }
}

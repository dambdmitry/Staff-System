package org.internship.system.controller;

import org.internship.system.models.*;
import org.internship.system.models.enums.ActionByUser;
import org.internship.system.models.enums.ActionObject;
import org.internship.system.mq.Producer;
import org.internship.system.organization.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Set;

@Controller
public class OrganizationController {
    private final Organization org;
    //private final ProducerManagement producer;
    @Autowired
    private final Producer producer;


    @Autowired
    public OrganizationController(Organization org, /*, ProducerManagement producer*/Producer producer) {
        this.org = org;
        //this.producer = producer;
        this.producer = producer;
    }


    @GetMapping("/employees")
    public String getEmployeesForm(Model model, @RequestParam(required = false) String date, @RequestParam(required = false) String departmentId){
        Set<Department> departments = org.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("org", org);
        Set<Employee> employees;
        if(date != null){
            Date dateSql = Date.valueOf(date);
            employees = org.getEmployeesByDate(dateSql);

        }else if(departmentId != null && !departmentId.equals("")){
            employees = org.getEmployeesByDepartment(Long.parseLong(departmentId));
        }else {
            employees = org.getAllEmployees();
        }
        model.addAttribute("allEmployees", employees);
        return "employees";
    }

    @PostMapping("/employees")
    public String removeEmployee(Model model, String idForDelete){
        Long id = Long.parseLong(idForDelete);
        if(!org.hasEmployeeOrders(id)){
            org.removeEmployee(id);

            producer.sendMessage(ActionByUser.REMOVE.getName(), ActionObject.EMPLOYEE.getName());

            return "redirect:/employees";
        }else{
            String msg = "Невозможно удалить этого работника";
            model.addAttribute("error", msg);
            return "error";
        }
    }

    @GetMapping("/orders")
    public String getOrdersForm(Model model){
        Set<PersonnelOrder> orders = org.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("org", org);
        return "orders";
    }

    @GetMapping("/departments")
    public String getDepartmentsForm(Model model){
        Set<Department> departments = org.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("org", org);
        return "departments";
    }

    @PostMapping("/departments")
    public String removeDepartment(Model model, String idForDelete){
        Long id = Long.parseLong(idForDelete);
        if(!org.hasDepartmentOrders(id)){
            org.removeDeparment(id);

            producer.sendMessage(ActionByUser.REMOVE.getName(), ActionObject.DEPARTMENT.getName());

            return "redirect:/departments";
        }else{
            String msg = "Невозможно удалить это подразделение";
            model.addAttribute("error", msg);
            return "error";
        }
    }

    @GetMapping("/addEmployee")
    public String addEmployeeForm(Model model){
        return "addEmployeeForm";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(Model model, String lastName, String firstName, String patronymic){
        if(!lastName.equals("") && !firstName.equals("") && !patronymic.equals("")) {
            org.addEmployee(lastName, firstName, patronymic);

            producer.sendMessage(ActionByUser.ADDITION.getName(), ActionObject.EMPLOYEE.getName());

            return "redirect:/employees";
        }else{
            String err = "Заполните все поля";
            model.addAttribute("error", err);
            return "error";
        }
    }

    @GetMapping("/addOrder")
    public String addOrderForm(Model model){
        Set<Employee> employees = org.getAllEmployees();
        Set<Department> departments = org.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("allEmployees", employees);
        return "addOrder";
    }

    @PostMapping("/addOrder")
    public String addOrder(Model model, Date orderDate, String orderNumber, String orderType, String employeeId, String departmentId){
        Set<Employee> employees = org.getAllEmployees();
        Set<Department> departments = org.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("allEmployees", employees);
        try{
            Long number = Long.parseLong(orderNumber);
            if(!org.hasOrderNumber(number)){
                if(orderDate != null && employeeId != null && !orderType.equals("") && departmentId != null){
                    if(orderType.equals("Увольнение")){
                        org.addOrder(orderDate, number, orderType, null, Long.parseLong(employeeId));
                    }else{
                        org.addOrder(orderDate, number, orderType, Long.parseLong(departmentId), Long.parseLong(employeeId));
                    }
                    producer.sendMessage(ActionByUser.ADDITION.getName(), ActionObject.ORDER.getName());
                    return "redirect:/orders";
                }else{
                    String msg = "Заполните все поля";
                    model.addAttribute("error", msg);
                    return "addOrder";
                }
            }else{
                String msg = "Приказ под номером " + number + " уже есть";
                model.addAttribute("error", msg);
                return "addOrder";
            }
        }catch (NumberFormatException ex){
            String msg = "Введите номер корректно";
            model.addAttribute("error", msg);
            return "addOrder";
        }
    }

    @GetMapping("/addDepartment")
    public String addDepartmentForm(Model model){
        Set<Department> departments = org.getAllDepartments();
        model.addAttribute("departments", departments);
        return "addDepartment";
    }

    @PostMapping("/addDepartment")
    public String addDepartment(Model model, String newDepartmentName, String parentId){

        if(!newDepartmentName.equals("")){
            if (!parentId.equals("")){
                org.addDepartment(Long.parseLong(parentId), newDepartmentName);
            }else{
                org.addDepartment(newDepartmentName);
            }
            producer.sendMessage(ActionByUser.ADDITION.getName(), ActionObject.DEPARTMENT.getName());
            return "redirect:/departments";
        }else{
            String msg = "Введите название подразделения";
            model.addAttribute("error", msg);
            return "error";
        }
    }
}

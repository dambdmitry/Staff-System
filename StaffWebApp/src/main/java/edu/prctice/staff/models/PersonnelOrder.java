package edu.prctice.staff.models;

import java.util.Date;

public class PersonnelOrder {
    private Date orderDate;
    private Long orderNumber;
    private Long employeeId;
    private String orderType;
    private Long departmentId;

    public PersonnelOrder(Date orderDate, Long orderNumber, Long employeeId, String orderType, Long departmentId) {
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
        this.employeeId = employeeId;
        this.orderType = orderType;
        this.departmentId = departmentId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}

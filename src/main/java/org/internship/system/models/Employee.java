package org.internship.system.models;

import java.util.Objects;

public class Employee {
    private Long id;
    private Long departmentId;
    private String firstName;
    private String lastName;
    private String patronymic;


    public Employee(Long id, Long departmentId, String lastName, String firstName, String patronymic) {
        this.id = id;
        this.departmentId = departmentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }

    public Employee(){}

    public Long getId(){
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getFullName(){
        return lastName + " " + firstName + " " + patronymic;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    @Override
    public String toString(){
        return id + " " + lastName + " " + firstName + " " + patronymic;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return id.equals(employee.id) && lastName.equals(employee.lastName) &&
                firstName.equals(employee.firstName) &&
                patronymic.equals(employee.patronymic);
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, lastName, firstName, patronymic);
    }
}

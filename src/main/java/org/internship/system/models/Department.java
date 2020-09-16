package org.internship.system.models;

import java.util.Objects;

public class Department {
    private Long id;
    private Long parentId;
    private String departmentName;

    public Department(Long id, Long parentId, String departmentName) {
        this.id = id;
        this.parentId = parentId;
        this.departmentName = departmentName;
    }

    public Department() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }



    @Override
    public String toString(){
        return departmentName;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Department department = (Department) obj;
        return departmentName.equals(department.departmentName);
    }

    @Override
    public int hashCode(){
        return Objects.hash(departmentName);
    }
}

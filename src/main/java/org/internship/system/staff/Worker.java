package org.internship.system.staff;

import java.util.Objects;

public class Worker {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;

    public Worker(int id, String lastName, String firstName, String patronymic){
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
    }

    public Worker(){}

    public int getId(){
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

    @Override
    public String toString(){
        return id + " " + lastName + " " + firstName + " " + patronymic;
    }

    @Override
    public boolean equals(Object obj){
       if(this == obj) return true;
       if (obj == null || getClass() != obj.getClass()) return false;
       Worker worker = (Worker) obj;
       return id == worker.id && lastName.equals(worker.lastName) &&
               firstName.equals(worker.firstName) &&
               patronymic.equals(worker.patronymic);
    }

    public void setId(int id) {
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

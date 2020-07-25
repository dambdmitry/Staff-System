package StaffSystem;

import java.util.Objects;

public class Worker {
    private int id;
    private String name;

    public Worker(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Worker(){}

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return id + " " + name;
    }

    @Override
    public boolean equals(Object obj){
       if(this == obj) return true;
       if (obj == null || getClass() != obj.getClass()) return false;
       Worker worker = (Worker) obj;
       return id == worker.id && name.equals(worker.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, name);
    }
}

package edu.prctice.staff.db;


import edu.prctice.staff.models.User;

public interface UsersTableManagement {

    User getUserByUsername(String username);
}

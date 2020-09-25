package org.internship.system.db;

import org.internship.system.models.User;

public interface UsersTableManagement {

    User getUserByUsername(String username);
}

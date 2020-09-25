package org.internship.system.security;

import org.internship.system.db.UsersTable;
import org.internship.system.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersTable usersTable;

    @Autowired
    public UserDetailsServiceImpl(UsersTable usersTable) {
        this.usersTable = usersTable;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersTable.getUserByUsername(username);
        return SecurityUser.fromUser(user);
    }
}

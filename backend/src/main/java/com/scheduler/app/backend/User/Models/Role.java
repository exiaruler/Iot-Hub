package com.scheduler.app.backend.User.Models;

import java.util.Set;

import com.scheduler.Base.ModelBase.ModelBase;

public class Role extends ModelBase {
    // role name
    private String role;
    // users assigned to this role
    private Set<User> users;
    

}

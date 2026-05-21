package com.scheduler.app.backend.User.Models;

import java.util.Set;

import com.scheduler.Base.ModelBase.ModelBase;

public class User extends ModelBase{
    // mongo user record id
    private String mongoId;
    // first name
    private String name;
    // last name
    private String lastName;
    // full name
    private String fullName=this.name+" "+this.lastName;
    // email
    private String email;
    // user timezone
    private String timezone;
    // active status
    private boolean active;
    // roles assigned
    private Set<Role> roles;
}

package com.scheduler.app.backend.User.Models;

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
    // status
    private boolean status;
    // 
    // role assigned
}

package com.scheduler.app.backend.Common.Models;

import java.time.Instant;

import com.scheduler.Base.ModelBase.ModelBase;
// log storage for entire system
public class Log extends ModelBase {

    // class name
    private String className;
    // id associated with that class
    private long parentId;
    // process
    private String process;
    // log
    private String log;
    // admin view
    private boolean adminView;
    // log expiry
    private boolean logExpiry;
    // log life of when to expire
    private long expireDuration;
    // expiry date time
    private Instant expiryDateTime;
    // server error log
    private boolean errorLog;
    // system entry log
    private boolean systemLog;


}

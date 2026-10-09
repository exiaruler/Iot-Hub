package com.scheduler.app.backend.Schedule.Models;

import javax.persistence.*;
import com.scheduler.Base.ModelBase.ModelBase;
import com.scheduler.app.backend.aREST.Models.Schedule;

public class NextSchedule extends ModelBase{
    // schedule order
    private int order;
    // schedule
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}

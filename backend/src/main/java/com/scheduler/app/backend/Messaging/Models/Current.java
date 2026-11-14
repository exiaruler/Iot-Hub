package com.scheduler.app.backend.Messaging.Models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import com.scheduler.Base.ModelBase.TaskModelBase;
// current
@MappedSuperclass
public class Current extends TaskModelBase {
    
    // current
    @Column
    private int current=0;
    // order of output
    @Column
    private int orderPosition;


    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getOrderPosition() {
        return this.orderPosition;
    }

    public void setOrderPosition(int orderPosition) {
        this.orderPosition = orderPosition;
    }


}

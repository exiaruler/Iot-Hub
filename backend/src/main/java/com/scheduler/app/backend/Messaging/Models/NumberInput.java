package com.scheduler.app.backend.Messaging.Models;

import com.scheduler.Base.ModelBase.TaskModelBase;
// long number input for other values
public class NumberInput extends TaskModelBase{
    // input
    //@ManyToOne
    //@JoinColumn(name="board_task_id")
    //@JsonBackReference("boardvariable-input")
    private BoardTask boardTaskInput;
    private long value;
    private int order;

}

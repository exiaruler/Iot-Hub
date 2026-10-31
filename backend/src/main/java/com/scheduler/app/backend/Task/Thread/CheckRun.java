package com.scheduler.app.backend.Task.Thread;

import java.util.List;

import com.scheduler.Base.ThreadBase.BaseThread;
import com.scheduler.app.backend.Task.Model.Task;

public class CheckRun extends BaseThread{
    private List <Task> tasks;

    public CheckRun(List<Task> tasks) {
        this.tasks = tasks;
    }
    
    @Override
    public void run(){
        checkRun(tasks);
    }
    public boolean checkRun(List<Task> running){
        boolean res=false;
        if(running.isEmpty()){
            res=true;
        }
        return res;
    }
}
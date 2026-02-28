package com.scheduler.app.backend.aREST.Repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.app.backend.aREST.Models.Task;
public interface TaskRepo extends JpaRepository<Task, TaskEventId>{
    @Query(value="select * from Task where active= ?1",
    nativeQuery=true)
    List<Task> getAllTaskAct(boolean active);

    @Query(value="Select * from Task where oneTimeJob=true and boardId=?1",nativeQuery = true)
    List<Task> getOneTimeJobs(long boardId);

    @Query(value="Select * from Task where active=true and boardId=?1",nativeQuery = true)
    List<Task> getRoutineJobs(long boardId);

}

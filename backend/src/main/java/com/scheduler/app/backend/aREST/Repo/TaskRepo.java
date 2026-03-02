package com.scheduler.app.backend.aREST.Repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.app.backend.aREST.Models.Task;
public interface TaskRepo extends JpaRepository<Task, TaskEventId>{
    @Query(value="select * from task where active= ?1",
    nativeQuery=true)
    List<Task> getAllTaskAct(boolean active);

    @Query(value="select * from task where one_time_job=true and board_id=?1",nativeQuery = true)
    List<Task> getOneTimeJobs(long boardId);

    @Query(value="select * from task where active=true and board_id=?1",nativeQuery = true)
    List<Task> getRoutineJobs(long boardId);

}

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

    @Query(value="select * from task right join schedule on task.schedule_id=schedule.id and schedule.status=true where board_id=?1",nativeQuery = true)
    List<Task> getRoutineJobsScheduled(long boardId);
    // get board tasks
    @Query(value ="select * from task right outer join schedule on schedule_device_id = task.device_id and task.schedule_id=schedule.id where task.device_id in (?1) order by scheduled_time desc",nativeQuery = true)
    List<Task> getBoardTasks(String ids);
    // get board routine tasks
     @Query(value ="select * from task right outer join schedule on schedule_device_id = task.device_id and task.schedule_id=schedule.id where task.device_id in (?1) and task.active=?2 ",nativeQuery = true)
    List<Task> getDeviceRoutineTasks(List<Long> ids,boolean active);

}

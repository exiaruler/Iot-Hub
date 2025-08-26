package com.scheduler.app.backend.aREST.Repo;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scheduler.app.backend.aREST.Models.Task;
public interface TaskRepo extends JpaRepository<Task, Long>{
    @Query(value="select * from scheduler.task where active= ?1",
    nativeQuery=true)
    List<Task> getAllTaskAct(boolean active);
    @Modifying
    @Transactional
    @Query(value = "update scheduler.task set retry= :tries where id= :id",nativeQuery = true)
    void updateAttempt(@Param("id")long id,@Param("tries")int tries);

    @Query(value="Select id from scheduler.task where oneTimeJob=true and board=?1",nativeQuery = true)
    List<Long> getOneTimeJobIds(long boardId);

    @Query(value="Select id from scheduler.task where active=true and board=?1",nativeQuery = true)
    List<Long> getRoutineJobIds(long boardId);

}

package com.scheduler.app.backend.aREST.Repo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.app.backend.aREST.Models.BoardQueue;

public interface BoardQueueRepo extends JpaRepository<BoardQueue,TaskEventId> {
    // get board operations by board
    @Query(value="Select *,ifnull(expired_date_time,next_occurance) as occurance from board_queue where board=?1 order by occurance asc,event_time desc",nativeQuery = true)
    public List<BoardQueue> getQueueByBoard(long id);
    // get board operations by board and device
    @Query(value="Select *,ifnull(expired_date_time,next_occurance) as occurance from board_queue where board=?1 and device=?2 order by occurance asc,event_time desc",nativeQuery = true)
    public List<BoardQueue> getQueueByDevice(long boardId,long deviceId);
    // remove expired
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value="delete from board_queue where expired_date_time <= ?1", nativeQuery = true)
    public void removeExpired(Instant dateTime);
    // get next board queue operation
    @Query(value="Select ifnull(expired_date_time,next_occurance) as next_update from board_queue where board=?1 order by next_update asc limit 1",nativeQuery = true)
    public Instant getNextQueueOperation(long boardId);
    // get queue by board and task id
    @Query(value="Select * from board_queue where board_id=?1 and board=?1 and board_task_id=?2 limit 1",nativeQuery = true)
    public BoardQueue getQueueByTaskId(long board,long taskId);
    // get event time
    @Query(value="select event_time from board_queue where board_id=?1 and board_task_id=2? and device_id=?3",nativeQuery = true)
    public Instant getEventTime(long boardId,long taskId,long deviceId);

}

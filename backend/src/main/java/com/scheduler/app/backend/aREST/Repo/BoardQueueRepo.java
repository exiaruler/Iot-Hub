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
    @Query(value="Select * from board_queue where board=?1 order by event_time desc",nativeQuery = true)
    public List<BoardQueue> getQueueByBoard(long id);
    // get board operations by board and device
    @Query(value="Select * from board_queue where board=?1 and device=?2 order by expired_date_time asc,event_time desc",nativeQuery = true)
    public List<BoardQueue> getQueueByDevice(long boardId,long deviceId);
    // remove expired
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value="delete from board_queue where expired_date_time <= ?1", nativeQuery = true)
    public void removeExpired(Instant dateTime);
    // get next board queue operation
    @Query(value="Select if(expired_date_time=null,event_time,expired_date_time) as next_update from board_queue where board=?1 order by next_update desc limit 1",nativeQuery = true)
    public Instant getNextQueueOperation(long boardId);


}

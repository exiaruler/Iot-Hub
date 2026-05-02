package com.scheduler.app.backend.aREST.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.app.backend.aREST.Models.BoardQueue;

public interface BoardQueueRepo extends JpaRepository<BoardQueue,TaskEventId> {
    // get board operations by board
    @Query(value="Select * from board_queue where board=?1",nativeQuery = true)
    public List<BoardQueue> getQueueByBoard(long id);
    // get board operations by board and device
    @Query(value="Select * from board_queue where board=?1 and device=?2",nativeQuery = true)
    public List<BoardQueue> getQueueByDevice(long boardId,long deviceId);
    


}

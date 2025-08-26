package com.scheduler.app.backend.aREST.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scheduler.app.backend.aREST.Models.Board;

public interface BoardRepo extends JpaRepository<Board, Long>{
    @Query(value="Select * from scheduler.board where  boardId=?1",nativeQuery = true)
    Board findBoardByBoardId(String i);

    @Query(value="Select * from scheduler.board where ip=:ip",nativeQuery = true)
    Board findBoardByIp(@Param("ip")String ip);

    
}

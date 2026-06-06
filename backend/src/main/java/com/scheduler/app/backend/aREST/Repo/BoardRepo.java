package com.scheduler.app.backend.aREST.Repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scheduler.app.backend.aREST.Models.Board;

public interface BoardRepo extends JpaRepository<Board, Long>{
    // get board by board id
    @Query(value="select * from board where board_id=?1 order by id limit 1",nativeQuery = true)
    Board findBoardByBoardId(String i);
    // get board by ip address
    @Query(value="select * from board where ip=:ip",nativeQuery = true)
    Board findBoardByIp(@Param("ip")String ip);

    // retrieve boards that were last connected under these specification
    @Query(value = "select * from board where TIMESTAMPDIFF(MINUTE,last_connect_date_time,NOW()) >= FLOOR(offline/60000)",nativeQuery = true)
    List<Board> getBoardsPassBy();
}

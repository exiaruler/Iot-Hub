package com.scheduler.app.backend.Messaging.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Messaging.Models.BoardPin;

public interface BoardPinInterface extends JpaRepository<BoardPin,Long>{

    @Query(value="select * from board_pin where board_task_id=?1 order by pin_order",nativeQuery = true)
    public List<BoardPin> getPinsByBoardTaskId(long id);
}

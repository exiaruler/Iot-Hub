package com.scheduler.app.backend.Messaging.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Messaging.Models.InputCurrent;

public interface InputCurrentInterface extends JpaRepository<InputCurrent, Long>{

    @Query(value="select * from input_current where board_task_id=?1 order by order_position",nativeQuery = true)
    public List<InputCurrent> getInputByBoardTaskId(long id);
}

package com.scheduler.app.backend.Messaging.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Messaging.Models.OutputCurrent;

public interface OutputCurrentInterface extends JpaRepository<OutputCurrent, Long>{

    @Query(value="SELECT * FROM scheduler.outputcurrent where board_task_id=?1 order by orderPosition",nativeQuery = true)
    public List<OutputCurrent> getOutputByBoardTaskId(long id);
}

package com.scheduler.app.backend.Messaging.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Messaging.Models.BoardTask;

public interface BoardTaskInterface extends JpaRepository<BoardTask, Long> {

    @Query(value="select task.* from Command as com,board_task as task where com.command=?1 and task.command_id=com.id and com.commandType=?2 and com.systemCommand=?3",nativeQuery = true)
    public BoardTask getBoardTaskByCommand(String commmand,String commandType,boolean systemCommand);

    @Query(value="select * from board_task where command_id=?1",nativeQuery = true)
    public BoardTask getBoardTaskByCommandId(long id);
}

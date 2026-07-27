package com.scheduler.app.backend.Messaging.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Messaging.Models.BoardTask;

public interface BoardTaskInterface extends JpaRepository<BoardTask, Long> {

    @Query(value="SELECT task.*\r\n" + //
                "FROM board_task AS task\r\n" + //
                "RIGHT JOIN command AS com\r\n" + //
                "    ON task.command_id = com.id\r\n" + //
                "WHERE com.command = ?1\r\n" + //
                "  AND com.command_type = ?2\r\n" + //
                "  AND com.system_command = ?3 limit 1",nativeQuery = true)
    public BoardTask getBoardTaskByCommand(String commmand,String commandType,boolean systemCommand);

    @Query(value="select * from board_task where command_id=?1",nativeQuery = true)
    public BoardTask getBoardTaskByCommandId(long id);
}

package com.scheduler.app.backend.Command.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Command.Models.Command;

public interface CommandRepo extends JpaRepository<Command, Long> {

    @Query(value="select * from command where command=?1 and class_name=?2",nativeQuery = true)
    Command findRouteByClass(String route,String className);

    @Query(value="select * from command where command_type=?1 and command=?2 and system_command=?3 ",nativeQuery = true)
    Command findCommand(String commandType,String command,boolean system);
    
    @Query(value="select * from command where system_command=?1",nativeQuery = true)
    List<Command>findCommandBySystem(boolean system);
}

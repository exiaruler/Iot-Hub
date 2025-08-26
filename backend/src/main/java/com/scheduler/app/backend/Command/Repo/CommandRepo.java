package com.scheduler.app.backend.Command.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Command.Models.Command;

public interface CommandRepo extends JpaRepository<Command, Long> {

    @Query(value="Select * from scheduler.command where command=?1 and className=?2",nativeQuery = true)
    Command findRouteByClass(String route,String className);

    @Query(value="Select * from scheduler.command where commandType=?1 and command=?2 and systemCommand=?3 ",nativeQuery = true)
    Command findCommand(String commandType,String command,boolean system);
    
    @Query(value="Select * from scheduler.command where systemCommand=?1",nativeQuery = true)
    List<Command>findCommandBySystem(boolean system);
}
package com.scheduler.app.backend.Command.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Command.Models.CommandParameter;

public interface CommandParameterRepo extends JpaRepository<CommandParameter,Long> {

    @Query(value="select * from CommandParameter where command_id=?1 and pin=true order by parameterOrder",nativeQuery = true)
    List<CommandParameter> findPinParameters(long id);

    @Query(value="select * from CommandParameter where command_id=?1 ",nativeQuery = true)
    List<CommandParameter> findParametersByCommand(long id);

    @Query(value="select * from CommandParameter where id=?1",nativeQuery = true)
    CommandParameter getParameter(long id);
}

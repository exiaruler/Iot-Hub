package com.scheduler.app.backend.Messaging.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.scheduler.Base.Base;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Messaging.Models.BoardPin;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;
import com.scheduler.app.backend.Messaging.Repo.BoardPinInterface;
import com.scheduler.app.backend.Messaging.Repo.BoardTaskInterface;
import com.scheduler.app.backend.Messaging.Repo.InputCurrentInterface;
import com.scheduler.app.backend.Messaging.Repo.OutputCurrentInterface;
@Service
public class BoardTaskService extends Base{

    public final BoardTaskInterface boardTaskRepo;
    public final BoardPinInterface boardPinRepo;
    public final InputCurrentInterface boardInputRepo;
    public final OutputCurrentInterface outputCurrentRepo;

    public BoardTaskService(BoardTaskInterface boardTaskRepo, InputCurrentInterface boardInputRepo, BoardPinInterface boardPinRepo, OutputCurrentInterface outputCurrentRepo){
        this.boardTaskRepo=boardTaskRepo;
        this.boardPinRepo = boardPinRepo;
        this.boardInputRepo = boardInputRepo;
        this.outputCurrentRepo = outputCurrentRepo;
    }
    public BoardTask getBoardTask(long id){
        return boardTaskRepo.findById(id).get();
    }
    public void deleteBoardTask(long id){
        if(boardTaskRepo.existsById(id)){
            boardTaskRepo.deleteById(id);
        }
    }
    public BoardTask addBoardTask(BoardTask task){
        return boardTaskRepo.save(task);
    }
    public BoardTask getTask(String comm,String type,boolean system){
        BoardTask tsk=boardTaskRepo.getBoardTaskByCommand(comm, type, system);
        return tsk;
    }
    // get board task by command id
    public BoardTask getTaskByCommandId(long id){
        return boardTaskRepo.getBoardTaskByCommandId(id);
    }
    public List<BoardPin> getPins(long id){
        List<BoardPin> res=new ArrayList<>();
        List<BoardPin> query=boardPinRepo.getPinsByBoardTaskId(id);
        if(query.size()>0) res=query;
        return res;
    }
    public List<OutputCurrent> getOuputs(long id){
        List<OutputCurrent> res=new ArrayList<>();
        List<OutputCurrent> query=outputCurrentRepo.getOutputByBoardTaskId(id);
        if(query.size()>0) res=query;
        return res;
    }
    public List<InputCurrent> getInputs(long id){
        List<InputCurrent> res=new ArrayList<>();
        List<InputCurrent> query=boardInputRepo.getInputByBoardTaskId(id);
        if(query.size()>0) res=query;
        return res;
    }
    
}

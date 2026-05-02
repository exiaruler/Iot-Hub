package com.scheduler.app.backend.aREST.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.scheduler.Base.Base;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.BoardQueue;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Task;
import com.scheduler.app.backend.aREST.Repo.BoardQueueRepo;

@Service
public class BoardQueueService extends Base{

    public final BoardQueueRepo boardQueueRepo;

    public BoardQueueService(BoardQueueRepo boardQueueRepo) {
        this.boardQueueRepo = boardQueueRepo;
    }
    // add to queue through BoardTask
    public BoardQueue addToQueueBoardTask(BoardTask task,Board board,Device device){
        BoardQueue rec=null;
        if(task.getTask().equals("schedule")){
            Boolean taskRepeat=false;
            Instant expiry=null;
            String jsonStr="";
            try {
                jsonStr=messageUtil.objectToJsonString(task);
            } catch (Exception e) {
                jsonStr="";
            }
            
            if(task.getRunTarget()==0){
                taskRepeat=true;
            }else
            {
                // calculate expiry
                expiry=calculateExpiry(task.getDelayInterval());
            }
            rec=new BoardQueue(board, device, task.getTaskId(), taskName(task.getMethod()), task.getSystemTask(), true, true, task.getSystemTask(), taskRepeat, false, expiry, jsonStr, null, null);
            rec=boardQueueRepo.save(rec);
        }
        return rec;
    }
    // add to queue through task
    public BoardQueue addToQueueTask(Task task){
        BoardQueue rec=null;
        if(task.getSystemTask()&&!task.getBoardTaskJson().equals("")){

        }
        return rec;
    }
    public List<BoardQueue> getQueues(String boardId,String deviceId){
        List<BoardQueue> queue=new ArrayList<>();
        if(boardId!=""){
            long id=getDataLong("select id from board where board_id="+quoteParam(boardId));
            if(id>-1){
                queue=boardQueueRepo.getQueueByBoard(id);
            }
        }else 
        if(boardId!=""&&deviceId!=""){
            long id=getDataLong("select id from board where board_id="+quoteParam(boardId));
            long devId=getDataLong("select id from board where device_id="+quoteParam(deviceId));
            if(id>-1&&devId>-1){
                queue=boardQueueRepo.getQueueByDevice(id, devId);
            }
        }
        return queue;
    }

    private Instant calculateExpiry(long delay){
        Instant curr=Instant.now();
        curr.plusMillis(delay);
        return curr;
    }
    private String taskName(String method){
        return "Task Process: "+ method;
    }

}

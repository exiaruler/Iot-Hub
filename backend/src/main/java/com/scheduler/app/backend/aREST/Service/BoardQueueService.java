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
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.Task;
import com.scheduler.app.backend.aREST.Repo.BoardQueueRepo;
import com.scheduler.app.backend.aREST.Repo.BoardRepo;

@Service
public class BoardQueueService extends Base{

    public final BoardQueueRepo boardQueueRepo;
    public final BoardRepo boardRepo;

    public BoardQueueService(BoardQueueRepo boardQueueRepo, BoardRepo boardRepo) {
        this.boardQueueRepo = boardQueueRepo;
        this.boardRepo = boardRepo;
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
            rec=new BoardQueue(board, device, task.getTaskId(), taskName(task.getMethod()), task.getSystemTask(), true, true, task.getSystemTask(), taskRepeat, task.getDelayInterval(), false, expiry, jsonStr, null, null);
            rec=boardQueueRepo.save(rec);
        }
        return rec;
    }
    // add to queue through task
    public BoardQueue addToQueueTask(Task task){
        BoardQueue rec=null;
        BoardTask boardTask=null;
        // handle system task
        if(task.getSystemTask()&&!task.getBoardTaskJson().equals("")){
            String jsonStr=task.getBoardTaskJson();
            Board board=boardRepo.findById(task.getBoard()).orElse(null);
            Device device=board.getDevice().stream().filter(dev->dev.getId()==task.getDeviceId()).findFirst().orElse(null);
            try {
                boardTask=messageUtil.stringToObject(jsonStr);
                if(boardTask!=null&&boardTask.getTask().equals("schedule")){
                    Boolean taskRepeat=false;
                    Instant expiry=null;
                    if(boardTask.getRunTarget()==0){
                        taskRepeat=true;
                    }else
                    {
                        // calculate expiry
                        expiry=calculateExpiry(boardTask.getDelayInterval());
                    }
                    rec=new BoardQueue(board, device, boardTask.getTaskId(), taskName(boardTask.getMethod()), boardTask.getSystemTask(), true, true, boardTask.getSystemTask(), taskRepeat, boardTask.getDelayInterval(), false, expiry, jsonStr, null, null);
                    rec=boardQueueRepo.save(rec);
                    
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        // handle non system task
        }else if(!task.getSystemTask()&&task.getSchedule()!=null){
            Board board=boardRepo.findById(task.getBoard()).orElse(null);
            Device device=board.getDevice().stream().filter(dev->dev.getId()==task.getDeviceId()).findFirst().orElse(null);
            if(board!=null&&device!=null){
                Route rou=device.getRoutes().stream().filter(r->r.getId()==task.getRouteId()).findFirst().orElse(null);
                Mode mode=rou.getMode().stream().filter(m->m.getId()==task.getModeId()).findFirst().orElse(null);
                if(mode!=null){
                    boardTask=mode.getBoardAction();
                }
                if(boardTask.getTask().equals("schedule")){
                    Boolean taskRepeat=false;
                    Instant expiry=null;
                    String jsonStr="";
                    try {
                        jsonStr=messageUtil.objectToJsonString(boardTask);
                    } catch (Exception e) {
                        jsonStr="";
                    }
                    
                    if(boardTask.getRunTarget()==0){
                        taskRepeat=true;
                    }else
                    {
                        // calculate expiry
                        expiry=calculateExpiry(boardTask.getDelayInterval());
                    }
                    rec=new BoardQueue(board, device, boardTask.getTaskId(), taskName(boardTask.getMethod()), boardTask.getSystemTask(), true, true, boardTask.getSystemTask(), taskRepeat, boardTask.getDelayInterval(), false, expiry, jsonStr, null, null);
                    rec=boardQueueRepo.save(rec);
                }
            }
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

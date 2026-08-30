package com.scheduler.app.backend.aREST.Controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.scheduler.Base.ControllerBase;
import com.scheduler.Base.Service.ControllerBaseService;
import com.scheduler.app.backend.Hardware.Models.Hardware;
import com.scheduler.app.backend.Messaging.Board.Models.BoardLogin;
import com.scheduler.app.backend.Messaging.Board.Models.BoardRegister;
import com.scheduler.app.backend.Messaging.Board.Models.DeviceCheck;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Service.BoardService;


@RestController
@RequestMapping(value = "/board")
public class BoardController extends ControllerBaseService<Long,Board>{
    @Autowired
    private BoardService service;

    public BoardController() {
        this.objectClass=this.pathBase+".aREST.Models.Board";
    }
    
    @PostMapping(value="/add-board-socket", consumes = "application/json")
    public ResponseEntity<Board> addBoard(@RequestBody Board input) {
        Board boardSave=service.addBoardSocket(input.getName(),input.getHardwardId(),input.getBoardId());
        return ResponseEntity.ok(boardSave);
    }
    @PutMapping(value="/update-board/{id}", consumes = {"application/xml","application/json"})
    public ResponseEntity<Board> updateBoard(@RequestBody Board board,@PathVariable long id){
        Board update=service.updateBoard(board, id);
        if(update==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(board);
        
    }
    @GetMapping(value="/getboards")
    public ResponseEntity<List<Board>> all(){
        return ResponseEntity.ok(service.getBoards());
    }
 
    @GetMapping(value="/get-board-id/{id}")
    public ResponseEntity<Board> getBoardId(@PathVariable String id){
        Board board=service.getBoardByBoardId(id);
        if(board!=null){
            return ResponseEntity.ok(board);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(value="/get-board-hardware/{id}")
    public ResponseEntity<Hardware> getBoardHardware(@PathVariable String id){
        Hardware hardware=service.getBoardHardwareId(id);
        if(hardware!=null){
            return ResponseEntity.ok(hardware);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable long id){
        String result="";
        result=service.deleteBoard(id);
        return ResponseEntity.ok(result);
    }
    // board routes
    // routine status check by http request
    @GetMapping(value="/status-check/{id}")
    public ResponseEntity<DeviceCheck> routineCheck(@RequestHeader("ram-usage")String ram,@RequestHeader("ip")String ip,@RequestHeader("free-heap")String heap,@RequestHeader("millis")String millis,@RequestHeader("sys-task-tot")String systemTotalTask,@RequestHeader("task-tot")String taskTotal,@RequestHeader("queue-tot")String totalQueue,@PathVariable long id,@RequestParam(name="tid",defaultValue ="0")long tid){
        DeviceCheck check=service.routineCheck(id,Integer.parseInt(ram),ip,Integer.parseInt(heap),Long.parseLong(millis),tid);
        return ResponseEntity.ok(check);
    }

    // get latest firmware update
    @GetMapping(value="/get-update/{id}")
    public ResponseEntity<StreamingResponseBody> getUpdate(@RequestHeader("ram-usage")String ram,@RequestHeader("ip")String ip,@RequestHeader("free-heap")String heap,@RequestHeader("millis")String millis,@RequestHeader("sys-task-tot")String systemTotalTask,@RequestHeader("task-tot")String taskTotal,@RequestHeader("queue-tot")String totalQueue,@PathVariable long id) {
        return service.getUpdate(id);
    }
    // when board starts-up verify credentials
    @PostMapping("/startup")
    public ResponseEntity<BoardLogin> startup(@RequestBody BoardRegister entity,@RequestHeader("ram-usage")String ram,@RequestHeader("ip")String ip,@RequestHeader("SSID")String ssid,@RequestHeader("mac-address")String macAddress,@RequestHeader("free-heap")String freeHeap,@RequestHeader("heap")String heap,@RequestHeader("sys-task-tot")String systemTotalTask,@RequestHeader("task-tot")String taskTotal,@RequestHeader("queue-tot")String totalQueue,@RequestHeader("version")String version,@RequestHeader("millis")String millis) {
        BoardLogin check=service.startup(entity,ip,Integer.parseInt(ram),ssid,macAddress,Integer.parseInt(freeHeap),Integer.parseInt(heap),Integer.parseInt(systemTotalTask),Integer.parseInt(taskTotal),Integer.parseInt(totalQueue),Long.parseLong(millis),version);
        if(check!=null){
            return ResponseEntity.ok(check);
        }
        return ResponseEntity.status(401).body(check);
    }
    
    
    

    

}

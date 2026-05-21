package com.scheduler.app.backend.aREST.Controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.scheduler.Base.ControllerBase;
import com.scheduler.app.backend.Hardware.Models.Hardware;
import com.scheduler.app.backend.Messaging.Board.Models.BoardRegister;
import com.scheduler.app.backend.Messaging.Board.Models.DeviceCheck;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Service.BoardService;


@RestController
@RequestMapping(value = "/board")
public class BoardController extends ControllerBase{
    @Autowired
    private BoardService boardService;

    public BoardController() {
        this.objectClass=this.pathBase+".aREST.Models.Board";
    }
    
    @PostMapping(value="/add-board-socket", consumes = "application/json")
    public ResponseEntity<Board> addBoard(@RequestBody Board input) {
        Board boardSave=boardService.addBoardSocket(input.getName(),input.getHardwardId(),input.getBoardId());
        return ResponseEntity.ok(boardSave);
    }
    @PutMapping(value="/update-board/{id}", consumes = {"application/xml","application/json"})
    public ResponseEntity<Board> updateBoard(@RequestBody Board board,@PathVariable long id){
        Board update=boardService.updateBoard(board, id);
        if(update==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(update);
        
    }
    @GetMapping(value="/getboards")
    public ResponseEntity<List<Board>> all(){
        return ResponseEntity.ok(boardService.getBoards());
    }
    @GetMapping(value="/getboard/{id}")
    public ResponseEntity<Optional<Board>> getBoard(@PathVariable long id){
        Optional<Board> board=boardService.findBoard(id);
        if(board!=null){
            return ResponseEntity.ok(board);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(value="/get-board-id/{id}")
    public ResponseEntity<Board> getBoardId(@PathVariable String id){
        Board board=boardService.getBoardByBoardId(id);
        if(board!=null){
            return ResponseEntity.ok(board);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(value="/get-board-hardware/{id}")
    public ResponseEntity<Hardware> getBoardHardware(@PathVariable String id){
        Hardware hardware=boardService.getBoardHardwareId(id);
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
        result=boardService.deleteBoard(id);
        return ResponseEntity.ok(result);
    }
    // board routes
    // routine status check by http request
    @GetMapping(value="/status-check/{id}")
    public ResponseEntity<DeviceCheck> routineCheck(@RequestHeader("ram-usage")String ram,@RequestHeader("ip")String ip,@PathVariable long id){
        //System.out.println("Connection check "+id+" "+LocalTime.now()+" "+ram+" "+ip);
        DeviceCheck check=boardService.routineCheck(id,Integer.parseInt(ram),ip);
        return ResponseEntity.ok(check);
    }
    // when board starts-up verify credentials
    @PostMapping("/startup")
    public ResponseEntity<DeviceCheck> startup(@RequestBody BoardRegister entity,@RequestHeader("ram-usage")String ram,@RequestHeader("ip")String ip,@RequestHeader("SSID")String ssid,@RequestHeader("mac-address")String macAddress) {
        //System.out.println("startup "+ entity.getBoardId());
        DeviceCheck check=boardService.startup(entity,ip,Integer.parseInt(ram),ssid,macAddress);
        if(check!=null){
            return ResponseEntity.ok(check);
        }
        return ResponseEntity.status(401).body(check);
    }
    
    
    

    

}

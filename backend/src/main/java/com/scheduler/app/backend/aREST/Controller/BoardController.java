package com.scheduler.app.backend.aREST.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.ControllerBase;
import com.scheduler.app.backend.Messaging.Board.Models.BoardRegister;
import com.scheduler.app.backend.Messaging.Board.Models.DeviceCheck;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Service.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.scheduler.app.backend.InterfaceModels.Input.BoardInput;
import org.springframework.web.bind.annotation.RequestParam;
import com.scheduler.app.backend.Hardware.Models.Hardware;


@RestController
@RequestMapping(value = "/board")
public class BoardController extends ControllerBase{
    @Autowired
    private BoardService boardService;

    public BoardController() {
        this.objectClass=this.pathBase+".InterfaceModels.Input.BoardInput";
    }
    
    @PostMapping(value="/addboard")
    @ResponseBody
    public Board addBoardTest(@RequestBody Map<String, Object> payload){
        String name=(String)payload.get("name");
        String ip=(String) payload.get("ip");
        boolean arest=(boolean) payload.get("aRest");
        boolean status=(boolean) payload.get("status");
        return boardService.addBoardManual(name, ip, arest, status);
    }
    @PostMapping(value="/add-board-socket", consumes = "application/json")
    public ResponseEntity<Board> addBoard(@RequestBody BoardInput input) {
        Board boardSave=boardService.addBoardSocket(input.getName(),input.getHardwareModel());
        return ResponseEntity.ok(boardSave);
    }
    @PutMapping(value="/update-board/{id}", consumes = {"application/xml","application/json"})
    public ResponseEntity<Board> updateBoard(@RequestBody Board board,@PathVariable long id){
        Board a=boardService.updateBoard(board, id);
        return ResponseEntity.ok(a);
        
    }
    
    @PostMapping(value="/addboardscan")
    public ArrayList<Board> addBoardScan(){
        ArrayList<Board> addedList=new ArrayList<Board>();
        addedList=boardService.scanNewBoards();
        return addedList;
    }
    @PostMapping(value="/addboardip")
    public Board addBoardByIP(@RequestBody String ip){
        return boardService.addBoardByIp(ip);
    }
    
    @GetMapping(value="/getboards")
    public List<Board> all(){
        return boardService.getBoards();
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
        System.out.println("Connection check "+id+" "+LocalTime.now()+" "+ram+" "+ip);
        DeviceCheck check=boardService.routineCheck(id,Integer.parseInt(ram),ip);
        return ResponseEntity.ok(check);
    }
    // when board starts-up verify credentials
    @PostMapping("/startup")
    public ResponseEntity<DeviceCheck> startup(@RequestBody BoardRegister entity,@RequestHeader("ram-usage")String ram,@RequestHeader("ip")String ip) {
        DeviceCheck check=boardService.startup(entity,ip,Integer.parseInt(ram));
        if(check!=null){
            ResponseEntity.ok(check);
        }
        return ResponseEntity.ok(check);
    }
    
    
    

    

}

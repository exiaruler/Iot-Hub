package com.scheduler.app.backend.aREST.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.scheduler.Base.Base;
import com.scheduler.Base.Exception.ValidationException;
import com.scheduler.app.backend.Task.Model.CompletedTask;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Repo.BoardRepo;
import com.scheduler.app.backend.aREST.Repo.DeviceRepo;

@Service
public class DeviceService extends Base {
    private final DeviceRepo deviceRepo;
    private final RoutesService routesService;
    private final BoardRepo boardRepo;

    public DeviceService(DeviceRepo deviceRepo, RoutesService routesService, BoardRepo boardRepo) {
        this.deviceRepo = deviceRepo;
        this.routesService = routesService;
        this.boardRepo = boardRepo;
    }
    
    public Device addDeviceSocket(Device entry,long boardId){
        Board board=boardRepo.getReferenceById(boardId);
        String entryName=entry.getName();
        long devExist=getDataInt("select count(name) from device where board_id="+board.getId()+" and name="+quoteParam(entryName));
        if(board!=null && devExist == 0){
            entry.setBoard(board);
            Device save=deviceRepo.save(entry);
            String deviceId=board.getBoardId()+save.getId();
            save.setDeviceId(deviceId);
            save = deviceRepo.save(entry);
            entry = save;
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("name", "Device with name already exists on this board");
            throw new ValidationException(errors,null);
        }
        return entry;
    }
    public Device updateDeviceSocket(Device entry,long id){
        Device device=deviceRepo.findById(id).get();
        if(device!=null){
            Map<String, String> errors = new HashMap<>();
            if(entry.getName() != null && !entry.getName().isEmpty()){
                int devExist=getDataInt("select count(id) from device where name="+quoteParam(entry.getName())+" and board_id="+device.getBoard().getId());
                if(devExist>0){
                    errors.put("name", "Device with name already exists on this board");
                }
            }
            if(!errors.isEmpty()){
                throw new ValidationException(errors,null);
            }

            device.setName(entry.getName());
            device=deviceRepo.save(device);
        }
        return device;
    }
  
    public Device updateDevice(long id,Device deviceObj){
        Device updateDev=deviceRepo.getReferenceById(id);
        if(updateDev!=null){
            updateDev=deviceObj;
            deviceRepo.save(updateDev);
        }
        return updateDev;
    }
    public List<Device> getAllDevice(){
        return deviceRepo.findAll();
    }
    public List<Device> getAllDevicesWithRoutes(){
        return deviceRepo.getDevicesByRoutes();
    }
    public long [] getDevicesById(long id){
        return deviceRepo.findDevicesByBoard(id);
    }
    public Device getDevice(long id){
        Device device=deviceRepo.findById(id).get();
        return device;
    }
    public String deleteDevice(long id){
        String res="";
        Device device=deviceRepo.getReferenceById(id);
        if(device!=null){
            deviceRepo.deleteById(id);
            res=device.getName()+" is deleted";
        }else res="Device does not exist";
        return res;
    }
    public void deleteAllBoard(){
        deviceRepo.deleteAll();
    }
    // update device after http request
    public void updateDeviceAfterAction(CompletedTask task,Device deviceUpdate){
        
       String state="";
       String warning="";
       if(deviceUpdate!=null){
        // update device state and warning
        if(task.getWarning()!=""&&!task.getStatus()){
            warning=task.getWarning();
        }else{
            if(task.getStatusString()!=""){
                state=task.getStatusString();
            }else{
                state="offline";
            }
        }
        try {
            deviceRepo.updateStateAndWarning(task.getDevice().getId(),state,warning);
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println(state);
       }

    }
    
}


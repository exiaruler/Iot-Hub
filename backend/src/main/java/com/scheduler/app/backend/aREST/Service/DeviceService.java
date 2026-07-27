package com.scheduler.app.backend.aREST.Service;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.scheduler.Base.Exception.ValidationException;
import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.Task.Model.CompletedTask;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Repo.BoardRepo;
import com.scheduler.app.backend.aREST.Repo.DeviceRepo;

@Service
public class DeviceService extends BaseService<Device, Long> {
    @Autowired
    private DeviceRepo deviceRepo;
    public final RoutesService routesService;
    private final BoardRepo boardRepo;

    public DeviceService(DeviceRepo deviceRepo, RoutesService routesService, BoardRepo boardRepo) {
        this.deviceRepo = deviceRepo;
        this.routesService = routesService;
        this.boardRepo = boardRepo;
    }
    @Override
    protected JpaRepository<Device, Long> repository() {
        return deviceRepo;
    }
     @Override
    protected void beforeSave(Device entity, Map<String, String> errors, Map<String, String> warnings) {
        if (entity.getName() != null) {
            entity.setName(entity.getName().trim());
        }

        boolean existingDevice = entity.getId() > 0 && deviceRepo.existsById(entity.getId());
        Board board = entity.getBoard();

        if (existingDevice) {
            Device persistedDevice = deviceRepo.getReferenceById(entity.getId());
            board = persistedDevice.getBoard();
            entity.setBoard(board);
            entity.setDeviceId(persistedDevice.getDeviceId());
        }

        if (board == null) {
            errors.put("board", "Device must belong to a board");
        } else if (entity.getName() != null && !entity.getName().isEmpty()) {
            long duplicateCount = getDataInt("select count(id) from device where board_id="
                    + board.getId() + " and name=" + quoteParam(entity.getName())
                    + " and id<>" + entity.getId());
            if (duplicateCount > 0) {
                errors.put("name", "Device with name already exists on this board");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors, null);
        }
    }

    @Override
    protected void afterSave(Device entity) {
        if (entity.getDeviceId() == null || entity.getDeviceId().isEmpty()) {
            entity.setDeviceId(entity.getBoard().getBoardId() + entity.getId());
            deviceRepo.save(entity);
        }
    }

    public Device addDeviceSocket(Device entry,long boardId){
        Board board=boardRepo.getReferenceById(boardId);
        entry.setBoard(board);
        return save(entry);
    }
    public Device updateDeviceSocket(Device entry,long id){
        if (!deviceRepo.existsById(id)) {
            return null;
        }
        entry.setId(id);
        return save(entry);
    }
    /*
    public Device updateDevice(long id,Device deviceObj){
        Device updateDev=deviceRepo.getReferenceById(id);
        if(updateDev!=null){
            updateDev=deviceObj;
            deviceRepo.save(updateDev);
        }
        return updateDev;
    }
    */
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


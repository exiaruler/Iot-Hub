package com.scheduler.app.backend.aREST.Service;
import java.util.*;

import javax.management.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.scheduler.Base.Base;
import com.scheduler.Base.JsonObject.JsonObject;
import com.scheduler.app.backend.Task.Model.CompletedTask;
import com.scheduler.app.backend.aREST.ArestV2Frame;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.ScanDevice;
import com.scheduler.app.backend.aREST.Repo.BoardRepo;
import com.scheduler.app.backend.aREST.Repo.DeviceRepo;

@Service
public class DeviceService extends Base {
    private final DeviceRepo deviceRepo;
    private final RoutesService routesService;
    public ArestV2Frame arest=new ArestV2Frame();
    private final BoardRepo boardRepo;

    public DeviceService(DeviceRepo deviceRepo, RoutesService routesService, BoardRepo boardRepo) {
        this.deviceRepo = deviceRepo;
        this.routesService = routesService;
        this.boardRepo = boardRepo;
    }
    
    public Device addDevice(Device entry){
        return deviceRepo.save(entry);
    }
    public Device addDeviceSocket(Device entry,long boardId){
        Board board=boardRepo.getReferenceById(boardId);
        if(board!=null){
            entry.setBoard(board);
            Device save=deviceRepo.save(entry);
            String deviceId=board.getBoardId()+save.getId();
            save.setDeviceId(deviceId);
            save=deviceRepo.save(entry);
            entry=save;
        }
        return entry;
    }
    public Device saveDeviceManual(long board,String name,String type,String subtype,boolean framework,boolean custom){
        Device newDev=new Device();
        Board boardObj=boardRepo.getReferenceById(board);
        if(boardObj!=null){
            List<Device>deviceList=boardObj.getDevice();
            deviceList.add(newDev);
            boardObj.setDevice(deviceList);
            newDev.setBoard(boardObj);
            newDev.setName(name);
            newDev.setType(type);
            newDev.setSubtype(subtype);
            newDev.setFrameworkFollowed(framework);
            newDev.setCustom(custom);
            deviceRepo.save(newDev);
        }
        return newDev;
    }
    public Device updateDevice(long id,Device deviceObj){
        Device updateDev=deviceRepo.getReferenceById(id);
        if(updateDev!=null){
            updateDev=deviceObj;
            deviceRepo.save(updateDev);
        }
        return updateDev;
    }
    public List<Device> addDeviceFromScan(Board board,String ip,JsonObject jsonObj){
        List<Device> deviceList=new ArrayList<Device>();
        if(arest.testDeviceFramework(jsonObj, ip)){
            String device=jsonObj.findKeyValue("Devices");
            if(device.indexOf("|")>-1){
                String[] deviceArr=device.split("\\|");
                for(int i=0; i<deviceArr.length; i++){
                    String deviceName=deviceArr[i];
                    Device newDevice=new Device();
                    Device exist=deviceRepo.findExistingDevice(board.getId(), deviceName);
                    if(exist!=null) newDevice=exist;
                    newDevice.setFrameworkFollowed(true);
                    newDevice.setBoard(board);
                    newDevice.setName(deviceName);
                    // saves routes
                    List<Route> route=routesService.addRoutesByScan(newDevice,ip);
                    newDevice.setRoutes(route);
                    deviceList.add(newDevice);
                }
            }else{
                String deviceName=device.trim();
                try{
                    httpUtil.getRoutes(ip, deviceName);
                }catch(Exception err){

                }
                Device newDevice=new Device();
                Device exist=deviceRepo.findExistingDevice(board.getId(), deviceName);
                if(exist!=null) newDevice=exist;
                newDevice.setBoard(board);
                newDevice.setName(deviceName);
                Device save=addDevice(newDevice);
                save.setRoutes(routesService.addRoutesByScan(save,ip));
                save=addDevice(save);
                deviceList.add(save);
            }  
        }
        return deviceList;
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


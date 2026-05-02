package com.scheduler.app.backend.aREST.Service;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scheduler.Base.Base;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Messaging.Models.BoardPin;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Repo.DeviceRepo;
import com.scheduler.app.backend.aREST.Repo.ModeRepo;
import com.scheduler.app.backend.aREST.Repo.RoutesRepo;
// Functio and modes
@Service
public class RoutesService extends Base {
    private final RoutesRepo routeRepo;
    private final ModeRepo modeService;
    private final DeviceRepo deviceRepo;
    private final CommandService commandService;
    private final ParameterService parameterService;
    public RoutesService(RoutesRepo routeRepo, ModeRepo modeService, DeviceRepo deviceRepo, CommandService commandService, ParameterService parameterService) {
        this.routeRepo = routeRepo;
        this.modeService = modeService;
        this.deviceRepo = deviceRepo;
        this.commandService = commandService;
        this.parameterService = parameterService;
    }
   
    // add route and mode socket
    public Route addRouteandModes(Route route,String deviceId){
        if(deviceId!=""){
            Device dev=deviceRepo.findDeviceByDeviceId(deviceId);
            Command com=commandService.getCommand(route.getCommandId());
            if(dev!=null&&com!=null){
                route.setDevice(dev);
                route.setCommand(com);
                if(route.getMode().size()>0){
                    route.setModes(true);
                }
                // set id to 0
                List<Mode> modeList=route.getMode();
                for(Mode mode:modeList){
                    mode.getBoardAction().newInputs();
                }
                route.setMode(modeList);
                //route.calculateCurrent();
                Route save=routeRepo.save(route);
                route=save;
            }
        }
        return route;
    }
    
    public Route updateRoute(Route entry,Long id){
        Route rec=null;
        if(routeRepo.existsById(id)){
            rec=routeRepo.findById(id).get();
            Command com=commandService.getCommand(entry.getCommandId());
            if(com!=null&&rec.getCommand().getId()==com.getId()){
                rec.setRoute(entry.getRoute());
                rec.setElectrode(entry.getElectrode());
                if(entry.getMode().size()>0){
                    rec.setModes(true);
                }
                rec.getMode().removeIf(exist->entry.getMode().stream().noneMatch(m->m.getId()==exist.getId()));
                
                List<Mode> existModeList=rec.getMode();
                // updated list
                List<Mode> updatedModeList=entry.getMode();
                for(Mode mode:updatedModeList){
                    Mode ex=existModeList.stream().filter(m->m.getId()==mode.getId()).findFirst().orElse(null);
                    // update existing mode
                    if(ex!=null){
                        ex.setMode(mode.getMode());
                        BoardTask act=mode.getBoardAction();
                        act.setMode(ex);
                        ex.setBoardAction(act);
                    }
                    else{
                        mode.setRoute(rec);
                        mode.getBoardAction().newInputs();
                        rec.getMode().add(mode);
                    }    
                }
                
            }else
            {
                rec.setCommand(com);
                rec.setCommandId(com.getId());
                rec.getMode().clear();
            }
            rec.calculateCurrent();
            rec=routeRepo.save(rec);
        }
        return rec;
    }
    public void deleteRoute(long id){
        routeRepo.deleteById(id);
    }

    // routes
    public List<Route> getAllRoutes(){
        return routeRepo.findAll();
    }
    public Route getRouteById(Long id){
        Route rou=routeRepo.findById(id).get();
        return rou;
    }
    public Route getRouteByIdDisplay(Long id){
        Route rou=routeRepo.findById(id).get();
        rou.calculateOutputDisplay();
        return rou;
    }
    // modes
    public List<Mode> getAllModes(){
        return modeService.findAll();
    }
    public List<Mode> getModesRouteId(long id){
        return modeService.getModesByRouteId(id);
    }
    @Async
    @Transactional
    public Mode getMode(long id){
        return modeService.findById(id).get();
    }
    

    
}

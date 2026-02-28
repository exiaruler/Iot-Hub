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
                for(int i=0; i<modeList.size(); i++){
                    BoardTask tsk=modeList.get(i).getBoardAction();
                    int pinOrder=1,inCurr=1,outCurr=1;
                    for(int p=0; p<tsk.getPins().size(); p++){
                        BoardPin pin=tsk.getPins().get(p);
                        pin.setId(0);
                        pin.setBoardTask(tsk);
                        pin.setPinOrder(pinOrder);
                        tsk.getPins().set(p,pin);
                        pinOrder++;
                    }
                    tsk.setId(0);
                    // calculate current
                    if(tsk.getInput().size()==tsk.getOutput().size()){
                        int totLength=tsk.getOutput().size();
                        for(int x=0; x<totLength; x++){
                            InputCurrent in=tsk.getInput().get(x);
                            in.setId(0);
                            in.setOrderPosition(inCurr);
                            in.setBoardTaskInput(tsk);
                            if(route.getElectrode()=="anode") in.setCurrent(currentAnodeCalculate(in.getCurrent()));
                            tsk.getInput().set(x, in);
                            OutputCurrent out=tsk.getOutput().get(x);
                            out.setOrderPosition(outCurr);
                            out.setId(0);
                            if(route.getElectrode()=="anode") out.setCurrent(currentAnodeCalculate(out.getCurrent()));
                            tsk.getOutput().set(x,out);
                            inCurr++;
                            outCurr++;
                        }
                    }else
                    {
                        if(tsk.getInput().size()>0){
                            //currentAnodeCalculate
                            for(int a=0; a<tsk.getInput().size(); a++){
                                InputCurrent in=tsk.getInput().get(a);
                                in.setId(0);
                                in.setOrderPosition(inCurr);
                                in.setBoardTaskInput(tsk);
                                if(route.getElectrode()=="anode"){
                                    in.setCurrent(currentAnodeCalculate(in.getCurrent()));
                                }
                                tsk.getInput().set(a, in);
                                inCurr++;
                            }
                        }
                        if(tsk.getOutput().size()>0){
                            for(int b=0; b<tsk.getOutput().size(); b++){
                                OutputCurrent out=tsk.getOutput().get(b);
                                out.setId(0);
                                out.setOrderPosition(outCurr);
                                out.setBoardTaskOutput(tsk);
                                if(route.getElectrode()=="anode"){
                                    out.setCurrent(currentAnodeCalculate(out.getCurrent()));
                                }
                                tsk.getOutput().set(b,out);
                                outCurr++;
                            }
                        }
                    }
                    modeList.get(i).setBoardAction(tsk);
                    
                }
            route.setMode(modeList);
            route.calculateCurrent();
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
            Device dev=deviceRepo.findDeviceByDeviceId(rec.getDevice().getDeviceId());
            Command com=commandService.getCommand(entry.getCommandId());
            if(dev!=null&&com!=null){
                rec.setDevice(dev);
                rec.setCommand(com);
                if(rec.getMode().size()>0){
                    rec.setModes(true);
                }
                // set id to 0
                List<Mode> modeList=rec.getMode();
                for(int i=0; i<modeList.size(); i++){
                    BoardTask tsk=modeList.get(i).getBoardAction();
                    int pinOrder=1,inCurr=1,outCurr=1;
                    for(int p=0; p<tsk.getPins().size(); p++){
                        BoardPin pin=tsk.getPins().get(p);
                        pin.setId(0);
                        pin.setBoardTask(tsk);
                        pin.setPinOrder(pinOrder);
                        tsk.getPins().set(p,pin);
                        pinOrder++;
                    }
                    tsk.setId(0);
                    // calculate current
                    if(tsk.getInput().size()==tsk.getOutput().size()){
                        int totLength=tsk.getOutput().size();
                        for(int x=0; x<totLength; x++){
                            InputCurrent in=tsk.getInput().get(x);
                            in.setId(0);
                            in.setOrderPosition(inCurr);
                            in.setBoardTaskInput(tsk);
                            if(rec.getElectrode()=="anode") in.setCurrent(currentAnodeCalculate(in.getCurrent()));
                            tsk.getInput().set(x, in);
                            OutputCurrent out=tsk.getOutput().get(x);
                            out.setOrderPosition(outCurr);
                            out.setId(0);
                            if(rec.getElectrode()=="anode") out.setCurrent(currentAnodeCalculate(out.getCurrent()));
                            tsk.getOutput().set(x,out);
                            inCurr++;
                            outCurr++;
                        }
                    }else
                    {
                        if(tsk.getInput().size()>0){
                            //currentAnodeCalculate
                            for(int a=0; a<tsk.getInput().size(); a++){
                                InputCurrent in=tsk.getInput().get(a);
                                in.setId(0);
                                in.setOrderPosition(inCurr);
                                in.setBoardTaskInput(tsk);
                                if(rec.getElectrode()=="anode"){
                                    in.setCurrent(currentAnodeCalculate(in.getCurrent()));
                                }
                                tsk.getInput().set(a, in);
                                inCurr++;
                            }
                        }
                        if(tsk.getOutput().size()>0){
                            for(int b=0; b<tsk.getOutput().size(); b++){
                                OutputCurrent out=tsk.getOutput().get(b);
                                out.setId(0);
                                out.setOrderPosition(outCurr);
                                out.setBoardTaskOutput(tsk);
                                if(rec.getElectrode()=="anode"){
                                    out.setCurrent(currentAnodeCalculate(out.getCurrent()));
                                }
                                tsk.getOutput().set(b,out);
                                outCurr++;
                            }
                        }
                    }
                    modeList.get(i).setBoardAction(tsk);
                    
                }
            rec.calculateCurrent();
            rec.setMode(modeList);
            }
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

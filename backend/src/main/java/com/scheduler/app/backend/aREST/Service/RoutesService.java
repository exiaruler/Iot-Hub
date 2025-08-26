package com.scheduler.app.backend.aREST.Service;

import java.util.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scheduler.Base.Base;
import com.scheduler.Base.JsonObject.JsonObject;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Models.CommandParameter;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Messaging.Models.BoardPin;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;
import com.scheduler.app.backend.aREST.ArestV2Frame;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.ScanDevice;
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
    public ArestV2Frame arest=new ArestV2Frame();
    public RoutesService(RoutesRepo routeRepo, ModeRepo modeService, DeviceRepo deviceRepo, CommandService commandService, ParameterService parameterService) {
        this.routeRepo = routeRepo;
        this.modeService = modeService;
        this.deviceRepo = deviceRepo;
        this.commandService = commandService;
        this.parameterService = parameterService;
    }
    
    public Route addRoute(long deviceId,String route,boolean modes,List<Mode>modeList){
        Route newRoute=new Route();
        Device device=deviceRepo.getReferenceById(deviceId);
        if(device!=null){
            newRoute.setDevice(device);
            if(!modes){
                newRoute.setMode(null);
            }else newRoute.setMode(modeList);
        }
        routeRepo.save(newRoute);
        return newRoute;
    }
    public Route addRouteCommand(long deviceId,String route,long commandId,List<String> pins){
        Route newRoute=new Route();
        newRoute.setRoute(route);
        Device device=deviceRepo.getReferenceById(deviceId);
        Command command=commandService.getCommand(commandId);
        if(device!=null&&command!=null){
            List<CommandParameter> paraCom=command.getCommandParameter();
            
            if(command.getParams()) newRoute.setModes(true);
            newRoute.setDevice(device);
            newRoute.setCommand(command);
        }
        routeRepo.save(newRoute);
        return newRoute;
    }
    public Mode addMode(long routeId,Mode entry){
        return modeService.save(entry);
    }
    public Mode addModeCommand(long deviceId,String modeName,long routeId,List<String> params){
        Mode newMode=new Mode();
        List<Mode> modeList=new ArrayList<Mode>();
        Device device=deviceRepo.getReferenceById(deviceId);
        Route route=routeRepo.getReferenceById(routeId);
        newMode.setMode(modeName);
        if(device!=null&&route!=null){
            Command command=route.getCommand();
            if(params.size()==command.getTotalParam()){
                // create mode
                newMode.setParams(parameterService.addParameters(params, newMode));
                newMode.setRoute(route);
                //Mode save=modeService.save(newMode);
                modeList.add(newMode);
                route.setMode(modeList);
            }
        }
        routeRepo.save(route);
        Mode savedMode=route.getMode().get(route.getMode().size()-1);
        return savedMode;
    }
    // add route and mode socket
    public Route addRouteandModes(Route route,String deviceId,Long commandId){
        if(deviceId!=""){
            Device dev=deviceRepo.findDeviceByDeviceId(deviceId);
            Command com=commandService.getCommand(commandId);
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
            
            Route save=routeRepo.save(route);
            route=save;
            }
        }

        

        return route;
    }
    
    public Route updateRoute(Route entry,Long id,Long commandId){
        Route rec=null;
        if(routeRepo.existsById(id)){
            rec=routeRepo.findById(id).get();
            Device dev=deviceRepo.findDeviceByDeviceId(rec.getDevice().getDeviceId());
            Command com=commandService.getCommand(commandId);
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
            rec.setMode(modeList);
            }
            routeRepo.save(rec);
        }
        return rec;
    }
    public void deleteRoute(long id){
        routeRepo.deleteById(id);
    }
    public List<Route> addRoutesByScan(Device deviceId,String ip){
        List<Route> routeList=new ArrayList<Route>();
        if(arest.testRoutes(ip)){
            String rawJson=httpUtil.request(ip);
            String queryDataRequest="QueryData";
            JsonObject json=jsonobj.jsonToObject(rawJson);
            String queryData=json.findKeyValue(queryDataRequest);
            String [] rawRoute=queryData.split("\\|\\|");
            // controller of version of routes and params
            String [] controlArr=rawRoute[0].trim().split("");
            String routeControl=controlArr[0];
            String paramControl=controlArr[1]; 
            String [] arr=rawRoute[1].split("\\"+routeControl);

            // loop and save routes and modes 
            for(int i=0; i<arr.length; i++){
                List <Mode> modeList=new ArrayList<Mode>();
                String route=arr[i];
                Route newRoute=new Route();
                String[] routeParam=getRoute(route);
                Route exist=routeRepo.findExistingRouteByDevice(routeParam[0],deviceId.getId());
                if(exist!=null){
                    newRoute=exist;
                }else{
                newRoute.setDevice(deviceId);
                }
                String param="";
                // if there params 
                if(routeParam[1]!=""){
                    param=routeParam[1];
                    String routeItself=routeParam[0];
                    //save param of that route
                    if(param!=""){
                        String [] arrParams=param.split("\\"+paramControl);
                        for(int x=0; x<arrParams.length; x++){
                            Mode newMode=new Mode();
                            Mode existMode=modeService.findMode(arrParams[x],newRoute.getId());
                            if(existMode!=null) newMode=existMode;
                            newMode.setRoute(newRoute);
                            newMode.setMode(arrParams[x]);
                            modeList.add(newMode);
                        }
                    }
                    newRoute.setRoute(routeItself);
                    newRoute.setModes(true);
                    newRoute.setMode(modeList);
                }else{
                    newRoute.setRoute(route);
                }
                routeList.add(newRoute);
            }
        }
        return routeList;
    }
    private String[] getRoute(String fullRoute){
        String routeParam[]={"",""};
        int bracketStartI=fullRoute.indexOf("(");
        int bracketEndI=fullRoute.indexOf(")");
        if(bracketEndI>-1&&bracketEndI>-1){
            String param=fullRoute.substring(bracketStartI+1, bracketEndI);
            String route=fullRoute.substring(0,bracketStartI);
            routeParam[0]=route;
            routeParam[1]=param;
        }else{
            routeParam[0]=fullRoute;
        }
        return routeParam;
    }
    // routes
    public List<Route> getAllRoutes(){
        return routeRepo.findAll();
    }
    public Route getRouteById(Long id){
        Route rou=routeRepo.findById(id).get();
        return rou;
    }
    // modes
    public List<Mode> getAllModes(){
        return modeService.findAll();
    }
    @Async
    @Transactional
    public Mode getMode(long id){
        return modeService.findById(id).get();
    }
    

    
}

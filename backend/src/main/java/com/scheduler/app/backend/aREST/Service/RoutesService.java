package com.scheduler.app.backend.aREST.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scheduler.Base.Exception.ValidationException;
import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Repo.DeviceRepo;
import com.scheduler.app.backend.aREST.Repo.ModeRepo;
import com.scheduler.app.backend.aREST.Repo.RoutesRepo;
// Functio and modes
@Service
public class RoutesService extends BaseService<Route, Long> {
    @Autowired
    private RoutesRepo routeRepo;

    private final ModeRepo modeService;
    private final DeviceRepo deviceRepo;
    private final CommandService commandService;
    private final ParameterService parameterService;
    @Override
    protected JpaRepository<Route, Long> repository() {
        return routeRepo;
    }
    public RoutesService(RoutesRepo routeRepo, ModeRepo modeService, DeviceRepo deviceRepo, CommandService commandService, ParameterService parameterService) {
        this.routeRepo = routeRepo;
        this.modeService = modeService;
        this.deviceRepo = deviceRepo;
        this.commandService = commandService;
        this.parameterService = parameterService;
    }
    public void updateRouteOffline(List<Long> ids){
        routeRepo.updateRoutesOffline(ids);
    }

    public List<Route> saveBatch(List<Route> routes){
        return routeRepo.saveAll(routes);
    }
    /* */
    @Override
    protected void beforeSave(Route entity, Map<String, String> errors, Map<String, String> warnings) {
        boolean exRec=routeRepo.existsById(entity.getId());
        // validate default mode
        if(entity.getMode().size()>0){
            boolean defModeSel=false;
            for(Mode mode:entity.getMode()){
                if(!exRec) mode.getBoardAction().newInputs();
                if(defModeSel&&mode.getDefaultMode()){
                    errors.put("mode","Cannot have more than 1 enabled default mode. Please uncheck "+mode.getMode());
                    break;
                }
                if(mode.getDefaultMode()&&!defModeSel){
                    defModeSel=true;
                }
            }
            if(!defModeSel) errors.put("mode","Please select 1 mode to be default");
            if(entity.getMode().size()>0){
                entity.setModes(true);
            }
        }
        if(entity.getCycleForward()&&entity.getCycleBack()){
            String msg="Selection of Forward Cycle and Backward Cyycle is not allowed";
            errors.put("cycleForward", msg);
            errors.put("cycleBack", msg);
        }

        if(errors.size()>0) throw new ValidationException(errors, null);

        if(!exRec){
            Command com=commandService.getCommand(entity.getCommandId());
            if(com!=null)entity.setCommand(com);
        }else
        {
            Route rec=routeRepo.findById(entity.getId()).get();
            Command com=commandService.getCommand(entity.getCommandId());
            if(com!=null && rec.getCommand().getId()==com.getId()){
                // BaseService saves the entity supplied to this method, not rec.
                // Reconcile the persisted modes into that entity so the changes
                // below are included in the subsequent save.
                List<Mode> updatedModeList=new ArrayList<>(entity.getMode());
                List<Mode> existModeList=rec.getMode();

                // A route edit may omit modes that are still referenced by
                // schedules. Keep those persisted mode entities in the update.
                for (Mode existingMode : existModeList) {
                    if (existingMode.getScheduledModes() != null
                            && !existingMode.getScheduledModes().isEmpty()
                            && updatedModeList.stream()
                                    .noneMatch(mode -> mode.getId() == existingMode.getId())) {
                        updatedModeList.add(existingMode);
                    }
                }

                entity.setCommand(com);
                entity.setCommandId(com.getId());
                if(entity.getMode().size()>0){
                    entity.setModes(true);
                }
                existModeList.removeIf(exist -> updatedModeList.stream()
                        .noneMatch(mode -> mode.getId() == exist.getId()));
                
                // Use the persisted mode instances for existing modes, then put
                // the reconciled collection back onto the incoming entity.
                boolean defModeSel=false;
                for(Mode mode:updatedModeList){
                    Mode ex=existModeList.stream().filter(m->m.getId()==mode.getId()).findFirst().orElse(null);
                    if(defModeSel&&mode.getDefaultMode()){
                        errors.put("mode","Cannot have more than 1 enabled default mode. Please uncheck "+mode.getMode());
                        break;
                    }
                    if(mode.getDefaultMode()&&!defModeSel){
                        defModeSel=true;
                    }
                    // update existing mode
                    if(ex!=null){
                        ex.setMode(mode.getMode());
                        BoardTask act=mode.getBoardAction();
                        ex.setDefaultMode(mode.getDefaultMode());
                        if(act!=null){
                            act.setMode(ex);
                            ex.setBoardAction(act);
                        }
                    }else{
                        mode.setRoute(entity);
                        if(mode.getBoardAction()!=null) mode.getBoardAction().newInputs();
                        existModeList.add(mode);
                    }    
                }
                if(!defModeSel) errors.put("mode","Please select 1 mode to be default");
                entity.setMode(existModeList);
            }else
            {
                entity.setCommand(com);
                entity.setCommandId(com != null ? com.getId() : 0);
                boolean hasScheduledModes = rec.getMode().stream()
                        .anyMatch(mode -> mode.getScheduledModes() != null
                                && !mode.getScheduledModes().isEmpty());
                if (hasScheduledModes) {
                    errors.put("command", "Cannot change the command while a mode is used by a schedule");
                } else {
                    entity.getMode().clear();
                }
            }
            if(errors.size()>0) throw new ValidationException(errors, null);
            entity.setDevice(rec.getDevice());
        }
        entity.calculateCurrent();
    }
    @Override
    protected void afterSave(Route entity) {
        if(entity.getSelectedModeId()==0){
            entity.setDefaultMode();
            routeRepo.save(entity);
        }
    }
    // add route and mode socket
    public Route addRouteandModes(Route route,String deviceId){
        if(deviceId!=""){
            Device dev=deviceRepo.findDeviceByDeviceId(deviceId);
            if(dev!=null){
                route.setDevice(dev);
                Route save=this.save(route);
                route=save;
            }
        }
        return route;
    }
    // update route and modes
    @Transactional
    public Route updateRoute(Route entry,Long id){
        Route existingRoute = routeRepo.findById(id).orElse(null);
        if (existingRoute == null) {
            return null;
        }
        // The request may omit inverse relationships. Keep the persisted
        // schedule collection and device while applying the route update.
        entry.setId(id);
        entry.setDevice(existingRoute.getDevice());
        entry.setScheduledRoutes(existingRoute.getScheduledRoutes());
        return this.save(entry);
    }
    // route/function config update
    public Route updateRouteConfig(Route entry,Long id){
        Route rec=null;
        if(routeRepo.existsById(id)){
            rec=routeRepo.findById(id).get();
            rec.setDefaultModeId(entry.getDefaultModeId());
            rec=routeRepo.save(rec);
        }
        return rec;
    }
    public void deleteRoute(long id){
        this.delete(id);
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

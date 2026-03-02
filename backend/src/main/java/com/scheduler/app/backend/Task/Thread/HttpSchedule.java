package com.scheduler.app.backend.Task.Thread;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.scheduler.Base.JsonObject.JsonObject;
import com.scheduler.Base.ThreadBase.BaseThread;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.Task;
// sends action http request to device
public class HttpSchedule extends BaseThread{
    private Task task;
    private Device device;
    private Route route;
    private Mode mode;
    private String[] params;
    
    public HttpSchedule(Task task,Device device,Route route,Mode mode,String[] params) {
        this.task=task;
        this.device=device;
        this.route=route;
        this.mode=mode;
        this.params=params;
    }
    @Override
    public void run(){
        try {
            sendRequest(task,device,route,mode,params);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    // send request and sends reponse to services to update devices
    public void sendRequest(Task task,Device device,Route route,Mode mode,String[] params) throws InstantiationException{
        long startTime = System.nanoTime();
        
        boolean sucess=false;
        String state="";
        String warning="";
        boolean complete=false;
        boolean execute=false;
        if(device!=null){
            String deviceIp=device.getBoard().getIp();
            if(http.requestRouteTest(deviceIp)&&device.getBoard().getArestCommand()){ 
                String modeState=mode.getMode();
                String classString="";
                String methodName=route.getCommand().getCommand();
                try {
                    Class <?> className=Class.forName(classString);
                    if(route.getModes()){
                        Method method =className.getMethod(methodName,String.class,String.class.arrayType());
                        @SuppressWarnings("deprecation")
                        Object classInstance = className.newInstance();
                        execute=(boolean)method.invoke(classInstance,device.getBoard().getIp(),params);
                        if(execute){
                            sucess=true;    
                            state=modeState;
                            complete=true;
                        }
                        // execute method that don't have parameter
                    }else{
                        Method method =className.getMethod(methodName,String.class);
                        @SuppressWarnings("deprecation")
                        Object classInstance = className.newInstance();
                        execute=(boolean)method.invoke(classInstance,device.getBoard().getIp());
                        if(execute){
                            sucess=true;    
                            state=modeState;
                            complete=true;
                        }
                    }
                } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                sche.removeRunningTask(task);
                long endTime = System.nanoTime();
                sche.addToComplete(device, sucess, state, warning,complete,task);
            }else if(http.requestRouteTest(deviceIp)&&device.getBoard().getArest()){
                JsonObject change=arest.changeDevice(deviceIp,device.getName());
                if(change!=null&&change.findKeyValue("return_value").equals("1")){
                    String response=http.requestDevice(task.getUrl(),5,"");
                    // get data required to update device in database
                    if(response!=""){
                        String rawVariable=base.getrawVariable(response);
                        String returnValue=base.getDataByFieldRevamp("return_value",rawVariable);
                        JsonObject deviceJson=new JsonObject();
                        String deviceName="";
                        // 1 is sucess
                        if(returnValue.equals("1")){
                            deviceJson=base.jsonobj.jsonToObject(http.request(deviceIp));
                            deviceName=deviceJson.findKeyValue("SetDevice");
                            sucess=true;
                        }
                        // get state of device
                        if(sucess&&deviceName!=""){
                            state=deviceJson.findKeyValue("Status");
                            device.setState(state);
                            System.out.println(state);
                        }else{
                            warning=deviceJson.findKeyValue("Warning");
                            device.setWarning(warning);
                        }
                        complete=true;
                        long endTime = System.nanoTime();
                        sche.removeRunningTask(task);
                        // add to complete task queue to update device
                        sche.addToComplete(device, sucess, state, warning,complete,task);
                    }else sche.failedTask(task,device);
                }sche.failedTask(task,device);
            }else sche.failedTask(task,device);
        }else
        // send http request for non devices
        {
            String response=http.requestDevice(task.getUrl(),5,task.getPayload());
            long endTime = System.nanoTime();
            sche.addToComplete(null, sucess, state, warning,true,task);
            
        }
    }
}

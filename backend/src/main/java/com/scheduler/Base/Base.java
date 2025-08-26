package com.scheduler.Base;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;

import com.scheduler.Base.JsonObject.JsonObject;
import com.scheduler.app.backend.HTTPHandle.HttpUtil;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;

// template class 
public class Base{
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private Validator validator;
    protected HttpUtil httpUtil=new HttpUtil();
    public JsonObject jsonobj=new JsonObject();
    public final String pathBase="com.scheduler.app.backend";
    // temporary memory storage
    public static HashMap<String[],String> memory=new HashMap<String[],String>();

    public String getModel(String module,String className){
        return pathBase+"."+module+"."+"Models"+"."+className;
    }
    public String getDataString(String query){
        String data="";
        try{
            data=entityManager.createNativeQuery(query).getSingleResult().toString();
        }catch(Exception e){

        }
        return data;
    }
    public int getDataInt(String query){
        int data=-1;
        String executeQuery="";
        try{
            executeQuery=entityManager.createNativeQuery(query).getSingleResult().toString();
            if(executeQuery!=""){
                data=Integer.parseInt(executeQuery);
            }
        }catch(Exception e){

        }
        return data;
    }
    public long getDataLong(String query){
        long data=-1;
        String executeQuery="";
        try {
            executeQuery=entityManager.createNativeQuery(query).getSingleResult().toString();
            if(executeQuery!=""){
                data=Long.parseLong(executeQuery);
            }
        } catch (Exception e) {
        }
        return data;
    }
    public void executeQuery(String query){
        try {
            entityManager.createNativeQuery(query).executeUpdate();
        } catch (Exception e) {
        }
    }
    public String quoteParam(String value){
        return "'"+value+"'";
    }

    // arest v2 get value from query route
    public String requestQuery(String ip,String param){
        String responseBody="";
        long standard=200;
        String routeSet=httpUtil.createRoute(ip,"query",param);
        try {
            String rawReturn=httpUtil.httpRequest(routeSet,standard);
            JsonObject returnJson=jsonobj.jsonToObject(rawReturn);
            if(returnJson.findKeyValue("return_value").trim().equals("1")){
                String queryDataRoute=httpUtil.createRoute(ip,"QueryData","");
                String queryDataRaw=httpUtil.httpRequest(queryDataRoute, standard);
                returnJson=jsonobj.jsonToObject(queryDataRaw);
                responseBody=returnJson.findKeyValue("QueryData").trim();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    // check if the device is an arduino board using aREST
    public boolean arduinoboardCheck(String jsonString){
        String [] arr=jsonString.split("},");
        if(arr.length==2){
            return true;
        }
        return false;
    }
    // get part from json string. variable or device
    public String getrawPart(String jsonString,int part){
        String [] arr=jsonString.split("},");
        String output="";
        output=arr[part];
        if(part==1){
            int curlyBracesFind=output.indexOf("}");
            output=output.substring(0, curlyBracesFind);
        }
        output=output.replaceAll("\"","");
        return output;
    }
    public String getrawVariable(String jsonString){
        int startBrace=jsonString.indexOf("{");
        int endBrace=jsonString.indexOf("}");
        jsonString=jsonString.substring(startBrace+1, endBrace);
        jsonString=jsonString.replaceAll("\"","");
        return jsonString;
    }
    // get field from json for board
    public String getDataByFieldBoard(String field,String input){
        field=" "+field;
        String [] arr=input.split(":");
        String output="";
        if(arr[0].equals(field)){
            output=arr[1];
        }
        return output;
    }

    // get field from json
    public String getDataByField(String field,String input){
        String [] arr=input.split(":");
        String output="";
        if(arr[0].toLowerCase().equals(field.toLowerCase())){
            output=arr[1];
        }
        return output;
    }
    public String getDataByFieldRevamp(String field,String input){
        String data="";
        String [] arr=input.split(",");
        for(int i=0; i<arr.length; i++){
            String jsonRow=arr[i];
            String [] jsonSplit=jsonRow.split(":");
            if(field.toLowerCase().equals(jsonSplit[0].toLowerCase())){
                data=jsonSplit[1].trim();
                break;
            }
        }
        return data;
    }
    // get object in json aREST
    public String getVariableData(String value){
        if(value=="") return "";
        int index=value.indexOf(": {");
        value=value.substring(index);
        index=value.indexOf("{");
        value=value.substring(index+1);
        return value;
    }
    // generate random string
    public String generateRandString(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilt = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilt.append(characters.charAt(index));
        }
        return stringBuilt.toString();
    }
    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
    // create string variable
    public String stringVariable(String variable){
        return "\""+variable+"\"";
    }
    // create string boolean
    public String booleanToString(boolean bool){
        String res="false";
        if(bool) res="true";
        return res;
    }
    public String returnDataString(List<String> list){
        return "{"+String.join(",", list)+"}";
    }
    public List<InputCurrent> inputCurrentCalculate(List<InputCurrent> inputs,String electrode){
        boolean anode=false;
        if(electrode=="anode"){
            anode=true;
        }
        for(int i=0; i<inputs.size(); i++){
            InputCurrent in=inputs.get(i);
            if(anode){
                in.setCurrent(currentAnodeCalculate(in.getCurrent()));
            }
            inputs.set(i,in);
        }
        return inputs;
    }
    public List<OutputCurrent> outputCurrentCalculate(List<OutputCurrent> inputs,String electrode){
        boolean anode=false;
        if(electrode=="anode"){
            anode=true;
        }
        for(int i=0; i<inputs.size(); i++){
            OutputCurrent in=inputs.get(i);
            if(anode){
                in.setCurrent(currentAnodeCalculate(in.getCurrent()));
            }
            inputs.set(i,in);
        }
        return inputs;
    }
    public int currentAnodeCalculate(int value){
        return 255-value;
    }
}
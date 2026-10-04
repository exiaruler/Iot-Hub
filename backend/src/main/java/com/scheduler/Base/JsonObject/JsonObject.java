package com.scheduler.Base.JsonObject;
import java.util.*;

// turn json string into object
public class JsonObject implements Json {
    // store json key and data in arraylist
    ArrayList<Variable> variables=new ArrayList<Variable>();
    // store key index for search in variables
    ArrayList<String> keyIndex=new ArrayList<String>();

    public JsonObject jsonToObject(String json){
        JsonObject obj=new JsonObject();
        json=json.replaceAll("\"", "");
        int startBrace=json.indexOf("{");
        int endBrace=-1;
        for(int x=json.length()-1; x>0; --x){
            char character=json.charAt(x);
            if(character=='}'){
                endBrace=x;
                break;
            }
        }
        json=json.substring(startBrace+1,endBrace);
        // add json within first 
        int stringIndex=0;
        int startIndex=-1,endIndex=-1;
        while(json.contains("{")&&json.contains("}")){
            char character=json.charAt(stringIndex);
            if(character=='{'&&startIndex==-1){
                startIndex=stringIndex;
            }
            if(character=='}'&&endIndex==-1&&startIndex>-1){
                endIndex=stringIndex;
            }
            if(startIndex>-1&&endIndex>-1){
                String variable=json.substring(startIndex+1,endIndex);
                String vArr[]=variable.split(",");
                for(int i=0; i<vArr.length; i++){
                    String [] arr=vArr[i].split(":");
                    obj=addJson(arr[0],arr[1], obj);
                }
                json=json.substring(endIndex+2,json.length());
                if(json.contains("{")&&json.contains("}")){
                    startIndex=-1;
                    endIndex=-1;
                }
            }
            stringIndex++;
        }
        String arr []=json.split(",");
        for(int i=0; i<arr.length; i++){
            if(arr[i].indexOf(":")>0){
                String [] varr=arr[i].split(":");
                obj=addJson(varr[0],varr[1], obj);
            }else break;
            
        }
        return obj;
    }

    
    // find value by key in JsonObject 
    public String findKeyValue(String key){
        String output="";
        ArrayList<String> indexes=this.keyIndex;
        if(indexes.contains(key)){
            int index=indexes.indexOf(key);
            output=this.variables.get(index).value;
        }
        return output;
    }
    // check key in JsonObject
    public boolean checkKey(String key){
        boolean output=false;
        ArrayList<String> indexes=this.keyIndex;
        if(indexes.contains(key)){
            output=true;
        }
        return output;
    }
    // find value by key that is formatted as an and convert value into arraylist
    public ArrayList<String> findKeyToArray(String key,String split){
        ArrayList<String> list=new ArrayList<>();
        String value=findKeyValue(key).trim();
        int startBrace=value.indexOf("[");
        int endBrace=value.indexOf("]");
        if(value!=""&&startBrace>-1&&endBrace>-1&&value.length()-1==endBrace){
            String a []=value.split(split);
            ArrayList<String> arr=new ArrayList<String>( Arrays.asList(a));
            list=arr;
        }
        return list;
    }
    /* 
    public ArrayList<ArrayList<String>> findKeyToMultiDimArray(String key,String split){

    }
    */
    private JsonObject addJson(String key,String value,JsonObject jObject){
        Variable obj=new Variable();
        ArrayList<Variable> variableList=new ArrayList<Variable>();
        ArrayList<String> indexList=new ArrayList<String>();
        // check if value is an array
        
        obj.key=key.trim();
        obj.value=value.trim();
        // add data
        if(jObject.variables.size()==0){
            variableList.add(obj);
            jObject.variables=variableList;
        }else{
            jObject.variables.add( obj);
        }
        // add index
        if(jObject.keyIndex.size()==0){
            indexList.add(key.trim());
            jObject.keyIndex=indexList;
        }else{
            jObject.keyIndex.add(key.trim());
        }
        return jObject;
    }
    private String loopThroughJsonObjectArray(String value){
        String json="";
        if(value.contains("{")&&value.contains("}")){
            int start=value.indexOf("{");
            int end=value.indexOf("}");
        }
        return json;
    }
   
}


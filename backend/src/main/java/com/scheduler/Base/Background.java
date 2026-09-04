package com.scheduler.Base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
@Component
public class Background {
    private static final Map<String, Object> global = new ConcurrentHashMap<>();

    public static void putGlobal(String key,Object value){
        if(value!=null) global.put(key, value);
    }
    public static Object getGlobal(String key){
        return global.get(key);
    }
    public static Map<String, Object> getGlobals(){
        return new HashMap<>(global);
    }
    public static void removeGlobal(String key){
        global.remove(key);
    }
    public static boolean globalExist(String key){
        boolean res=false;
        if(global.get(key)!=null) res=true;
        return res;
    }
}

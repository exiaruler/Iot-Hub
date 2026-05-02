package com.scheduler.Base;

import java.lang.reflect.InvocationTargetException;

import org.springframework.web.bind.annotation.GetMapping;

import com.scheduler.Base.MapCast.MapCast;

public class ControllerBase extends Base {
    public MapCast mapCast=new MapCast();
    public String objectClass="";
    
    @GetMapping("/new-record")
    public Object getNewRecord() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Object obj=null;
        try {
            Class <?> className=Class.forName(objectClass);
            obj=className.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
    

}

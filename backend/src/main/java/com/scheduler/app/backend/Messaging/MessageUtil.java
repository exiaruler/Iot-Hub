package com.scheduler.app.backend.Messaging;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.scheduler.app.backend.JacksonConfig;
import com.scheduler.app.backend.Messaging.Models.BoardTask;

public class MessageUtil {
    private final ObjectMapper mapper = JacksonConfig.createObjectMapper();

    // convert string BoardTask to object
    public BoardTask stringToObject(String json) throws JacksonException{
        BoardTask boardTask=null;
        try {
            boardTask=mapper.readValue(json,BoardTask.class);
        } catch (JacksonException e) {
            boardTask=null;
            e.printStackTrace();
        } 
        return boardTask;
    }

    // convert board task to json string
    public String objectToJsonString(BoardTask task) throws JacksonException{
        String json="";  
        try {
            json = mapper.writeValueAsString(task);
        } catch (Exception e) {
            json="";
        }
        return json;
    }


}

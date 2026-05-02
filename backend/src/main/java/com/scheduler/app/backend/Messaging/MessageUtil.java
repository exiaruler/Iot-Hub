package com.scheduler.app.backend.Messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.app.backend.Messaging.Models.BoardTask;

public class MessageUtil {

    // convert string BoardTask to object
    public BoardTask stringToObject(String json) throws JsonProcessingException{
        BoardTask boardTask=null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            boardTask=mapper.readValue(json,BoardTask.class);
        } catch (JsonMappingException e) {
            boardTask=null;
            e.printStackTrace();
        } 
        return boardTask;
    }

    // convert board task to json string
    public String objectToJsonString(BoardTask task) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String json="";  
        try {
            json = mapper.writeValueAsString(task);
        } catch (Exception e) {
            json="";
        }
        return json;
    }


}

package com.scheduler.app.backend.Messaging.Board.Models.ArraySerial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;

@JsonPropertyOrder({"current"})
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonIgnoreProperties({"orderPosition", "boardTaskOutput", "id"})
public class BoardInputSerial extends InputCurrent{

     public BoardInputSerial(InputCurrent output) {
        this.setCurrent(output.getCurrent());
        this.setOrderPosition(output.getOrderPosition());
    }
}

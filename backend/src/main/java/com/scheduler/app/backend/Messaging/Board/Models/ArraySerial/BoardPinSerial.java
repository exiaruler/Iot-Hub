package com.scheduler.app.backend.Messaging.Board.Models.ArraySerial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.scheduler.app.backend.Messaging.Models.BoardPin;

@JsonPropertyOrder({"pin"})
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonIgnoreProperties({"pinOrder", "boardTask", "id"})
public class BoardPinSerial extends BoardPin {

    public BoardPinSerial(BoardPin boardPin) {
        this.setPin(boardPin.getPin());
        this.setPinOrder(boardPin.getPinOrder());
    }

}

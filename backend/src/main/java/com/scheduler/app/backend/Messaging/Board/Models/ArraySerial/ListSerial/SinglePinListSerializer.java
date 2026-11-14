package com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.ListSerial;

import java.util.List;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.SerializationContext;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardPinSerial;

public class SinglePinListSerializer extends ValueSerializer<List<BoardPinSerial>>{

    @Override
    public void serialize(List<BoardPinSerial> value, JsonGenerator gen, SerializationContext context) {
            gen.writeStartArray();
            for (BoardPinSerial pin : value) {
                gen.writeNumber(pin.getPin());
            }
            gen.writeEndArray();
    }

}

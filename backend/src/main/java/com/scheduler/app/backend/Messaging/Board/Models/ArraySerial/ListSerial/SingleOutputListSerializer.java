package com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.ListSerial;

import java.util.List;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.SerializationContext;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardOutputSerial;

public class SingleOutputListSerializer extends ValueSerializer<List<BoardOutputSerial>>{

    @Override
    public void serialize(List<BoardOutputSerial> value, JsonGenerator gen, SerializationContext context) {
        // TODO Auto-generated method stub
        gen.writeStartArray();
        for (BoardOutputSerial out : value) {
            gen.writeNumber(out.getCurrent());
        }
        gen.writeEndArray();
    }

}

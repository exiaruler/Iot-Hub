package com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.ListSerial;

import java.util.List;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.SerializationContext;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardInputSerial;

public class SingleInputListSerializer extends ValueSerializer<List<BoardInputSerial>>{

    @Override
    public void serialize(List<BoardInputSerial> value, JsonGenerator gen, SerializationContext context) {
            gen.writeStartArray();
            for (BoardInputSerial in : value) {
                gen.writeNumber(in.getCurrent());
            }
            gen.writeEndArray();
    }

}

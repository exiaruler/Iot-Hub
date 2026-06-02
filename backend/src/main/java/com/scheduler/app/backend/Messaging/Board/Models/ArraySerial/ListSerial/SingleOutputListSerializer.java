package com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.ListSerial;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardOutputSerial;

public class SingleOutputListSerializer extends JsonSerializer<List<BoardOutputSerial>>{

    @Override
    public void serialize(List<BoardOutputSerial> value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        // TODO Auto-generated method stub
        gen.writeStartArray();
        for (BoardOutputSerial out : value) {
            gen.writeNumber(out.getCurrent());
        }
        gen.writeEndArray();
    }

}

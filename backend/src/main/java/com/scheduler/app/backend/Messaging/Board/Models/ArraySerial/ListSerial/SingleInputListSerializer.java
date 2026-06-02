package com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.ListSerial;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardInputSerial;

public class SingleInputListSerializer extends JsonSerializer<List<BoardInputSerial>>{

    @Override
    public void serialize(List<BoardInputSerial> value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
            gen.writeStartArray();
            for (BoardInputSerial in : value) {
                gen.writeNumber(in.getCurrent());
            }
            gen.writeEndArray();
    }

}

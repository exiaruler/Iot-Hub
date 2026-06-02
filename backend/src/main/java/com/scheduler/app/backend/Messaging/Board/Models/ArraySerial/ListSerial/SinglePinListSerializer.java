package com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.ListSerial;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardPinSerial;

public class SinglePinListSerializer extends JsonSerializer<List<BoardPinSerial>>{

    @Override
    public void serialize(List<BoardPinSerial> value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
            gen.writeStartArray();
            for (BoardPinSerial pin : value) {
                gen.writeNumber(pin.getPin());
            }
            gen.writeEndArray();
    }

}

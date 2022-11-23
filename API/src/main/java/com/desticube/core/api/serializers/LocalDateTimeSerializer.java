package com.desticube.core.api.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeSerializer extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter out, LocalDateTime time) throws IOException {
        if (time == null) {
            out.nullValue();
            return;
        }
        out.value(time.getYear() + ";" + time.getMonthValue() + ";" + time.getDayOfMonth() + ";" +
                time.getHour() + ";" + time.getMinute() + ";" + time.getSecond());
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String [] parts = in.nextString().split(";");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int dayOfMonth = Integer.parseInt(parts[2]);
        int hour = Integer.parseInt(parts[3]);
        int minute = Integer.parseInt(parts[4]);
        int second = Integer.parseInt(parts[5]);
        return LocalDateTime.of(year,month,dayOfMonth,hour,minute,second);
    }
}

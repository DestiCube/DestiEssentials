package com.desticube.core.api.serializers;

import com.desticube.core.api.objects.records.Badge;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BadgeSerializer extends TypeAdapter<Badge> {
    @Override
    public void write(JsonWriter out, Badge badge) throws IOException {
        if (badge == null) {
            out.nullValue();
            return;
        }
        out.value(badge.name() + ";" + badge.badge() + ";" + badge.description() + ";" + badge.permission());
    }

    @Override
    public Badge read(JsonReader in) throws IOException {
        String [] parts = in.nextString().split(";"); //If you changed the semicolon you must change it here too
        String name = parts[0];
        String badge = parts[1];
        String description = parts[2];
        String permission = parts[3];
        return new Badge(name, badge, description, permission);
    }
}

package com.desticube.core.api.serializers;

import com.desticube.core.api.objects.records.Jail;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.UUID;

public class JailSerializer extends TypeAdapter<Jail> {
    @Override
    public void write(JsonWriter out, Jail jail) throws IOException {
        if (jail == null) {
            out.nullValue();
            return;
        }
        Location loc = jail.location();
        out.value(jail.name() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch() + ";" + loc.getWorld().getUID().toString());
    }

    @Override
    public Jail read(JsonReader in) throws IOException {
        String [] parts = in.nextString().split(";");
        String name = parts[0];
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);
        UUID u = UUID.fromString(parts[6]);
        World world = Bukkit.getWorld(u);
        return new Jail(name, new Location(world, x, y, z, yaw, pitch));
    }
}

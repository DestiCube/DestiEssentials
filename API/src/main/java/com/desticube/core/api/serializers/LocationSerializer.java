package com.desticube.core.api.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.UUID;

public class LocationSerializer extends TypeAdapter<Location> {
    @Override
    public void write(JsonWriter out, Location loc) throws IOException {
        if (loc == null) {
            out.nullValue();
            return;
        }
        out.value(loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch() + ";" + loc.getWorld().getUID().toString());
    }

    @Override
    public Location read(JsonReader in) throws IOException {
        String [] parts = in.nextString().split(";"); //If you changed the semicolon you must change it here too
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        float yaw = Float.parseFloat(parts[3]);
        float pitch = Float.parseFloat(parts[4]);
        UUID u = UUID.fromString(parts[5]);
        World world = Bukkit.getWorld(u);
        return new Location(world, x, y, z, yaw, pitch);
    }
}

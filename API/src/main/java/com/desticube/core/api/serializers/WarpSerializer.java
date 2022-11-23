package com.desticube.core.api.serializers;

import com.desticube.core.api.objects.records.Warp;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class WarpSerializer extends TypeAdapter<Warp> {
    @Override
    public void write(JsonWriter out, Warp warp) throws IOException {
        if (warp == null) {
            out.nullValue();
            return;
        }
        Location loc = warp.location();
        out.value(warp.name() + ";" + itemStackToBase64(warp.displayItem()) + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch() + ";" + loc.getWorld().getUID().toString());
    }

    @Override
    public Warp read(JsonReader in) throws IOException {
        String [] parts = in.nextString().split(";"); //If you changed the semicolon you must change it here too
        String name = parts[0];
        String itemStackBase64 = parts[1];
        double x = Double.parseDouble(parts[2]);
        double y = Double.parseDouble(parts[3]);
        double z = Double.parseDouble(parts[4]);
        float yaw = Float.parseFloat(parts[5]);
        float pitch = Float.parseFloat(parts[6]);
        UUID u = UUID.fromString(parts[7]);
        World world = Bukkit.getWorld(u);
        return new Warp(name, new Location(world, x, y, z, yaw, pitch), itemStackFromBase64(itemStackBase64));
    }

    private String itemStackToBase64(ItemStack items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(items);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    private ItemStack itemStackFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack items = (ItemStack) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

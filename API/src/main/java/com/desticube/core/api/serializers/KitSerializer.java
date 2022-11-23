package com.desticube.core.api.serializers;

import com.desticube.core.api.objects.records.Kit;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KitSerializer extends TypeAdapter<Kit> {
    @Override
    public void write(JsonWriter out, Kit kit) throws IOException {
        if (kit == null) {
            out.nullValue();
            return;
        }
        out.value(kit.name() + ";" + itemStackArrayToBase64(kit.items()) + ";" + kit.cooldown() + ";" + kit.invSlot());
    }

    @Override
    public Kit read(JsonReader in) throws IOException {
        String [] parts = in.nextString().split(";");
        String name = parts[0];
        String itemStackArrayBase64 = parts[1];
        long cooldown = Long.parseLong(parts[2]);
        int invSlot = Integer.parseInt(parts[3]);
        return new Kit(name, invSlot, itemStackArrayFromBase64(itemStackArrayBase64), cooldown);
    }

    private String itemStackArrayToBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (int i = 0; i < items.length; i++) dataOutput.writeObject(items[i]);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    private ItemStack[] itemStackArrayFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) items[i] = (ItemStack) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

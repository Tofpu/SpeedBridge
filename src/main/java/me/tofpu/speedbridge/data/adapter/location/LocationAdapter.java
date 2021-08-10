package me.tofpu.speedbridge.data.adapter.location;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;

public class LocationAdapter extends TypeAdapter<Location> {

    @Override
    public void write(JsonWriter out, Location value) throws IOException {
        out.beginObject();

        out.name("world").value(value.getWorld().getName());
        out.name("x").value(value.getX());
        out.name("y").value(value.getY());
        out.name("z").value(value.getZ());
        out.name("pitch").value(value.getPitch());
        out.name("yaw").value(value.getYaw());

        out.endObject();
    }

    @Override
    public Location read(JsonReader in) throws IOException {
        in.beginObject();

        in.nextName();
        if (in.peek() == JsonToken.NULL) return null;
        final World world = Bukkit.getWorld(in.nextString());

        in.nextName();
        final double x = in.nextDouble();

        in.nextName();
        final double y = in.nextDouble();

        in.nextName();
        final double z = in.nextDouble();

        in.nextName();
        final float yaw = Float.parseFloat(in.nextString());

        in.nextName();
        final float pitch =  Float.parseFloat(in.nextString());

        final Location location = new Location(world, x, y, z, yaw, pitch);
        in.endObject();
        return location;
    }
}

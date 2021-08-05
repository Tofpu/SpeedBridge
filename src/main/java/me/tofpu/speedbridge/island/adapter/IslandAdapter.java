package me.tofpu.speedbridge.island.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import org.bukkit.Location;

import java.io.IOException;

public class IslandAdapter extends TypeAdapter<IIsland> {
    private final TypeAdapter<Location> locationAdapter;

    public IslandAdapter(TypeAdapter<Location> locationAdapter) {
        this.locationAdapter = locationAdapter;
    }

    @Override
    public void write(JsonWriter out, IIsland value) throws IOException {
        out.beginObject();
        out.name("slot").value(value.getSlot());
        out.name("spawn");
        locationAdapter.write(out, value.getLocation());
        out.beginArray();
        final IslandProperties properties = value.getProperties();
        out.name("point-a");
        locationAdapter.write(out, properties.getLocationA());
        out.name("point-b");
        locationAdapter.write(out, properties.getLocationB());
        out.endArray();
        out.endObject();
    }

    @Override
    public IIsland read(JsonReader in) throws IOException {
        in.beginObject();

        in.nextName();
        final IIsland island = new Island(in.nextInt());

        in.nextName();
        island.setLocation(locationAdapter.read(in));
        in.beginArray();

        final IslandProperties properties = island.getProperties();

        in.nextName();
        properties.setLocationA(locationAdapter.read(in));

        in.nextName();
        properties.setLocationB(locationAdapter.read(in));

        in.endArray();
        in.endObject();
        return island;
    }
}

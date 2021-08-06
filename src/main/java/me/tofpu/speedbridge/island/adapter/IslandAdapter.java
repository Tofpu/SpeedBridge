package me.tofpu.speedbridge.island.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import org.bukkit.Location;

import java.io.IOException;

public class IslandAdapter extends TypeAdapter<IIsland> {
    @Override
    public void write(JsonWriter out, IIsland value) throws IOException {
        out.beginObject();

        out.name("slot").value(value.getSlot());
        out.name("spawn");
        DataManager.GSON.toJson(value.getLocation(), Location.class, out);

        out.name("properties").beginArray();

        final IslandProperties properties = value.getProperties();
        out.beginObject();
        out.name("point-a");
        DataManager.GSON.toJson(properties.getLocationA(), Location.class, out);
        out.name("point-b");
        DataManager.GSON.toJson(properties.getLocationB(), Location.class, out);
        out.endObject();

        out.endArray();
        out.endObject();
    }

    @Override
    public IIsland read(JsonReader in) throws IOException {
        in.beginObject();

        final TypeAdapter<Location> adapter = DataManager.GSON.getAdapter(Location.class);
        in.nextName();
        final IIsland island = new Island(in.nextInt());

        in.nextName();
        island.setLocation(adapter.read(in));

        in.nextName();
        in.beginArray();

        final IslandProperties properties = island.getProperties();
        in.beginObject();

        in.nextName();
        properties.setLocationA(adapter.read(in));

        in.nextName();
        properties.setLocationB(adapter.read(in));
        in.endObject();

        in.endArray();
        in.endObject();
        return island;
    }
}

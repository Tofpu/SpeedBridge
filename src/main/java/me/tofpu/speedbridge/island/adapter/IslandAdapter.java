package me.tofpu.speedbridge.island.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.island.properties.point.Point;
import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import org.bukkit.Location;

import java.io.IOException;

public class IslandAdapter extends TypeAdapter<IIsland> {
    @Override
    public void write(JsonWriter out, IIsland value) throws IOException {
        out.beginObject();

        out.name("slot").value(value.getSlot());
        out.name("spawn");
        DataManager.GSON.toJson(value.getLocation(), Location.class, out);

        out.name("properties").beginArray().beginObject();

        final IslandProperties properties = value.getProperties();
        for (int i = 0; i < properties.getTwoSections().size(); i++) {
//            final TwoSection section = properties.getTwoSections().get(i);
            final Point point = properties.getTwoSections().get(i);

            out.name(i + "").beginArray();
            out.beginObject();
            if (point instanceof TwoSection) {
                final TwoSection section = (TwoSection) point;
                if (!section.hasPointA() || !section.hasPointA()) continue;

                out.name(section.getIdentifier() + "-a");
                write(section.getPointA(), out);
                out.name(section.getIdentifier() + "-b");
                write(section.getPointB(), out);
            } else {
                out.name(point.getIdentifier());
                write(point.getPointA(), out);
            }

            out.endObject();
            out.endArray();
        }

        out.endObject();
        out.endArray();
        out.endObject();
    }

    @Override
    public IIsland read(JsonReader in) throws IOException {
        final TypeAdapter<Location> adapter = DataManager.GSON.getAdapter(Location.class);

        in.beginObject();
        in.nextName();
        final IIsland island = new Island(in.nextInt());

        in.nextName();
        island.setLocation(adapter.read(in));

        in.nextName();
        in.beginArray();
        in.beginObject();

        final IslandProperties properties = island.getProperties();
        while (in.hasNext()) {
            final String index = in.nextName();
            in.beginArray();
            in.beginObject();


            while (in.hasNext()) {
                final String[] input = in.nextName().split("-");
                final Location location = toLocation(adapter, in);


//                final TwoSection section = properties.get(input[0]);
                final Point point = properties.get(input[0]);
                if (point instanceof TwoSection) {
                    final TwoSection section = (TwoSection) point;
                    switch (input[1]) {
                        case "a":
                            section.setPointA(location);
                            break;
                        case "b":
                            section.setPointB(location);
                            break;
                    }
                } else {
                    point.setPointA(location);
                }
            }

            in.endObject();
            in.endArray();
        }

        in.endObject();
        in.endArray();
        in.endObject();
        return island;
    }

    private void write(final Location location, JsonWriter writer) {
        DataManager.GSON.toJson(location, Location.class, writer);
    }

    private Location toLocation(final TypeAdapter<Location> adapter, final JsonReader reader) {
        try {
            return adapter.read(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

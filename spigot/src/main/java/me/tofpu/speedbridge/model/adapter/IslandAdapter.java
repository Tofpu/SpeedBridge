package me.tofpu.speedbridge.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.IslandProperties;
import me.tofpu.speedbridge.api.island.point.Point;
import me.tofpu.speedbridge.api.island.point.TwoSection;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.model.object.island.IslandImpl;
import org.bukkit.Location;

import java.io.IOException;

// TODO: A WAY TO CONVERT OLD NAMES TO NEW ONES
public class IslandAdapter extends TypeAdapter<Island> {
    @Override
    public void write(JsonWriter out, Island value) throws IOException {
        out.beginObject();

        out.name("slot").value(value.slot());
        out.name("spawn");
        DataManager.GSON.toJson(value.location(), Location.class, out);

        out.name("properties").beginArray().beginObject();

        final IslandProperties properties = value.properties();
        for (int i = 0; i < properties.points().size(); i++) {
            final Point point = properties.points().get(i);

            out.name(i + "").beginArray();
            out.beginObject();
            if (point instanceof TwoSection) {
                final TwoSection section = (TwoSection) point;
                if (!section.hasPointA() || !section.hasPointA()) continue;

                out.name(section.identifier() + "-1");
                write(section.pointA(), out);
                out.name(section.identifier() + "-2");
                write(section.pointB(), out);
            } else {
                out.name(point.identifier());
                write(point.pointA(), out);
            }

            out.endObject();
            out.endArray();
        }

        out.endObject();
        out.endArray();
        out.endObject();
    }

    @Override
    public Island read(JsonReader in) throws IOException {
        final TypeAdapter<Location> adapter = DataManager.GSON.getAdapter(Location.class);

        in.beginObject();
        in.nextName();
        final Island island = new IslandImpl(in.nextInt());

        in.nextName();
        island.location(adapter.read(in));

        in.nextName();
        in.beginArray();
        in.beginObject();

        final IslandProperties properties = island.properties();
        while (in.hasNext()) {
            final String index = in.nextName();
            in.beginArray();
            in.beginObject();


            while (in.hasNext()) {
                final String[] input = in.nextName().split("-");
                final Location location = toLocation(adapter, in);

                if (input[0].equals("selection")) input[0] = "position";
                else if (input[0].equals("point")) input[0] = "endpoint";
                final Point point = properties.get(input[0]);
                if (point instanceof TwoSection) {
                    final TwoSection section = (TwoSection) point;
                    switch (input[1]) {
                        case "a":
                        case "1":
                            section.pointA(location);
                            break;
                        case "b":
                        case "2":
                            section.pointB(location);
                            break;
                    }
                } else {
                    point.pointA(location);
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

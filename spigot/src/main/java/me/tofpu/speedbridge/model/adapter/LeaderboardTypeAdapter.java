package me.tofpu.speedbridge.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.api.model.object.lobby.BoardUser;
import me.tofpu.speedbridge.model.object.leaderboard.BoardUserImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeaderboardTypeAdapter extends TypeAdapter<List<BoardUser>> {
    @Override
    public void write(JsonWriter out, List<BoardUser> value) throws IOException {
        if (value.isEmpty()) return;
        out.beginObject().name("users").beginArray();

        for (final BoardUser user : value) {
            if (user.uniqueId() == null) return;
            out.beginObject();

            out.name("name").value(user.name());
            out.name("uuid").value(user.uniqueId().toString());
            out.name("result").value(user.score());

            out.endObject();
        }

        out.endArray();
        out.endObject();
    }

    @Override
    public List<BoardUser> read(JsonReader in) throws IOException {
        final List<BoardUser> users = new ArrayList<>(10);

        in.beginObject();
        in.nextName();
        in.beginArray();

        BoardUserImpl.Builder builder = new BoardUserImpl.Builder();
        while (in.hasNext()) {
            in.beginObject();

            while (in.hasNext()) {
                if (in.peek() == JsonToken.NULL) continue;
                switch (in.nextName()) {
                    case "name":
                        builder.name(in.nextString());
                        break;
                    case "uuid":
                        builder.uniqueId(UUID.fromString(in.nextString()));
                        break;
                    case "result":
                        if (in.peek() != JsonToken.NULL)
                            builder.result(in.nextDouble());
                        break;
                }
            }
            users.add(builder.build());
            builder = new BoardUserImpl.Builder();

            in.endObject();
        }

        in.endArray();
        in.endObject();
        return users;
    }
}

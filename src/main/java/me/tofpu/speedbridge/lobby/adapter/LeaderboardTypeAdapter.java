package me.tofpu.speedbridge.lobby.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.lobby.leaderboard.data.BoardUser;

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
            if (user.getUuid() == null) return;
            out.beginObject();

            out.name("name").value(user.getName());
            out.name("uuid").value(user.getUuid().toString());
            out.name("result").value(user.getScore());

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

        BoardUser.Builder builder = new BoardUser.Builder();
        while (in.hasNext()) {
            in.beginObject();

            while (in.hasNext()) {
                if (in.peek() == JsonToken.NULL) continue;
                switch (in.nextName()) {
                    case "name":
                        builder.setName(in.nextString());
                        break;
                    case "uuid":
                        builder.setUuid(UUID.fromString(in.nextString()));
                        break;
                    case "result":
                        if (in.peek() != JsonToken.NULL)
                            builder.setResult(in.nextDouble());
                        break;
                }
            }
            users.add(builder.build());
            builder = new BoardUser.Builder();

            in.endObject();
        }

        in.endArray();
        in.endObject();
        return users;
    }
}

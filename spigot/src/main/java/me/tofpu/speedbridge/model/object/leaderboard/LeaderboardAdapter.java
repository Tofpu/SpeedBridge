package me.tofpu.speedbridge.model.object.leaderboard;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.api.lobby.BoardUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeaderboardAdapter extends TypeAdapter<AbstractLeaderboard> {
    @Override
    public void write(final JsonWriter out, final AbstractLeaderboard value) throws IOException {
        out.beginObject();

        out.name("identifier").value(value.identifier());
        out.name("capacity").value(value.capacity());

        out.name("positions").beginArray();
        for (final BoardUser user : value.positions()) {
            if (user.uniqueId() == null) continue;
            out.beginObject();
            out.name("name").value(user.name());
            out.name("uniqueId").value(user.uniqueId().toString());
            out.name("score").value(user.score());
            out.endObject();
        }
        out.endArray();
        out.endObject();
    }

    @Override
    public AbstractLeaderboard read(final JsonReader in) throws IOException {
        in.beginObject();

        String identifier = "";
        int capacity = 0;

        final List<BoardUser> list = new ArrayList<>();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "identifier":
                    identifier = in.nextString();
                    break;
                case "capacity":
                    capacity = in.nextInt();
                    break;
                case "users":
                case "positions":
                    in.beginArray();
                    while (in.hasNext()) {
                        in.beginObject();
                        final BoardUserImpl.Builder builder = new BoardUserImpl.Builder();

                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "name":
                                    builder.name(in.nextString());
                                    break;
                                case "uuid":
                                case "uniqueId":
                                    builder.uniqueId(UUID.fromString(in.nextString()));
                                    break;
                                case "result":
                                case "score":
                                    if (in.peek() != JsonToken.NULL)
                                        builder.result(in.nextDouble());
                                    break;
                            }
                        }
                        in.endObject();
                        list.add(builder.build());
                    }
                    in.endArray();
                    break;
            }
        }
        in.endObject();

        final AbstractLeaderboard leaderboard = new AbstractLeaderboard(identifier, capacity) {};
        leaderboard.addAll(list);

        return leaderboard;
    }
}

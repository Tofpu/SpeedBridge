package me.tofpu.speedbridge.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.UserProperties;
import me.tofpu.speedbridge.api.user.timer.Timer;
import me.tofpu.speedbridge.model.object.user.UserImpl;
import me.tofpu.speedbridge.model.object.user.properties.timer.TimerFactory;

import java.io.IOException;
import java.util.UUID;

public class UserAdapter extends TypeAdapter<User> {
    @Override
    public void write(JsonWriter out, User value) throws IOException {
        out.beginObject();
        out.name("uniqueId").value(value.uniqueId().toString());

        out.name("properties")
                .beginArray()
                .beginObject();
        final UserProperties properties = value.properties();
        final Timer timer = properties.timer();
        out.name("island").value(timer == null ? null : timer.slot());
        out.name("result").value(timer == null ? null : timer.result())
                .endObject()
                .endArray();

        out.endObject();
    }

    @Override
    public User read(JsonReader in) throws IOException {
        in.beginObject();

        in.nextName();
        final User user = new UserImpl(UUID.fromString(in.nextString()));

        in.nextName();
        in.beginArray();
        in.beginObject();
        final UserProperties properties = user.properties();
        in.nextName();

        Integer slot = null;
        if (in.peek() != JsonToken.NULL) slot = in.nextInt();
        else in.nextNull();

        in.nextName();
        Double aDouble = null;
        if (in.peek() != JsonToken.NULL) aDouble = in.nextDouble();
        else in.nextNull();

        if (slot != null && aDouble != null) properties.timer(TimerFactory.of(slot, aDouble));
        in.endObject();
        in.endArray();

        in.endObject();
        return user;
    }
}

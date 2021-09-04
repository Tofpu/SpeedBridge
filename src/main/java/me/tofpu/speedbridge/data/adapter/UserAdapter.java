package me.tofpu.speedbridge.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.UserImpl;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.properties.Timer;

import java.io.IOException;
import java.util.UUID;

public class UserAdapter extends TypeAdapter<User> {
    @Override
    public void write(JsonWriter out, User value) throws IOException {
        out.beginObject();
        out.name("uniqueId").value(value.getUuid().toString());

        out.name("properties")
                .beginArray()
                .beginObject();
        final UserProperties properties = value.getProperties();
        final Timer timer = properties.getTimer();
        out.name("island").value(timer == null ? null : timer.getSlot());
        out.name("result").value(timer == null ? null : timer.getResult())
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
        final UserProperties properties = user.getProperties();
        in.nextName();

        Integer slot = null;
        if (in.peek() != JsonToken.NULL) slot = in.nextInt();
        else in.nextNull();

        in.nextName();
        Double aDouble = null;
        if (in.peek() != JsonToken.NULL) aDouble = in.nextDouble();
        else in.nextNull();

        if (slot != null && aDouble != null) properties.setTimer(new Timer(slot, aDouble));
        in.endObject();
        in.endArray();

        in.endObject();
        return user;
    }
}

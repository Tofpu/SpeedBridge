package me.tofpu.speedbridge.user.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.impl.User;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.timer.Timer;

import java.io.IOException;
import java.util.UUID;

public class UserAdapter extends TypeAdapter<IUser> {
    @Override
    public void write(JsonWriter out, IUser value) throws IOException {
        out.beginObject();
        out.value("uniqueId").value(value.getUuid().toString());

        out.beginArray();
        final UserProperties properties = value.getProperties();
        final Timer timer = properties.getTimer();
        out.value("island").value(timer.getSlot());
        out.value("result").value(timer.getResult());
        out.endArray();

        out.endObject();
    }

    @Override
    public IUser read(JsonReader in) throws IOException {
        in.beginObject();

        in.nextName();
        final IUser user = new User(UUID.fromString(in.nextString()));

        in.beginArray();
        final UserProperties properties = user.getProperties();
        in.nextName();
        final int slot = in.nextInt();

        in.nextName();
        final Timer timer = new Timer(slot, in.nextDouble());
        properties.setTimer(timer);
        in.endArray();

        in.endObject();
        return null;
    }
}

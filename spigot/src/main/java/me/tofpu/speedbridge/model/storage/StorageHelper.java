package me.tofpu.speedbridge.model.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import me.tofpu.speedbridge.model.adapter.UserAdapter;

public class StorageHelper {
    private final Gson gson;

    @Inject
    public StorageHelper(final UserAdapter userAdapter) {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(UserAdapter.class, userAdapter)
                .create();
    }

    public Gson gson() {
        return this.gson;
    }
}

package me.tofpu.speedbridge.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.data.adapter.location.LocationAdapter;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.adapter.IslandAdapter;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.adapter.UserAdapter;
import me.tofpu.speedbridge.user.service.IUserService;
import org.bukkit.Location;

import java.io.File;
import java.util.UUID;

public class DataManager {
    private final Gson gson;

    private final TypeAdapter<IIsland> islandAdapter;
    private final TypeAdapter<IUser> userAdapter;

    private final IIslandService islandService;
    private final IUserService userService;

    private final File[] files;

    public DataManager(@NotNull final File parentDirectory, @NotNull final IIslandService islandService, @NotNull final IUserService userService) {
        this.files = new File[3];

        this.files[0] = parentDirectory;
        this.files[1] = new File(parentDirectory, "islands");
        this.files[2] = new File(parentDirectory, "users");

        this.islandService = islandService;
        this.userService = userService;

        TypeAdapter<Location> locationAdapter = new LocationAdapter();
        this.islandAdapter = new IslandAdapter(locationAdapter);
        this.userAdapter = new UserAdapter();

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Location.class, locationAdapter)
                .registerTypeAdapter(IIsland.class, islandAdapter)
                .create();
    }

    public void initialize() {
        for (final File file : files) {
            if (!file.exists()) file.mkdirs();
        }
    }

    public void loadIslands() {
        islandService.loadAll(islandAdapter, files[1]);
    }

    public IUser loadUser(@NotNull final UUID uuid) {
        if (!files[0].exists()) return null;
        return userService.load(userAdapter, uuid, files[2]);
    }

    public void unloadUser(@NotNull final UUID uuid) {
        final IUser user = userService.searchForUUID(uuid);
        if (user == null) return;
        userService.removeUser(user);
    }

    public void save() {
        islandService.saveAll(islandAdapter, files[1]);
        userService.saveAll(userAdapter, files[2]);
    }
}

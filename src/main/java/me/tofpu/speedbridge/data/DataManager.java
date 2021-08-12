package me.tofpu.speedbridge.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.tofpu.speedbridge.data.adapter.location.LocationAdapter;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.adapter.IslandAdapter;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.lobby.adapter.LeaderboardTypeAdapter;
import me.tofpu.speedbridge.lobby.leaderboard.data.BoardUser;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.adapter.UserAdapter;
import me.tofpu.speedbridge.user.service.IUserService;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DataManager {
    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .registerTypeAdapter(IIsland.class, new IslandAdapter())
            .registerTypeAdapter(IUser.class, new UserAdapter())
            .registerTypeAdapter(new TypeToken<List<BoardUser>>() {
            }.getType(), new LeaderboardTypeAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final IIslandService islandService;
    private final IUserService userService;
    private final ILobbyService lobbyService;

    private final File[] files;

    public DataManager(final File parentDirectory, final IIslandService islandService, final IUserService userService, final ILobbyService lobbyService) {
        this.files = new File[5];

        this.files[0] = parentDirectory;
        this.files[1] = new File(parentDirectory, "islands");
        this.files[2] = new File(parentDirectory, "users");
        this.files[3] = new File(parentDirectory, "lobby.json");
        this.files[4] = new File(parentDirectory, "leaderboard.json");

        this.islandService = islandService;
        this.userService = userService;
        this.lobbyService = lobbyService;
    }

    public void initialize() {
        for (final File file : files) {
            if (!file.exists()) {
                if (file.getName().contains(".")) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else file.mkdirs();
            }
        }
    }

    public IUser loadUser(final UUID uuid) {
        if (!files[0].exists()) return null;
        IUser user = userService.load(GSON, uuid, files[2]);
        return user == null ? userService.createUser(uuid) : user;
    }

    public void unloadUser(final UUID uuid) {
        final IUser user = userService.searchForUUID(uuid);
        if (user == null) return;
        userService.removeUser(user);
    }

    public void load() {
        lobbyService.load(GSON, files[3], files[4]);
        islandService.loadAll(GSON, files[1]);
    }

    public void save() {
        islandService.saveAll(GSON, files[1]);
        userService.saveAll(GSON, files[2]);
        lobbyService.save(GSON, files[3], files[4]);
    }
}

package me.tofpu.speedbridge.data;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.fileutil.file.PluginFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.data.adapter.LocationAdapter;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.data.file.path.type.PathType;
import me.tofpu.speedbridge.data.file.type.MessageFile;
import me.tofpu.speedbridge.data.file.type.SettingsFile;
import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.data.adapter.IslandAdapter;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.data.adapter.LeaderboardTypeAdapter;
import me.tofpu.speedbridge.lobby.leaderboard.data.BoardUser;
import me.tofpu.speedbridge.lobby.service.LobbyService;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.data.adapter.UserAdapter;
import me.tofpu.speedbridge.user.service.UserService;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DataManager {
    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .registerTypeAdapter(Island.class, new IslandAdapter())
            .registerTypeAdapter(User.class, new UserAdapter())
            .registerTypeAdapter(new TypeToken<List<BoardUser>>() {
            }.getType(), new LeaderboardTypeAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final IslandService islandService;
    private final UserService userService;
    private final LobbyService lobbyService;

    private final File[] files;
    private final PluginFile[] pluginFiles;

    public DataManager(final IslandService islandService, final UserService userService, final LobbyService lobbyService) {
        this.files = new File[5];
        this.pluginFiles = new PluginFile[2];

        this.islandService = islandService;
        this.userService = userService;
        this.lobbyService = lobbyService;
    }

    public void initialize(final Plugin plugin, final File parentDirectory) {
        this.files[0] = parentDirectory;
        this.files[1] = new File(parentDirectory, "islands");
        this.files[2] = new File(parentDirectory, "users");
        this.files[3] = new File(parentDirectory, "lobby.json");
        this.files[4] = new File(parentDirectory, "leaderboard.json");

        this.pluginFiles[0] = new SettingsFile(plugin, parentDirectory);
        this.pluginFiles[1] = new MessageFile(plugin, parentDirectory);

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

        for (final PluginFile file : pluginFiles) {
            file.initialize(false);
        }
    }

    public void reload(){
        // TODO: combine file & config, it's basically the same
        for (final PathType type : PathType.values()){
            final String name = type.name().toLowerCase(Locale.ROOT);
            ConfigAPI.get(name).configuration(YamlConfiguration.loadConfiguration(new File(files[0], name + ".yml")));
        }

        for (final Path.Value<?> value : Path.values()){
            value.reload();
        }
    }

    public User loadUser(final UUID uuid) {
        if (!files[0].exists()) return null;
        User user = userService.load(GSON, uuid, files[2]);
        return user == null ? userService.createUser(uuid) : user;
    }

    public void unloadUser(final UUID uuid) {
        final User user = userService.searchForUUID(uuid);
        if (user == null) return;

        userService.save(GSON, user, files[2]);
        userService.removeUser(user);
    }

    public void load() {
        new BukkitRunnable() {
            @Override
            public void run() {
                lobbyService.load(GSON, files[3], files[4]);
                islandService.loadAll(files[1]);
            }
        }.runTaskAsynchronously(SpeedBridge.getProvidingPlugin(SpeedBridge.class));
//        Executors.newFixedThreadPool(1).submit(new Runnable() {
//            @Override
//            public void run() {
//                lobbyService.load(GSON, files[3], files[4]);
//                islandService.loadAll(GSON, files[1]);
//            }
//        });
    }

    public void save() {
        islandService.saveAll(files[1]);
        userService.saveAll(GSON, files[2]);
        lobbyService.save(GSON, files[3], files[4]);
    }

    public PluginFile[] getPluginFiles() {
        return pluginFiles;
    }
}

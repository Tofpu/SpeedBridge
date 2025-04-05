package me.tofpu.speedbridge.data;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.fileutil.file.PluginFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.IslandService;
import me.tofpu.speedbridge.api.leaderboard.LeaderboardService;
import me.tofpu.speedbridge.api.leaderboard.LeaderboardType;
import me.tofpu.speedbridge.api.lobby.LobbyService;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.UserService;
import me.tofpu.speedbridge.data.adapter.IslandAdapter;
import me.tofpu.speedbridge.data.adapter.LocationAdapter;
import me.tofpu.speedbridge.data.adapter.UserAdapter;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.data.file.path.PathType;
import me.tofpu.speedbridge.data.file.type.MessageFile;
import me.tofpu.speedbridge.data.file.type.SettingsFile;
import me.tofpu.speedbridge.game.leaderboard.AbstractLeaderboard;
import me.tofpu.speedbridge.game.leaderboard.LeaderboardAdapter;
import me.tofpu.speedbridge.game.leaderboard.LeaderboardServiceImpl;
import me.tofpu.speedbridge.island.service.IslandServiceImpl;
import me.tofpu.speedbridge.user.service.UserServiceImpl;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class DataManager {
    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .registerTypeAdapter(Island.class, new IslandAdapter())
            .registerTypeAdapter(User.class, new UserAdapter())
            .registerTypeAdapter(AbstractLeaderboard.class, new LeaderboardAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final Plugin plugin;
    private final File[] files;
    private final PluginFile[] pluginFiles;

    private IslandServiceImpl islandService;
    private UserServiceImpl userService;
    private LobbyService lobbyService;
    private LeaderboardServiceImpl leaderboardService;

    public DataManager(final Plugin plugin) {
        this.plugin = plugin;
        this.files = new File[5];
        this.pluginFiles = new PluginFile[2];
    }

    public void initialize(final IslandService islandService, final UserService userService, final LobbyService lobbyService, final LeaderboardServiceImpl leaderboardService, final Plugin plugin, final File parentDirectory) {
        this.islandService = (IslandServiceImpl) islandService;
        this.userService = (UserServiceImpl) userService;
        this.lobbyService = lobbyService;
        this.leaderboardService = leaderboardService;

        this.files[0] = parentDirectory;
        this.files[1] = new File(parentDirectory, "islands");
        this.files[2] = new File(parentDirectory, "users");
        this.files[3] = new File(parentDirectory, "leaderboards");
        this.files[4] = new File(parentDirectory, "lobby.json");
//        this.files[4] = new File(parentDirectory, "leaderboard.json");

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

    public void save(){
        // TODO: combine file & config, it's basically the same
        for (final PathType type : PathType.values()){
            final String name = type.name().toLowerCase(Locale.ROOT);
            try {
                ConfigAPI.get(name).configuration().save(new File(files[0], name + ".yml"));
            } catch (IllegalArgumentException | IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public User loadUser(final UUID uuid) {
        final User user = userService.load(uuid);

        return user == null ? userService.createUser(uuid) : user;
    }

    public void unloadUser(final UUID uuid) {
        final User user = userService.get(uuid);
        if (user == null) return;

        userService.save(user);
        userService.removeUser(user);
    }

    public void load() throws IOException {
        lobbyService.load(GSON, files[4]);
        islandService.loadAll();

        final File file = new File(getFiles()[0], "leaderboard.json");
        if (file.exists()) {
            try (final FileReader reader = new FileReader(file)) {
                plugin.getLogger().info("Migrating your outdated leaderboard to the new leaderboard system now...");
                leaderboardService.get(LeaderboardType.GLOBAL).addAll(GSON.fromJson(reader, AbstractLeaderboard.class).positions());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            file.delete();
        }
        leaderboardService.load();
    }

    public void shutdown() {
        islandService.saveAll(true);
        userService.saveAll(true);
        saveLobbyService();
        leaderboardService.save();
    }

    public void saveLobbyService() {
        lobbyService.save(GSON, files[4]);
    }

    public File[] getFiles() {
        return files;
    }

    public PluginFile[] getPluginFiles() {
        return pluginFiles;
    }
}

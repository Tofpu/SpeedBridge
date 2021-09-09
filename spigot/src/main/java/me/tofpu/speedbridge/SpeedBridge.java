package me.tofpu.speedbridge;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.identifier.ConfigIdentifier;
import com.github.requestpluginsforfree.dependency.api.DependencyAPI;
import com.github.requestpluginsforfree.dependency.impl.PlaceholderDependency;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.expansion.BridgeExpansion;
import me.tofpu.speedbridge.game.Game;
import me.tofpu.speedbridge.island.mode.ModeManager;
import me.tofpu.speedbridge.util.UpdateChecker;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpeedBridge extends JavaPlugin {
    private final Game game;

    public SpeedBridge() {
        this.game = new Game(this);
    }

    @Override
    public void onLoad() {
        // initializing phase
        initialize();
    }

    @Override
    public void onEnable() {
        // loading phase
        load();
    }

    @Override
    public void onDisable() {
        // saving phase
        game().dataManager().save();

        // cancelling the leaderboard task
        game().lobbyService().getLeaderboard().cancel();
    }

    private void initialize(){
        final DataManager dataManager = game().dataManager();

        // update checker on async
        UpdateChecker.init(this, 95918).requestUpdateCheck().whenComplete((updateResult, throwable) -> {
            if (updateResult.getReason() == UpdateChecker.UpdateReason.NEW_UPDATE) {
                getLogger().warning("You're not on the latest version of SpeedBridge!");
                getLogger().warning("It's highly recommended to download the latest version at https://www.spigotmc.org/resources/speedbridge-1-free-bridge-trainer-rpf.95918/!");
            } else if (updateResult.getReason() == UpdateChecker.UpdateReason.UP_TO_DATE) {
                getLogger().warning("You're using the latest version of SpeedBridge!");
            }
        });
        // game initializing
        game().initialize();

        final ConfigIdentifier settingsIdentifier = ConfigIdentifier.of(
                "settings",
                dataManager.getPluginFiles()[0].configuration()
        );
        final ConfigIdentifier messagesIdentifier = ConfigIdentifier.of(
                "messages",
                dataManager.getPluginFiles()[1].configuration()
        );

        // initializing the two configs
        ConfigAPI.initialize(settingsIdentifier, messagesIdentifier);

        // initializing the mode manager
        ModeManager.getModeManager().initialize();

        initializePlaceholderApi();
    }

    private void load(){
        // starting the loading phase
        game.load();

        game.dataManager().load();

        // reload patch
        Bukkit.getOnlinePlayers().forEach(player -> game().dataManager().loadUser(player.getUniqueId()));

        // starting up the leaderboard
        game().lobbyService().getLeaderboard().start(this);
    }

    private void initializePlaceholderApi() {
        // registering the placeholder dependency
        DependencyAPI.register(new PlaceholderDependency());

        // initializing the dependency api
        DependencyAPI.initialize(this);

        // if it's not available, return
        if (!DependencyAPI.get("PlaceholderAPI").isAvailable()) return;

        Util.isPlaceholderHooked = true;
        new BridgeExpansion(getDescription(), game().userService(), game().gameService(), game().lobbyService()).register();
    }

    public Game game() {
        return this.game;
    }
}

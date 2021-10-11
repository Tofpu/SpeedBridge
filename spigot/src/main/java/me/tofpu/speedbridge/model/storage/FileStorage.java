package me.tofpu.speedbridge.model.storage;

import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.lobby.Lobby;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.repository.IslandRepository;
import me.tofpu.speedbridge.api.model.repository.LobbyRepository;
import me.tofpu.speedbridge.api.model.repository.UserRepository;
import me.tofpu.speedbridge.api.model.storage.Storage;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class FileStorage implements Storage {
    private final File[] files = new File[4];

    private final StorageHelper helper;
    private final LobbyRepository lobbyRepository;
    private final IslandRepository islandRepository;
    private final UserRepository userRepository;

    @Inject
    public FileStorage(final StorageHelper helper, final Plugin plugin, final LobbyRepository lobbyRepository, final IslandRepository islandRepository, final UserRepository userRepository) {
        this.helper = helper;
        this.lobbyRepository = lobbyRepository;
        this.islandRepository = islandRepository;
        this.userRepository = userRepository;

        this.files[0] = plugin.getDataFolder();
        this.files[1] = new File(files[0], "lobby.json");
        this.files[2] = new File(files[0], "islands");
        this.files[3] = new File(files[0], "users");

        for (final File file : files) {
            if (file.getName().contains(".")) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                file.mkdir();
            }
        }
    }

    @Override
    public Optional<Lobby> loadLobby() {
        final Lobby lobby;
        try (final FileReader reader = new FileReader(files[1])) {
            lobby = helper.gson().fromJson(reader, Lobby.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return Optional.ofNullable(lobby);
    }

    @Override
    public Collection<Island> loadIslands() {
        final List<Island> islands = new ArrayList<>();
        for (final File file : files[2].listFiles()) {
            try {
                if (!file.getName().endsWith(".json")) continue;

                final Island island;
                try (final FileReader reader = new FileReader(file)) {
                    island = helper.gson().fromJson(reader, Island.class);;
                }
                if (island == null) continue;

                islands.add(island);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return islands;
    }

    @Override
    public Optional<User> loadUser(final UUID uniqueId) {
        final File file = new File(files[3], uniqueId.toString());
        if (!file.exists()) {
            return Optional.empty();
        }

        final User user;
        try (final FileReader reader = new FileReader(file)) {
            user = helper.gson().fromJson(reader, User.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void saveUser(final User user) {
        final File file = new File(files[3], user.uniqueId().toString());
        try {
            try (final FileWriter writer = new FileWriter(file, false)) {
                this.helper.gson().toJson(user, User.class, writer);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void saveLobby() {
        try (final FileWriter writer = new FileWriter(files[1], false)) {
            this.helper.gson().toJson(lobbyRepository.lobby(), Lobby.class, writer);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void deleteIsland(final int slot) {
        final File islandFile = new File(files[2], "island-" + slot +
                ".json");
        try {
            Files.deleteIfExists(islandFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveIslands() {
        for (final Island island : islandRepository.islands()) {
            final File file = new File(files[2], island.slot() + "");
            try (final FileWriter writer = new FileWriter(file, false)) {
                this.helper.gson().toJson(island, Island.class, writer);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public void saveUsers() {
        for (final User user : userRepository.users()) {
            saveUser(user);
        }
    }
}

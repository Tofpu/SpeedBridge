package me.tofpu.speedbridge.island;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.Location;

public interface IIsland {
    public boolean isAvailable();

    @Nullable
    public IUser getTakenBy();

    public void setTakenBy(@NotNull final IUser takenBy);

    @Nullable
    public Location getLocation();

    public void setLocation(@NotNull final Location location);

    public boolean hasLocation();

    @NotNull
    public int getSlot();

    @NotNull
    public IslandProperties getProperties();
}

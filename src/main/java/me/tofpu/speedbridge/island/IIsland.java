package me.tofpu.speedbridge.island;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.Location;

public interface IIsland {
    public void setTakenBy(@NotNull final IUser takenBy);

    public void setLocation(@NotNull final Location location);

    public boolean isAvailable();

    @Nullable
    public IUser getTakenBy();

    @Nullable
    public Location getLocation();

    public boolean hasLocation();

    @NotNull
    public int getSlot();
}

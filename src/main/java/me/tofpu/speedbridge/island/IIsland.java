package me.tofpu.speedbridge.island;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.Location;

public interface IIsland {
    public void setTakenBy(@NotNull IUser takenBy);

    public void setLocation(@NotNull Location location);

    @NotNull
    public boolean isAvailable();

    @Nullable
    public IUser getTakenBy();

    @Nullable
    public Location getLocation();

    @NotNull
    public int getSlot();
}

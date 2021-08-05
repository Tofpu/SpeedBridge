package me.tofpu.speedbridge.island;

import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.Location;

public interface IIsland {
    public boolean isAvailable();


    public IUser getTakenBy();

    public void setTakenBy(final IUser takenBy);


    public Location getLocation();

    public void setLocation(final Location location);

    public boolean hasLocation();


    public int getSlot();


    public IslandProperties getProperties();
}

package me.tofpu.speedbridge.process.item;

import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.process.type.GameItemProcessor;
import me.tofpu.speedbridge.util.Util;
import me.tofpu.speedbridge.util.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpectatorItemProcessor extends GameItemProcessor {
    @Override
    public void process(final User user, final Player player,
            final ProcessType type) {
        final Inventory inventory = player.getInventory();
        // clearing this user inventory
        inventory.clear();

        // if the process type is not process, we're done here
        if (type != ProcessType.PROCESS) return;

        final ItemStack itemStack = XMaterial.RED_DYE.parseItem();
        final ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Util.colorize("&cLeave"));
        itemStack.setItemMeta(meta);

        inventory.setItem(0, itemStack);
    }
}

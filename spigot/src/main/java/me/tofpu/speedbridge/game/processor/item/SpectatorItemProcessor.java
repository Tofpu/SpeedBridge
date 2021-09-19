package me.tofpu.speedbridge.game.processor.item;

import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.game.processor.ProcessType;
import me.tofpu.speedbridge.game.processor.type.GameItemProcessor;
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
        inventory.clear();

        if (type == ProcessType.PROCESS) {
            final ItemStack itemStack = XMaterial.RED_DYE.parseItem();
            final ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(Util.colorize("&cLeave"));
            itemStack.setItemMeta(meta);

            inventory.setItem(0, itemStack);
        }
    }
}

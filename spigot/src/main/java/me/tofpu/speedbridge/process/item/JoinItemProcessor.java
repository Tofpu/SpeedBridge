package me.tofpu.speedbridge.process.item;

import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.process.type.GameItemProcessor;
import me.tofpu.speedbridge.util.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class JoinItemProcessor extends GameItemProcessor {
    @Override
    public void process(final User user, final Player player,
            final ProcessType type) {
        final Inventory inventory = player.getInventory();
        // clearing this user inventory
        inventory.clear();

        // if the process type is not process, we're done here
        if (type != ProcessType.PROCESS) return;

        // trying to get the material that matches the server version
        final Optional<XMaterial> material = XMaterial.matchXMaterial(Path.SETTINGS_BLOCK.getValue());

        // if the material is present
        if (material.isPresent()){
            // parsing the material chosen
            inventory.addItem(new ItemStack(material.get().parseMaterial(), 64));
        } else {
            // default material
            inventory.addItem(new ItemStack(XMaterial.WHITE_WOOL.parseMaterial(), 64));
        }
    }
}

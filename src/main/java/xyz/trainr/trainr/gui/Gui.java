package xyz.trainr.trainr.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class Gui {

    private final Inventory inventory;
    private final Set<GuiButton> buttons;
    private final GuiListener guiListener;

    Gui(int size, String title, Set<GuiButton> buttons, ItemStack[] contents) {
        this.inventory = Bukkit.createInventory(new GuiInventoryHolder(), size, title);
        this.buttons = buttons;
        this.guiListener = new GuiListener(this);

        setItems(contents);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    private void setItems(ItemStack[] contents) {
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) {
                continue;
            }

            inventory.setItem(i, item);
        }

        for (GuiButton button : buttons) {
            inventory.setItem(button.getSlot(), button.getItemStack());
        }
    }

    GuiInventoryHolder getInventoryHolder() {
        return (GuiInventoryHolder) inventory.getHolder();
    }

    public Set<GuiButton> getButtons() {
        return Collections.unmodifiableSet(buttons);
    }

    static class GuiInventoryHolder implements InventoryHolder {
        private final UUID uuid = UUID.randomUUID();

        @Override
        public Inventory getInventory() {
            return null;
        }

        public UUID getUuid() {
            return uuid;
        }
    }

}

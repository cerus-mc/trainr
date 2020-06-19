package xyz.trainr.trainr.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.trainr.trainr.Trainr;

/**
 * Represents the event listener of a GUI
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
class GUIListener implements Listener {

    // Define the GUI of this listener
    private final GUI gui;

    /**
     * Creates a new GUI event listener
     *
     * @param gui The GUI this listener relates to
     */
    public GUIListener(GUI gui) {
        this.gui = gui;

        Trainr plugin = JavaPlugin.getPlugin(Trainr.class);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() == null) {
            return;
        }

        if (!(inventory.getHolder() instanceof GUI.GUIInventoryHolder)) {
            return;
        }

        if (!((GUI.GUIInventoryHolder) inventory.getHolder())
                .getUUID().equals(gui.getInventoryHolder().getUUID())) {
            return;
        }

        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        if (clickedInventory.getHolder() == null) {
            return;
        }

        if (!(clickedInventory.getHolder() instanceof GUI.GUIInventoryHolder)) {
            return;
        }

        if (!((GUI.GUIInventoryHolder) clickedInventory.getHolder())
                .getUUID().equals(gui.getInventoryHolder().getUUID())) {
            return;
        }

        int slot = event.getSlot();
        boolean present = gui.getButtons().stream()
                .filter(guiButton -> guiButton.getSlot() == slot)
                .peek(button -> event.setCancelled(button.getClickCallback()
                        .apply((Player) event.getWhoClicked())))
                .findAny().isPresent();
        if (present) {
            return;
        }

        if (gui.getItemPolicy() == ItemPolicy.DENY_CLICK) {
            event.setCancelled(true);
        }
    }

}

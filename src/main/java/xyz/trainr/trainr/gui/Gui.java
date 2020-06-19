package xyz.trainr.trainr.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Represents a general GUI
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class GUI {

    // Define local variables
    private final Inventory inventory;
    private final Set<GUIButton> buttons;
    private final ItemPolicy itemPolicy;

    /**
     * Creates a new GUI
     *
     * @param size       The size of the GUI
     * @param title      The title of the GUI
     * @param buttons    A set of buttons
     * @param contents   A set of item stacks for the content
     * @param itemPolicy The default item policy
     */
    GUI(int size, String title, Set<GUIButton> buttons, ItemStack[] contents, ItemPolicy itemPolicy) {
        this.itemPolicy = itemPolicy;
        this.inventory = Bukkit.createInventory(new GUIInventoryHolder(), size, title);
        this.buttons = buttons;

        new GUIListener(this);

        setItems(contents);
    }

    /**
     * Opens the inventory for a specific player
     *
     * @param player The player who should receive the GUI
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Sets the items for the inventory
     *
     * @param contents The items to use
     */
    private void setItems(ItemStack[] contents) {
        // Set the contents into the inventory
        IntStream.range(0, contents.length).forEach(index -> {
            ItemStack item = contents[index];
            if (item != null) {
                inventory.setItem(index, item);
            }
        });

        // Set the buttons into the inventory
        buttons.forEach(button ->
                inventory.setItem(button.getSlot(), button.getItemStack())
        );
    }

    /**
     * @return The {@link InventoryHolder} of this GUI
     */
    GUIInventoryHolder getInventoryHolder() {
        return (GUIInventoryHolder) inventory.getHolder();
    }

    /**
     * @return A set of buttons
     */
    public Set<GUIButton> getButtons() {
        return Collections.unmodifiableSet(buttons);
    }

    /**
     * @return The default item policy
     */
    public ItemPolicy getItemPolicy() {
        return itemPolicy;
    }

    /**
     * Represents the GUI {@link InventoryHolder}
     */
    static class GUIInventoryHolder implements InventoryHolder {

        // Define the UUID of the inventory holder
        private final UUID uuid = UUID.randomUUID();

        @Override
        public Inventory getInventory() {
            return null;
        }

        /**
         * @return The UUID of the inventory holder
         */
        public UUID getUUID() {
            return uuid;
        }

    }

}

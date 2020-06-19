package xyz.trainr.trainr.gui;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Helps to build GUIs
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class GUIBuilder {

    // Define local variables
    private int size;
    private String title;
    private final Set<GUIButton> buttons;
    private ItemStack[] contents;
    private ItemPolicy itemPolicy;

    /**
     * Creates a new GUI builder
     */
    public GUIBuilder() {
        this.size = 6 * 9;
        this.title = "My GUI";
        this.buttons = new HashSet<>();
        this.contents = new ItemStack[6 * 9];
        this.itemPolicy = ItemPolicy.ALLOW_CLICK;
    }

    /**
     * Sets the size of the inventory
     *
     * @param size The size of the inventory
     * @return The new GUI builder state
     */
    public GUIBuilder setSize(int size) {
        this.size = size;
        this.contents = Arrays.copyOf(contents, size);
        return this;
    }

    /**
     * Sets an item into a specific slot
     *
     * @param slot The slot the item should be set to
     * @param item The item which should get added
     * @return The new GUI builder state
     */
    public GUIBuilder setItem(int slot, ItemStack item) {
        this.contents[slot] = item;
        return this;
    }

    /**
     * Sets the title of the GUI
     *
     * @param title The title of the GUI
     * @return The new GUI builder state
     */
    public GUIBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Adds a button to the GUI
     *
     * @param button The button which should get added
     * @return The new GUI builder state
     */
    public GUIBuilder addButton(GUIButton button) {
        buttons.add(button);
        return this;
    }

    /**
     * Sets the default item policy
     *
     * @param itemPolicy The default item policy
     * @return The new GUI builder state
     */
    public GUIBuilder setItemPolicy(ItemPolicy itemPolicy) {
        this.itemPolicy = itemPolicy;
        return this;
    }

    /**
     * Fills the inventory with one item
     *
     * @param itemStack The item to fill the inventory with
     * @param overwrite Whether or not old items should get overwritten
     * @return The new GUI builder state
     */
    public GUIBuilder fill(ItemStack itemStack, boolean overwrite) {
        IntStream.range(0, contents.length).forEach(index -> {
            ItemStack old = contents[index];
            if (old == null || overwrite) {
                contents[index] = itemStack;
            }
        });
        return this;
    }

    /**
     * Builds the final GUI
     *
     * @return The built GUI
     */
    public GUI build() {
        return new GUI(size, title, buttons, contents, itemPolicy);
    }

}

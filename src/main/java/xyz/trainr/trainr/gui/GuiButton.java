package xyz.trainr.trainr.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

/**
 * Represents a GUI button
 *
 * @author Cerus
 * @version 1.0.0
 * @since 1.0.0
 */
public class GUIButton implements GUIElement {

    // Define local variables
    private final int slot;
    private final ItemStack itemStack;
    private Function<Player, Boolean> clickCallback;

    /**
     * Creates a new GUI button
     *
     * @param slot      The slot the button should be set to
     * @param itemStack The item of the button
     */
    public GUIButton(int slot, ItemStack itemStack) {
        this(slot, itemStack, null);
    }

    /**
     * Creates a new GUI button
     *
     * @param slot          The slot the button should be set to
     * @param itemStack     The item of the button
     * @param clickCallback The callback that gets called when the button gets clicked
     */
    public GUIButton(int slot, ItemStack itemStack, Function<Player, Boolean> clickCallback) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.clickCallback = clickCallback;
    }

    /**
     * @return The slot of thr button
     */
    public int getSlot() {
        return slot;
    }

    /**
     * @return The item of the button
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Sets the callback that gets called when the button gets clicked
     *
     * @param clickCallback the callback that gets called when the button gets clicked
     */
    public void setClickCallback(Function<Player, Boolean> clickCallback) {
        this.clickCallback = clickCallback;
    }

    /**
     * @return the callback that gets called when the button gets clicked
     */
    public Function<Player, Boolean> getClickCallback() {
        return clickCallback;
    }

}

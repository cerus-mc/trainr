package xyz.trainr.trainr.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class GuiButton implements GuiElement {

    private final int slot;
    private final ItemStack itemStack;
    private Function<Player, Boolean> clickCallback;

    public GuiButton(int slot, ItemStack itemStack) {
        this(slot, itemStack, null);
    }

    public GuiButton(int slot, ItemStack itemStack, Function<Player, Boolean> clickCallback) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.clickCallback = clickCallback;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setClickCallback(Function<Player, Boolean> clickCallback) {
        this.clickCallback = clickCallback;
    }

    public Function<Player, Boolean> getClickCallback() {
        return clickCallback;
    }

}

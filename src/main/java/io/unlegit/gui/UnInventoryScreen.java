package io.unlegit.gui;

import java.util.ArrayList;

import io.unlegit.tracker.PlayerTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.CraftingRecipeBookComponent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/** Non-editable version of the Inventory with tweaks */
public class UnInventoryScreen extends AbstractRecipeBookScreen<InventoryMenu>
{
    private float xMouse, yMouse;
    private Player player;
    
    public UnInventoryScreen(Player player)
    {
        super(getMenu(player), new CraftingRecipeBookComponent(getMenu(player)), getInventory(player), Component.translatable("container.crafting"));
        this.player = player;
        titleLabelX = 97;
    }
    
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j)
    {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 4210752, false);
    }

    public void render(GuiGraphics guiGraphics, int i, int j, float f)
    {
        super.render(guiGraphics, i, j, f);
        renderTooltip(guiGraphics, i, j);
        xMouse = i; yMouse = j;
        
        if (PlayerTracker.hasChanged())
            minecraft.setScreen(new UnInventoryScreen(player));
    }
    
    protected void renderBg(GuiGraphics graphics, float f, int i, int j)
    {
        int k = leftPos;
        int l = topPos;
        graphics.blit(RenderType::guiTextured, INVENTORY_LOCATION, k, l, 0, 0, imageWidth, imageHeight, 256, 256);
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, k + 26, l + 8, k + 75, l + 78, 30, 0.0625F, xMouse, yMouse, player);
    }
    
    public static Inventory getInventory(Player player)
    {
        ArrayList<ItemStack> items = PlayerTracker.get().items.get(player);
        Inventory inventory = new Inventory(player);
        int i = 0;
        
        if (items != null)
        {
            for (ItemStack item : items)
                inventory.add(item.copy());
        }
        
        for (ItemStack armor : player.getInventory().armor)
        {
            if (armor != null)
                inventory.armor.set(i, armor.copy());
            i++;
        }
        
        return inventory;
    }
    
    public static InventoryMenu getMenu(Player player)
    {
        return new InventoryMenu(getInventory(player), true, player);
    }
    
    public boolean mouseClicked(double d, double e, int i) { return false; }
    public boolean mouseReleased(double d, double e, int i) { return false; }
    
    public boolean keyPressed(int i, int j, int k)
    {
        if (i == 256 && shouldCloseOnEsc())
        {
            onClose();
            return true;
        } return false;
    }

    @Override
    protected @NotNull ScreenPosition getRecipeBookButtonPosition()
    {
        return new ScreenPosition(this.leftPos + 104, this.height / 2 - 22);
    }
}

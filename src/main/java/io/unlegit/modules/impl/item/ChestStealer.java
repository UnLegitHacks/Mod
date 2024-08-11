package io.unlegit.modules.impl.item;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.ElapTime;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Items;

@IModule(name = "Chest Stealer", description = "Automatically steals chests.")
public class ChestStealer extends ModuleU
{
    public SliderSetting minDelay = new SliderSetting("Min Delay (ms)", "The minimum delay in randomization.", 0, 50, 1000),
                         maxDelay = new SliderSetting("Max Delay (ms)", "The maximum delay in randomization.", 0, 100, 1000);
    
    public ToggleSetting autoClose = new ToggleSetting("Auto Close", "Automatically closes the chest.", true);
                         // Coming soon: autoOpenChests = new ToggleSetting("Chest Aura", "Automatically opens chests around you.", false);
    
    private ElapTime elapTime = new ElapTime();
    public int delay = 0;
    
    public void onUpdate()
    {
        if (mc.player.hasContainerOpen() && mc.player.containerMenu instanceof ChestMenu)
        {
            ChestMenu chestMenu = (ChestMenu) mc.player.containerMenu;
            boolean foundItems = false;
            
            for (int i = 0; i < 36; i++)
            {
                if (chestMenu.getSlot(i) != null && !chestMenu.getSlot(i).getItem().is(Items.AIR))
                {
                    if (elapTime.passed(delay))
                    {
                        mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, i, 0, ClickType.QUICK_MOVE, mc.player);
                        delay = updateDelay();
                    }
                    
                    if (!foundItems) foundItems = true;
                }
            }
            
            if (autoClose.enabled && !foundItems) mc.player.closeContainer();
        }
    }
    
    private int updateDelay()
    {
        int min = (int) minDelay.currentValue, max = (int) maxDelay.currentValue;
        return (int) (min + (max - min) * Math.random());
    }
}

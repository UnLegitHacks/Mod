package io.unlegit.modules.impl.item;

import java.util.ArrayList;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.ElapTime;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Items;

@IModule(name = "Chest Stealer", description = "Automatically steals chests.")
public class ChestStealer extends ModuleU
{
    public SliderSetting minDelay = new SliderSetting("Min Delay (ms)", "The minimum delay in randomization.", 0, 50, 1000),
                         maxDelay = new SliderSetting("Max Delay (ms)", "The maximum delay in randomization.", 0, 100, 1000);
    
    public ToggleSetting autoClose = new ToggleSetting("Auto Close", "Automatically closes the chest.", true);
    private ArrayList<BlockPos> lootedChests = new ArrayList<>();
    private ElapTime elapTime = new ElapTime();
    public int delay = 0;
    
    public void onUpdate()
    {
        if (mc.player.hasContainerOpen() && mc.player.containerMenu instanceof ChestMenu chest)
        {
            boolean foundItems = false;
            
            for (int i = 0; i < 36; i++)
            {
                if (chest.getSlot(i) != null && !chest.getSlot(i).getItem().is(Items.AIR))
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
        int min = (int) minDelay.value, max = (int) maxDelay.value;
        return (int) (min + (max - min) * Math.random());
    }
    
    public void onWorldChange() { lootedChests.clear(); }
}

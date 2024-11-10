package io.unlegit.modules.impl.combat;

import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.client.AccMinecraft;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.utils.ElapTime;
import net.minecraft.world.phys.HitResult;

import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

@IModule(name = "Auto Clicker", description = "Automatically left clicks for you.")
public class AutoClicker extends ModuleU
{
    public SliderSetting minCPS = new SliderSetting("Min CPS", "The minimum CPS in randomization.", 1, 8, 20),
                         maxCPS = new SliderSetting("Max CPS", "The maximum CPS in randomization.", 1, 12, 20);

    private final ElapTime elapTime = new ElapTime();
    public float CPS = 0;

    public void onUpdate()
    {
        if (glfwGetMouseButton(mc.getWindow().getWindow(), 0) == 1 &&
                (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK))
        {
            if (elapTime.passed((long) (1000 / CPS)))
            {
                ((AccMinecraft) mc).invokeStartAttack();
                CPS = updateCPS();
            }
        }
    }

    private float updateCPS()
    {
        float min = minCPS.value, max = maxCPS.value;
        return (float) (min + (max - min) * Math.random());
    }
}

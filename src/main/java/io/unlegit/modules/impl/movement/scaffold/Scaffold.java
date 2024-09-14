package io.unlegit.modules.impl.movement.scaffold;

import static io.unlegit.modules.impl.movement.scaffold.HelperBlock.*;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;

import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.entity.InvUtil;
import io.unlegit.utils.network.Packets;
import io.unlegit.utils.render.Animation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

// You thought this was going to be named Block Fly? lol
@IModule(name = "Scaffold", description = "Automatically bridges for you.")
public class Scaffold extends ModuleU implements IGui
{
    public ModeSetting sprint = new ModeSetting("Sprint", "Determines how you sprint.", new String[]
    {
        "None", "Vanilla", "Bypass"
    });
    
    public ToggleSetting autoJump = new ToggleSetting("Auto Jump", "Automatically jumps like Bedrock players.", false),
                         swingHand = new ToggleSetting("Swing Hand", "Swings the hand normally.", true),
                         showBlocks = new ToggleSetting("Show Blocks", "Shows the amount of blocks you have on the screen.", true);
    
    public ModeSetting rotations = new ModeSetting("Rotations", "The mode for rotations.", new String[]
    {
        "Vanilla", "Smooth", "Pitch Only"
    });
    
    public ModeSetting switchItem = new ModeSetting("Switch Item", "How to switch items.", new String[]
    {
        "Normal", "Spoof"
    });
    
    protected int prevSlot = 0, blockSlot = 0;
    private ResourceLocation background;
    protected ItemStack prevItem;
    private Animation animation;
    private MutableBlockPos pos;
    private float yaw, pitch;
    private double y = 0;
    
    public void onEnable()
    {
        super.onEnable();
        if (mc.player == null) { setEnabled(false); return; }
        
        y = mc.player.getY();
        yaw = mc.player.getYRot(); pitch = mc.player.getXRot();
        prevSlot = mc.player.getInventory().selected;
        if (showBlocks.enabled) animation = new Animation(96);
        
        if (sprint.equals("Bypass") && mc.player.isSprinting())
            Packets.sendNoEvent(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
    }
    
    public void onUpdate()
    {
        blockSlot = InvUtil.getSlot(mc.player.getInventory(), stack -> stack.getItem() instanceof BlockItem);
        if (blockSlot == -1) return;
        
        if (prevSlot != blockSlot)
        {
            if (switchItem.equals("Normal")) mc.player.getInventory().selected = blockSlot;
            else
            {
                Packets.sendNoEvent(new ServerboundSetCarriedItemPacket(blockSlot));
                prevSlot = blockSlot;
            }
        }
        
        if (sprint.equals("None"))
        {
            mc.options.keySprint.setDown(false);
            if (mc.player.isSprinting()) mc.player.setSprinting(false);
        }
        
        if (autoJump.enabled) mc.options.keyJump.setDown(true);
        
        if (jumpKeyDown() || (int) mc.player.getY() == mc.player.getY()) y = mc.player.getY();
        
        Vec3 block = new Vec3(getBlockX(), y, getBlockZ());
        pos = new BlockPos((int) Math.floor(mc.player.getX()), (int) block.y - 1, (int) Math.floor(mc.player.getZ())).relative(getDirection()).mutable();
        
        if (mc.level.isEmptyBlock(pos.relative(getDirection().getOpposite())))
        {
            preSwitchItem();
            ItemStack itemStack = mc.player.getMainHandItem();
            
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof BlockItem && getDirection() != Direction.UP)
            {
                int i = itemStack.getCount();
                BlockHitResult hitResult = new BlockHitResult(block, getDirection().getOpposite(), pos, false);
                InteractionResult actResult =  mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hitResult);
                
                if (actResult.consumesAction() && actResult.shouldSwing())
                {
                    if (swingHand.enabled) mc.player.swing(InteractionHand.MAIN_HAND);
                    else Packets.send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                }
                
                if ((itemStack.getCount() != i || mc.gameMode.hasInfiniteItems()) && swingHand.enabled)
                    mc.gameRenderer.itemInHandRenderer.itemUsed(InteractionHand.MAIN_HAND);
            }
            
            postSwitchItem();
        }
    }
    
    public void onMotion(MotionE e)
    {
        if (blockSlot == -1) return;
        preSwitchItem();
        float[] rotations = new float[] {getDirection().toYRot(), 80};
        ItemStack itemStack = mc.player.getMainHandItem();
        
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof BlockItem && this.rotations.equals("Vanilla"))
        {
            Direction direction = getDirection();
            if (direction != Direction.UP) yaw = rotations[0];
        }
        
        if (this.rotations.equals("Smooth"))
        {
            float yawDifference = Math.abs(rotations[0] - yaw), pitchDifference = Math.abs(rotations[1] - pitch);
            
            if (yaw < rotations[0])
            {
                if (yawDifference < 5) yaw = rotations[0];
                else yaw += (rotations[0] - yaw) / 2;
            }
            
            else if (yaw > rotations[0])
            {
                if (yawDifference < 5) yaw = rotations[0];
                else yaw -= (yaw - rotations[0]) / 2;
            }
            
            if (pitch < rotations[1])
            {
                if (pitchDifference < 5) pitch = rotations[1];
                else pitch += (rotations[1] - pitch) / 2;
            }
            
            else if (pitch > rotations[1])
            {
                if (pitchDifference < 5) pitch = rotations[1];
                else pitch -= (pitch - rotations[1]) / 2;
            }
        }
        
        else pitch = rotations[1];
        if (!this.rotations.equals("Pitch Only")) e.yaw = yaw;
        e.pitch = pitch;
        postSwitchItem();
    }
    
    public void onGuiRender(GuiRenderE e)
    {
        if (showBlocks.enabled)
        {
            if (background == null)
                background = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/scaffold/background.png"));
            
            int items = 0;
            
            for (int i = 0; i < 9; i++)
            {
                ItemStack stack = mc.player.getInventory().items.get(i);
                if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) items += stack.getCount();
            }
            
            GuiGraphics graphics = e.graphics;
            int width = mc.getWindow().getGuiScaledWidth(), height = mc.getWindow().getGuiScaledHeight();
            
            graphics.pose().translate(0, 10 - (animation.get() * 10), 0);
            
            GlStateManager._disableDepthTest();
            GlStateManager._enableBlend();
            graphics.setColor(1, 1, 1, animation.get());
            graphics.blit(background, (width / 2) - 34, height - 84, 68, 26, 68, 26, 68, 26);
            
            IFont.NORMAL.drawCenteredString(graphics, items + "", (width / 2) - 18, height - 77, new Color(255, 255, 255, animation.wrap(255)));
            IFont.NORMAL.drawCenteredString(graphics, "Blocks", (width / 2) + 1 + (IFont.NORMAL.getStringWidth(items + "") / 2), height - 77, new Color(220, 220, 220, animation.wrap(128)));
            GlStateManager._enableDepthTest();
            
            graphics.pose().translate(0, -(10 - animation.get() * 10), 0);
        }
    }
    
    public void onPacketSend(PacketSendE e)
    {
        if (blockSlot == -1) return;
        
        if (e.packet instanceof ServerboundPlayerCommandPacket packet)
        {
            if (!sprint.equals("Bypass")) return;
            
            if ((packet.getAction() == Action.START_SPRINTING || packet.getAction() == 
                    Action.STOP_SPRINTING) && packet.getId() == mc.player.getId())
                e.cancelled = true;
        }
        
        else if (e.packet instanceof ServerboundSetCarriedItemPacket &&
                !switchItem.equals("Normal"))
            e.cancelled = true;
    }
    
    public void onDisable()
    {
        super.onDisable();
        
        if (autoJump.enabled) mc.options.keyJump.setDown(jumpKeyDown());
        if (switchItem.equals("Normal")) mc.player.getInventory().selected = prevSlot;
        
        else if (blockSlot != mc.player.getInventory().selected && blockSlot != -1)
            Packets.sendNoEvent(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
        
        if (showBlocks.enabled)
        {
            animation = new Animation(96);
            animation.reverse = true;
        }
    }
    
    public boolean renderCondition()
    {
        return super.renderCondition() || (animation != null && animation.reverse && !animation.finished());
    }
}

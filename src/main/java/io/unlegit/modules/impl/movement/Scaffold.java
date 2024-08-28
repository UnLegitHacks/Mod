package io.unlegit.modules.impl.movement;

import com.mojang.blaze3d.platform.InputConstants;

import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.client.KeyMapAccessor;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.entity.InvUtil;
import io.unlegit.utils.entity.PlayerUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

// You thought this was going to be named Block Fly? lol
@IModule(name = "Scaffold", description = "Automatically bridges for you.")
public class Scaffold extends ModuleU
{
    public ModeSetting sprint = new ModeSetting("Sprint", "Determines how you sprint.", new String[]
    {
        "None", "Vanilla", "Bypass"
    });
    
    public ToggleSetting autoJump = new ToggleSetting("Auto Jump", "Automatically jumps like Bedrock players.", false),
                         swingHand = new ToggleSetting("Swing Hand", "Swings the hand normally.", true);
    
    public ModeSetting rotations = new ModeSetting("Rotations", "The mode for rotations.", new String[]
    {
        "Vanilla", "Smooth", "Pitch Only"
    });
    
    public ModeSetting switchItem = new ModeSetting("Switch Item", "How to switch items.", new String[]
    {
        "Normal", "Spoof"
    });
    
    private int prevSlot = 0, blockSlot = 0;
    private MutableBlockPos pos;
    private ItemStack prevItem;
    private float yaw, pitch;
    private double y = 0;
    
    public void onEnable()
    {
        super.onEnable();
        if (mc.player == null) { toggle(); return; }
        
        y = mc.player.getY();
        yaw = mc.player.getYRot(); pitch = mc.player.getXRot();
        prevSlot = mc.player.getInventory().selected;
        
        if (sprint.equals("Bypass") && mc.player.isSprinting())
            mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));;
    }
    
    public void onUpdate()
    {
        blockSlot = InvUtil.getSlot(mc.player.getInventory(), stack -> stack.getItem() instanceof BlockItem);
        
        if (blockSlot != -1 && prevSlot != blockSlot)
        {
            if (switchItem.equals("Normal")) mc.player.getInventory().selected = blockSlot;
            else
            {
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(blockSlot));
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
        pos = new BlockPos((int) mc.player.getX(), (int) block.y - 1, (int) mc.player.getZ()).relative(getDirection()).mutable();
        
        if (mc.level.isEmptyBlock(pos.relative(getDirection().getOpposite())))
        {
            preSwitchItem();
            ItemStack itemStack = mc.player.getMainHandItem();
            
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof BlockItem && getDirection() != Direction.UP)
            {
                int i = itemStack.getCount();
                BlockHitResult hitResult = new BlockHitResult(block, getDirection().getOpposite(), pos, false);
                InteractionResult actResult =  mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hitResult);
                if (actResult.consumesAction() && actResult.shouldSwing() && swingHand.enabled) mc.player.swing(InteractionHand.MAIN_HAND);
                
                if (itemStack.getCount() != i || mc.gameMode.hasInfiniteItems() && swingHand.enabled)
                    mc.gameRenderer.itemInHandRenderer.itemUsed(InteractionHand.MAIN_HAND);
            }
            
            postSwitchItem();
        }
    }
    
    public void onMotion(MotionE e)
    {
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
    
    public void onPacketSend(PacketSendE e)
    {
        if (e.packet instanceof ServerboundPlayerCommandPacket)
        {
            if (!sprint.equals("Bypass")) return;
            ServerboundPlayerCommandPacket packet = (ServerboundPlayerCommandPacket) e.packet;
            
            if ((packet.getAction() == Action.START_SPRINTING || packet.getAction() == 
                    Action.STOP_SPRINTING) && packet.getId() == mc.player.getId())
                e.cancelled = true;
        }
        
        else if (e.packet instanceof ServerboundSetCarriedItemPacket)
        {
            ServerboundSetCarriedItemPacket packet = (ServerboundSetCarriedItemPacket) e.packet;
            if (!switchItem.equals("Normal") && packet.getSlot() != blockSlot) e.cancelled = true;
        }
    }
    
    public void onDisable()
    {
        super.onDisable();
        if (autoJump.enabled) mc.options.keyJump.setDown(jumpKeyDown());
        
        if (switchItem.equals("Normal")) mc.player.getInventory().selected = prevSlot;
        
        else if (blockSlot != mc.player.getInventory().selected)
            mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
    }
    
    private Direction getDirection()
    {
        return PlayerUtil.isInMotion() ? Direction.fromYRot(PlayerUtil.getDirection()).getOpposite()
                : jumpKeyDown() ? Direction.UP : mc.player.getDirection().getOpposite();
    }
    
    public double getBlockX()
    {
        double playerX = mc.player.getX();
        
        switch (getDirection())
        {
            case WEST:
                return (int) playerX;
            case EAST:
                return (int) playerX + 1;
            default:
                return playerX;
        }
    }
    
    public double getBlockZ()
    {
        double playerZ = mc.player.getZ();
        
        switch (getDirection())
        {
            case NORTH:
                return (int) playerZ;
            case SOUTH:
                return (int) playerZ + 1;
            default:
                return playerZ;
        }
    }
    
    public void preSwitchItem()
    {
        if (switchItem.equals("Normal")) return;
        prevItem = mc.player.getMainHandItem();
        mc.player.setItemInHand(InteractionHand.MAIN_HAND, mc.player.getInventory().getItem(blockSlot));
    }
    
    public void postSwitchItem()
    {
        if (switchItem.equals("Normal")) return;
        mc.player.setItemInHand(InteractionHand.MAIN_HAND, prevItem);
    }
    
    public boolean jumpKeyDown()
    {
        KeyMapping jump = mc.options.keyJump;
        return InputConstants.isKeyDown(mc.getWindow().getWindow(), ((KeyMapAccessor) jump).getKey().getValue());
    }
}

package io.unlegit.utils.network;

import io.unlegit.interfaces.IMinecraft;
import io.unlegit.mixins.network.AccConnection;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.*;
import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
import net.minecraft.network.protocol.configuration.ServerboundSelectKnownPacks;
import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.network.protocol.login.ServerboundLoginAcknowledgedPacket;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
import net.minecraft.server.RunningOnDifferentThreadException;

import java.util.HashMap;

public class Packets implements IMinecraft
{
    public static HashMap<String, Class<? extends Packet<?>>> c2s = new HashMap<>();
    
    /** Sends a packet. */
    public static void send(Packet<?> packet)
    {
        connection().send(packet);
    }
    
    /** Sends a packet without the event triggering. */
    public static void sendNoEvent(Packet<?> packet)
    {
        connection().send(packet, null);
    }
    
    /** Simulates receiving a packet. */
    public static void receive(Packet<?> packet)
    {
        try { AccConnection.genericsFtw(packet, connection().getPacketListener()); }
        // Uncertain about the reason for this being thrown even if the code works
        catch (RunningOnDifferentThreadException e) {}
    }
    
    private static Connection connection() { return mc.getConnection().getConnection(); }
    
    private static void put(Class<? extends Packet<?>> packet, String string)
    {
        c2s.put(string, packet);
    }
    
    static
    {
        put(ServerboundAcceptTeleportationPacket.class, "Accept Teleportation");
        put(ServerboundBlockEntityTagQueryPacket.class, "Block Entity Tag Query");
        put(ServerboundChangeDifficultyPacket.class, "Change Difficulty");
        put(ServerboundChatAckPacket.class, "Chat Ack");
        put(ServerboundChatCommandPacket.class, "Chat Command");
        put(ServerboundChatCommandSignedPacket.class, "Chat Command Signed");
        put(ServerboundChatPacket.class, "Chat");
        put(ServerboundChatSessionUpdatePacket.class, "Chat Session Update");
        put(ServerboundChunkBatchReceivedPacket.class, "Chunk Batch Received");
        put(ServerboundClientCommandPacket.class, "Client Command");
        put(ServerboundClientInformationPacket.class, "Client Information");
        put(ServerboundCommandSuggestionPacket.class, "Command Suggestion");
        put(ServerboundConfigurationAcknowledgedPacket.class, "Configuration Ack");
        put(ServerboundContainerButtonClickPacket.class, "Container Button Click");
        put(ServerboundContainerClickPacket.class, "Container Click");
        put(ServerboundContainerClosePacket.class, "Container Close");
        put(ServerboundContainerSlotStateChangedPacket.class, "Container Slote State Change");
        put(ServerboundCookieResponsePacket.class, "Cookie Response");
        put(ServerboundCustomQueryAnswerPacket.class, "Custom Query");
        put(ServerboundCustomPayloadPacket.class, "Custom Payload");
        put(ServerboundDebugSampleSubscriptionPacket.class, "Debug Sample Subscription");
        put(ServerboundEditBookPacket.class, "Edit Book");
        put(ServerboundEntityTagQueryPacket.class, "Entity Tag Query");
        put(ServerboundFinishConfigurationPacket.class, "Finish Configuration");
        put(ServerboundHelloPacket.class, "Login Hello");
        put(ServerboundInteractPacket.class, "Interact");
        put(ServerboundJigsawGeneratePacket.class, "Jigsaw Generate");
        put(ServerboundKeepAlivePacket.class, "Keep Alive");
        put(ServerboundKeyPacket.class, "Login Key");
        put(ServerboundLockDifficultyPacket.class, "Lock Difficulty");
        put(ServerboundLoginAcknowledgedPacket.class, "Login Ack");
        put(ServerboundMovePlayerPacket.class, "Move Player");
        put(ServerboundMoveVehiclePacket.class, "Move Vehicle");
        put(ServerboundPaddleBoatPacket.class, "Paddle Boat");
        put(ServerboundPickItemPacket.class, "Pick Item");
        put(ServerboundPingRequestPacket.class, "Ping Request");
        put(ServerboundPlaceRecipePacket.class, "Place Recipe");
        put(ServerboundPlayerAbilitiesPacket.class, "Player Abilities");
        put(ServerboundPlayerActionPacket.class, "Player Action");
        put(ServerboundPlayerCommandPacket.class, "Player Command");
        put(ServerboundPlayerInputPacket.class, "Player Input");
        put(ServerboundPongPacket.class, "Pong");
        put(ServerboundRecipeBookChangeSettingsPacket.class, "Recipe Book Change Settings");
        put(ServerboundRecipeBookSeenRecipePacket.class, "Recipe Book Seen Recipe");
        put(ServerboundRenameItemPacket.class, "Rename Item");
        put(ServerboundResourcePackPacket.class, "Resource Pack");
        put(ServerboundSeenAdvancementsPacket.class, "Seen Advancements");
        put(ServerboundSelectKnownPacks.class, "Select Known Packs");
        put(ServerboundSelectTradePacket.class, "Select Trade");
        put(ServerboundSetBeaconPacket.class, "Set Beacon");
        put(ServerboundSetCarriedItemPacket.class, "Set Carried Item");
        put(ServerboundSetCommandBlockPacket.class, "Command Block");
        put(ServerboundSetCommandMinecartPacket.class, "Command Minecart");
        put(ServerboundSetCreativeModeSlotPacket.class, "Creative Mode Slot");
        put(ServerboundSetJigsawBlockPacket.class, "Jigsaw Block");
        put(ServerboundSetStructureBlockPacket.class, "Structure Block");
        put(ServerboundSignUpdatePacket.class, "Sign Update");
        put(ServerboundStatusRequestPacket.class, "Status Request");
        put(ServerboundSwingPacket.class, "Swing");
        put(ServerboundTeleportToEntityPacket.class, "Teleport To Entity");
        put(ServerboundUseItemOnPacket.class, "Use Item On Entity");
        put(ServerboundUseItemPacket.class, "Use Item");
    }
}

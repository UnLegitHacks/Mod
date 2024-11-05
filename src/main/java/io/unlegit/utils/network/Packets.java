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

import java.util.ArrayList;

public class Packets implements IMinecraft
{
    public static ArrayList<Class<? extends Packet<?>>> client = new ArrayList<>();
    
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

    static
    {
        client.add(ServerboundAcceptTeleportationPacket.class);
        client.add(ServerboundBlockEntityTagQueryPacket.class);
        client.add(ServerboundChangeDifficultyPacket.class);
        client.add(ServerboundChatAckPacket.class);
        client.add(ServerboundChatCommandPacket.class);
        client.add(ServerboundChatCommandSignedPacket.class);
        client.add(ServerboundChatPacket.class);
        client.add(ServerboundChatSessionUpdatePacket.class);
        client.add(ServerboundChunkBatchReceivedPacket.class);
        client.add(ServerboundClientCommandPacket.class);
        client.add(ServerboundClientInformationPacket.class);
        client.add(ServerboundCommandSuggestionPacket.class);
        client.add(ServerboundConfigurationAcknowledgedPacket.class);
        client.add(ServerboundContainerButtonClickPacket.class);
        client.add(ServerboundContainerClickPacket.class);
        client.add(ServerboundContainerClosePacket.class);
        client.add(ServerboundContainerSlotStateChangedPacket.class);
        client.add(ServerboundCookieResponsePacket.class);
        client.add(ServerboundCustomQueryAnswerPacket.class);
        client.add(ServerboundCustomPayloadPacket.class);
        client.add(ServerboundDebugSampleSubscriptionPacket.class);
        client.add(ServerboundEditBookPacket.class);
        client.add(ServerboundEntityTagQueryPacket.class);
        client.add(ServerboundFinishConfigurationPacket.class);
        client.add(ServerboundHelloPacket.class);
        client.add(ServerboundInteractPacket.class);
        client.add(ServerboundJigsawGeneratePacket.class);
        client.add(ServerboundKeepAlivePacket.class);
        client.add(ServerboundKeyPacket.class);
        client.add(ServerboundLockDifficultyPacket.class);
        client.add(ServerboundLoginAcknowledgedPacket.class);
        client.add(ServerboundMovePlayerPacket.class);
        client.add(ServerboundMoveVehiclePacket.class);
        client.add(ServerboundPaddleBoatPacket.class);
        client.add(ServerboundPickItemPacket.class);
        client.add(ServerboundPingRequestPacket.class);
        client.add(ServerboundPlaceRecipePacket.class);
        client.add(ServerboundPlayerAbilitiesPacket.class);
        client.add(ServerboundPlayerActionPacket.class);
        client.add(ServerboundPlayerCommandPacket.class);
        client.add(ServerboundPlayerInputPacket.class);
        client.add(ServerboundPongPacket.class);
        client.add(ServerboundRecipeBookChangeSettingsPacket.class);
        client.add(ServerboundRecipeBookSeenRecipePacket.class);
        client.add(ServerboundRenameItemPacket.class);
        client.add(ServerboundResourcePackPacket.class);
        client.add(ServerboundSeenAdvancementsPacket.class);
        client.add(ServerboundSelectKnownPacks.class);
        client.add(ServerboundSelectTradePacket.class);
        client.add(ServerboundSetBeaconPacket.class);
        client.add(ServerboundSetCarriedItemPacket.class);
        client.add(ServerboundSetCommandBlockPacket.class);
        client.add(ServerboundSetCommandMinecartPacket.class);
        client.add(ServerboundSetCreativeModeSlotPacket.class);
        client.add(ServerboundSetJigsawBlockPacket.class);
        client.add(ServerboundSetStructureBlockPacket.class);
        client.add(ServerboundSignUpdatePacket.class);
        client.add(ServerboundStatusRequestPacket.class);
        client.add(ServerboundSwingPacket.class);
        client.add(ServerboundTeleportToEntityPacket.class);
        client.add(ServerboundUseItemOnPacket.class);
        client.add(ServerboundUseItemPacket.class);
    }
}

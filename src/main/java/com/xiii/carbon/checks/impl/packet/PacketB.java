package com.xiii.carbon.checks.impl.packet;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import com.xiii.carbon.Carbon;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.PacketUtils;
import org.bukkit.GameMode;

public class PacketB extends Check {
    public PacketB(final Profile profile) {
        super(profile, CheckType.PACKET, "B", "Post packet");
    }

    private int lastSlot = -1, actionBuffer, playerDiggingBuffer, flyingStreak;
    private long lastKeepAliveID = -1, serverKeepAlive = -1;
    private boolean swing;
    private float lastYaw, lastPitch;
    private WrapperPlayClientEntityAction.Action lastEntityAction;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        switch (clientPlayPacket.getType()) {

            case ANIMATION:

                if (PacketUtils.isPost(clientPlayPacket.getType(), PacketType.Play.Client.ANIMATION)) fail("Animation");

                this.swing = true;

                break;

            case INTERACT_ENTITY:

                if (PacketUtils.isPost(clientPlayPacket.getType(), PacketType.Play.Client.INTERACT_ENTITY)) fail("Interact Entity");

                if (clientPlayPacket.getInteractEntityWrapper().getEntityId() == profile.getPlayer().getEntityId()) fail("Interact Entity: Self");

                if (clientPlayPacket.getInteractEntityWrapper().getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {

                    if (profile.getActionData().getLastPlace() <= 50) fail("Interact Entity: Invalid action");

                    if (!this.swing) fail("Interact Entity: Swing");
                    else this.swing = false;
                }

                break;

            case STEER_VEHICLE:

                if (!profile.isExempt().isVehicle()) fail("Steer Vehicle");

                final WrapperPlayClientSteerVehicle steerVehicleWrapper = clientPlayPacket.getSteerVehicleWrapper();

                if ((Math.abs(steerVehicleWrapper.getSideways()) > 0.98F) || Math.abs(steerVehicleWrapper.getForward()) > 0.98F) fail("Steer Vehicle: Maximum");

                this.flyingStreak = 0;

                break;

            case PLAYER_DIGGING:

                if (PacketUtils.isPost(clientPlayPacket.getType(), PacketType.Play.Client.PLAYER_DIGGING)) fail("Player Digging");

                //if (clientPlayPacket.getPlayerDiggingWrapper().getAction() == DiggingAction.RELEASE_USE_ITEM && playerDiggingBuffer++ > 1) fail("Player Digging: Invalid action"); //TODO: Fix

                break;

            case ENTITY_ACTION:

                if (PacketUtils.isPost(clientPlayPacket.getType(), PacketType.Play.Client.ENTITY_ACTION)) fail("Entity Action");

                if (actionBuffer++ > 1 && this.lastEntityAction == clientPlayPacket.getEntityActionWrapper().getAction()) fail("Entity Action: Invalid action");

                this.lastEntityAction = clientPlayPacket.getEntityActionWrapper().getAction();

                break;

            case PLUGIN_MESSAGE:

                if (PacketUtils.isPost(clientPlayPacket.getType(), PacketType.Play.Client.PLUGIN_MESSAGE)) fail("Plugin Message");

                break;

            case HELD_ITEM_CHANGE:

                if (PacketUtils.isPost(clientPlayPacket.getType(), PacketType.Play.Client.HELD_ITEM_CHANGE)) fail("Held Item Change");

                if (clientPlayPacket.getHeldItemChangeWrapper().getSlot() == lastSlot) fail("Held Item Change: Invalid slot");

                this.lastSlot = clientPlayPacket.getHeldItemChangeWrapper().getSlot();

                break;

            case PLAYER_ROTATION:

                if (Math.abs(profile.getMovementData().getLocation().getPitch()) > 90) fail("Player Rotation");

                break;

            case PLAYER_POSITION_AND_ROTATION:

                if (Math.abs(profile.getMovementData().getLocation().getPitch()) > 90) fail("Player Position And Rotation");

                break;

            case PLAYER_FLYING:

                this.actionBuffer = 0;
                this.playerDiggingBuffer = 0;

                final WrapperPlayClientPlayerFlying flyingWrapper = clientPlayPacket.getFlyingWrapper();

                //Invalid flying
                if (flyingWrapper.hasRotationChanged() || Carbon.getInstance().getNmsManager().getNmsInstance().isInsideVehicle(profile.getPlayer())) {

                    final float yaw = flyingWrapper.getLocation().getYaw();
                    final float pitch = flyingWrapper.getLocation().getPitch();

                    final boolean exempt = profile.isExempt().getTeleportTicks() <= 2; //TODO: Exempt near vehicle

                    if (!exempt && yaw == this.lastYaw && pitch == this.lastPitch) fail("Flying");

                    this.lastYaw = yaw;
                    this.lastPitch = pitch;
                }

                //Suspending update packets
                if (!flyingWrapper.hasPositionChanged() && !profile.isExempt().isVehicle()) {

                    //if (this.flyingStreak++ > 20) fail("Flying: Streak"); //TODO: Doesn't work or not compatible with 1.8+ ?
                } else this.flyingStreak = 0;

            case CLICK_WINDOW:

                if (PacketUtils.isPost(clientPlayPacket.getType(), PacketType.Play.Client.CLICK_WINDOW)) fail("Click Window");

                break;

            case KEEP_ALIVE:

                if (clientPlayPacket.getKeepAliveWrapper().getId() == this.lastKeepAliveID) fail("Keep Alive");

                if (clientPlayPacket.getKeepAliveWrapper().getId() != this.serverKeepAlive) fail("Keep Alive: Confirmation");

                this.lastKeepAliveID = clientPlayPacket.getKeepAliveWrapper().getId();

                break;

            case SPECTATE:

                if (profile.getPlayer().getGameMode() != GameMode.SPECTATOR) fail("Spectate");

                break;
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {

        if (serverPlayPacket.is(PacketType.Play.Server.KEEP_ALIVE)) this.serverKeepAlive = serverPlayPacket.getKeepAliveWrapper().getId();
    }
}

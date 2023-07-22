package com.xiii.carbon.playerdata.data.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockAction;
import com.xiii.carbon.Carbon;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.Data;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;
import com.xiii.carbon.utils.MiscUtils;
import com.xiii.carbon.utils.TaskUtils;
import com.xiii.carbon.utils.custom.PlacedBlock;
import com.xiii.carbon.utils.custom.desync.Desync;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionData implements Data {

    private GameMode gameMode;

    private boolean allowFlight, sneaking, sprinting;

    private final Desync desync;

    private PlacedBlock placedBlock;

    private ItemStack itemInMainHand = MiscUtils.EMPTY_ITEM, itemInOffHand = MiscUtils.EMPTY_ITEM;

    private int lastAllowFlightTicks, lastSleepingTicks, lastRidingTicks;

    private long lastPlace = 10000L;

    /*
     * 1.9+
     */
    private int lastDuplicateOnePointSeventeenPacketTicks = 100;

    public ActionData(final Profile profile) {

        this.desync = new Desync(profile);

        //Initialize

        Player player = profile.getPlayer();

        this.gameMode = player.getGameMode();

        this.allowFlight = Carbon.getInstance().getNmsManager().getNmsInstance().getAllowFlight(player);

        this.lastAllowFlightTicks = this.allowFlight ? 0 : 100;
    }

    @Override
    public void process(final ClientPlayPacket clientPlayPacket) {

        switch (clientPlayPacket.getType()) {

            case ENTITY_ACTION:

                final WrapperPlayClientEntityAction action = clientPlayPacket.getEntityActionWrapper();

                if (action.getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) this.sprinting = true;

                if (action.getAction() == WrapperPlayClientEntityAction.Action.STOP_SPRINTING) this.sprinting = false;

                if (action.getAction() == WrapperPlayClientEntityAction.Action.START_SNEAKING) this.sneaking = true;

                if (action.getAction() == WrapperPlayClientEntityAction.Action.STOP_SNEAKING) this.sneaking = false;

                break;

            case PLAYER_BLOCK_PLACEMENT:

                final WrapperPlayClientPlayerBlockPlacement place = clientPlayPacket.getPlayerBlockPlacementWrapper();

                this.lastPlace = clientPlayPacket.getTimeStamp();
        }
    }

    @Override
    public void process(final ServerPlayPacket serverPlayPacket) {}

    public int getLastRidingTicks() {
        return lastRidingTicks;
    }

    public PlacedBlock getPlacedBlock() {
        return placedBlock;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public ItemStack getItemInMainHand() {
        return itemInMainHand;
    }

    public ItemStack getItemInOffHand() {
        return itemInOffHand;
    }

    public Desync getDesync() {
        return desync;
    }

    public int getLastSleepingTicks() {
        return lastSleepingTicks;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public long getLastPlace() {
        return MathUtils.elapsed(lastPlace);
    }

    public int getLastDuplicateOnePointSeventeenPacketTicks() {
        return lastDuplicateOnePointSeventeenPacketTicks;
    }
}

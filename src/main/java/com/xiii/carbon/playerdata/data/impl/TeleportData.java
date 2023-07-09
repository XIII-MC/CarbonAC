package com.xiii.carbon.playerdata.data.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.Data;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;

public class TeleportData implements Data {

    private final Profile profile;

    public TeleportData(final Profile profile) {
        this.profile = profile;
    }

    private int teleportTicks;
    private long lastTeleport;
    private double tpX, tpY, tpZ, tpPitch, tpYaw;

    @Override
    public void process(final ClientPlayPacket clientPlayPacket) {}

    @Override
    public void process(final ServerPlayPacket serverPlayPacket) {

        if (serverPlayPacket.is(PacketType.Play.Server.ENTITY_TELEPORT) || serverPlayPacket.is(PacketType.Play.Server.PLAYER_POSITION_AND_LOOK)) {

            tpX = serverPlayPacket.getPlayerPositionAndLookWrapper().getX();
            tpY = serverPlayPacket.getPlayerPositionAndLookWrapper().getY();
            tpZ = serverPlayPacket.getPlayerPositionAndLookWrapper().getZ();
            tpPitch = serverPlayPacket.getPlayerPositionAndLookWrapper().getPitch();
            tpYaw = serverPlayPacket.getPlayerPositionAndLookWrapper().getYaw();

            this.teleportTicks = 0;
            this.lastTeleport = System.currentTimeMillis();
        }
        if (MathUtils.elapsed(this.lastTeleport) >= 50) {
            if (tpX != profile.getMovementData().getLocation().getX() || tpY != profile.getMovementData().getLocation().getY() || tpZ != profile.getMovementData().getLocation().getZ() || tpPitch != profile.getMovementData().getLocation().getPitch() || tpYaw != profile.getMovementData().getLocation().getYaw()) {

                tpX = Double.NaN;
                tpY = Double.NaN;
                tpZ = Double.NaN;
                tpPitch = Double.NaN;
                tpYaw = Double.NaN;

                this.teleportTicks++;
            }
        }
    }

    public int getTeleportTicks() {
        return teleportTicks;
    }
}

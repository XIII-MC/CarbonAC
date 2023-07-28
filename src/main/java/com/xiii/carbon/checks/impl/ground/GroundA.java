package com.xiii.carbon.checks.impl.ground;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.BetterStream;
import org.bukkit.Material;

import java.util.List;

public class GroundA extends Check {

    public GroundA(final Profile profile) {
        super(profile, CheckType.GROUND, "A", "Player is spoofing ground state.");
    }

    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isMovement()) return;

        final MovementData movementData = profile.getMovementData();
        final List<Material> footBlocks = movementData.getFootBlocks();
        final boolean testBoolean = !BetterStream.allMatch(footBlocks, material -> material.toString().equalsIgnoreCase("AIR"));
        final boolean serverGroundFix = !BetterStream.anyMatch(footBlocks, material -> material.toString().equalsIgnoreCase("AIR"));
        final boolean serverGround = movementData.isServerGround() && testBoolean;
        final boolean clientGround = movementData.isOnGround();

        if (serverGround != clientGround && !serverGroundFix && BetterStream.allMatch(footBlocks, material -> material.toString().equalsIgnoreCase("AIR"))) {

            if (movementData.getServerAirTicks() > 1 || increaseBufferBy(1) > 1) fail("c=" + clientGround + " s=" + serverGround + " b=" + getBuffer());

        } else resetBuffer();

    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}

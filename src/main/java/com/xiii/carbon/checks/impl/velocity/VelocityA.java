package com.xiii.carbon.checks.impl.velocity;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.CombatData;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;

@Experimental
public class VelocityA extends Check {
    public VelocityA(final Profile profile) {
        super(profile, CheckType.VELOCITY, "A", "Check's difference between player's and server's velocity.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {}

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {
        if (serverPlayPacket.is(PacketType.Play.Server.ENTITY_VELOCITY)) {

            final MovementData movementData = profile.getMovementData();

            final double velocityDifference = movementData.getDeltaY() - serverPlayPacket.getEntityVelocityWrapper().getVelocity().getY();

            debug(velocityDifference);
        }
    }
}

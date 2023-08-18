package com.xiii.carbon.checks.impl.speed;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

public class SpeedA extends Check {
    public SpeedA(final Profile profile) {
        super(profile, CheckType.SPEED, "A", "Checks the player's acceleration.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isMovement()) return;

        final MovementData movementData = profile.getMovementData();

        if (movementData.getAccelXZ() != 0 && !movementData.isOnGround()) {

            final boolean exempt = (profile.isExempt().isJoined(2000L) && movementData.isServerGround()) || profile.isExempt().isClimable(50L);

            final double scaledAccel = movementData.getAccelXZ() * 100;

            if (scaledAccel < 1.2E-12 && !exempt) fail("accel: §c" + scaledAccel);
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

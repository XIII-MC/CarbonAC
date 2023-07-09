package com.xiii.carbon.checks.impl.speed;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;
import com.xiii.carbon.utils.MoveUtils;

@Experimental
public class SpeedA extends Check {

    public SpeedA(Profile profile) {
        super(profile, CheckType.SPEED, "A", "Checks the player's acceleration.");
    }

    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {
        if (clientPlayPacket.isMovement()) {
            MovementData movementData = profile.getMovementData();

            if (movementData.getAccelXZ() != 0) {
                if (!movementData.isOnGround()) {
                    final boolean exempt = profile.isExempt().isJoined(2000L);
                    final double scaledAccel = movementData.getAccelXZ() * 100;
                    if (scaledAccel < 1.2E-12 && !exempt) {
                        fail("accel=" + scaledAccel);
                    }
                }
            }

        }
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}

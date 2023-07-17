package com.xiii.carbon.checks.impl.speed;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.ActionData;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MoveUtils;

@Experimental
public class SpeedB extends Check {
    public SpeedB(final Profile profile) {
        super(profile, CheckType.SPEED, "B", "Player is not following Minecraft's vanilla friction");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isMovement()) {

            final MovementData movementData = profile.getMovementData();
            final ActionData actionData = profile.getActionData();

            final boolean exempt = profile.isExempt().isFly() || (profile.isExempt().isJoined(5000L) && movementData.isServerGround()) || profile.isExempt().getTeleportTicks() <= 2;

            if (movementData.getAirTicks() > 2) {

                final double friction = movementData.getDeltaXZ() - (movementData.getLastDeltaXZ() * MoveUtils.FRICTION + (actionData.isSprinting() ? 0.026 : 0.02));

                if (!exempt && friction > 0) fail("friction=" + friction);
            }
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

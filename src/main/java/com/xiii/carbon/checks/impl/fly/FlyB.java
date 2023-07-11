package com.xiii.carbon.checks.impl.fly;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

@Experimental
public class FlyB extends Check {

    public FlyB(final Profile profile) {
        super(profile, CheckType.FLY, "B", "Checks the player's y acceleration.");
    }

    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {
        if (clientPlayPacket.isMovement()) {
            MovementData movementData = profile.getMovementData();
            final double getAccelY = movementData.getDeltaY() - movementData.getLastDeltaY();
            final boolean exempt = profile.isExempt().isFly() || profile.isExempt().isClimable(50L);
            if (movementData.getAirTicks() > 2 && getAccelY >= 0 && !movementData.isOnGround() && !exempt) {
               fail("accel=" + getAccelY);
            }
        }
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}

package com.xiii.carbon.checks.impl.fly;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

@Experimental
public class FlyC extends Check {
    public FlyC(final Profile profile) {
        super(profile, CheckType.FLY, "C", "Checks the players motion.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isMovement()) return;

        final MovementData movementData = profile.getMovementData();

        final double accel = movementData.getAccelY();

        final double math = movementData.getLastDeltaY() + movementData.getDeltaY();

        final boolean exempt = profile.isExempt().isLava(50L) || profile.isExempt().isWater(50L) || profile.isExempt().isClimable(50L) || profile.isExempt().getTeleportTicks() <= 2;

        if (accel > 0 && Math.abs(math) <= 0.02 && !exempt) {

            if (getBuffer() > 2 || increaseBufferBy(1) > 1) fail(math+ " " + accel);

        } else decreaseBufferBy(0.25);
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

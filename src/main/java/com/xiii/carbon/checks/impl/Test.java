package com.xiii.carbon.checks.impl;

import com.xiii.carbon.checks.annotation.Disabled;
import com.xiii.carbon.checks.annotation.Testing;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

@Disabled
public class Test extends Check {
    public Test(final Profile profile) {
        super(profile, CheckType.TEST, "A", "Test Check for the Developers.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {
        if (!clientPlayPacket.isMovement()) return;

        final MovementData data = profile.getMovementData();
        final double accel = data.getAccelY();
        final double math = data.getLastDeltaY() + data.getDeltaY();
        final boolean exempt = profile.isExempt().isLava(50L) || profile.isExempt().isWater(50L) || profile.isExempt().isClimable(50L);
        if (accel > 0 && Math.abs(math) <= 0.02 && !exempt) {
            if (getBuffer() > 2 || increaseBufferBy(1) > 1) fail(math+ " " + accel);
        } else decreaseBufferBy(0.25);

    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}

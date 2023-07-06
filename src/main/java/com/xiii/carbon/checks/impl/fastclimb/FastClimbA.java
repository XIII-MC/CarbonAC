package com.xiii.carbon.checks.impl.fastclimb;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.annotation.Testing;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;

@Experimental
public class FastClimbA extends Check {
    public FastClimbA(final Profile profile) {
        super(profile, CheckType.FASTCLIMB, "A", "Checks if the player is moving too fast/slow on a climable.");
    }

    private int skipTicks = 0;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isMovement()) {

            final MovementData movementData = profile.getMovementData();

            if (MathUtils.decimalRound(movementData.getDeltaY(), 2) == 0.42) skipTicks = 6;

            if (profile.isExempt().isClimable(200L)) {

                if (movementData.getDeltaY() > 0.1177 && skipTicks <= 0) fail("deltaY: " + movementData.getDeltaY());

                if (skipTicks >= 1) skipTicks--;

                //debug("dy=" + movementData.getDeltaY() + " sT=" + skipTicks);
            }
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

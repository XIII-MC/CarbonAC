package com.xiii.carbon.checks.impl.fastclimb;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MoveUtils;

@Experimental
public class FastClimbA extends Check {
    public FastClimbA(final Profile profile) {
        super(profile, CheckType.FASTCLIMB, "A", "Checks if the player is moving too fast/slow on a climable.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isMovement()) {

            final MovementData movementData = profile.getMovementData();

            final double deltaY = movementData.getDeltaY();

            final boolean lowBlock = movementData.getClientGroundTicks() <= 1 && deltaY == 0.5 && movementData.isBlockBelow(1);

            final boolean exempt = profile.isExempt().isFly();

            if (!exempt && profile.isExempt().isClimable(50L)) {

                if (movementData.getAirTicks() > 4) {
                    if (deltaY > 0.1177) {
                        fail("deltaY: " + deltaY);
                        debug("FastClimb A - dY=" + deltaY + " aT=" + movementData.getAirTicks() + " lB=" + lowBlock);
                    }
                } else {
                    if (deltaY > (MoveUtils.JUMP_MOTION + (lowBlock ? 0.5 : 0))) {
                        fail("Exceeded maximum motion: " + deltaY);
                        debug("FastClimb A MAX - dY=" + deltaY + " aT=" + movementData.getAirTicks() + " lB=" + lowBlock + " bbT=" + movementData.getBlockBelowTicks());
                    }
                }
            }
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

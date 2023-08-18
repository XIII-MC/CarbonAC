package com.xiii.carbon.checks.impl.fastclimb;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MoveUtils;

public class FastClimbA extends Check {
    public FastClimbA(final Profile profile) {
        super(profile, CheckType.FASTCLIMB, "A", "Checks if the player is moving too fast/slow on a climable.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isMovement()) return;

        final MovementData movementData = profile.getMovementData();

        final double deltaY = movementData.getDeltaY();

        final boolean lowBlock = movementData.getClientGroundTicks() <= 1 && deltaY == 0.5 && movementData.isBlockBelow(1);

        final boolean exempt = profile.isExempt().isFly();

        if (!exempt && profile.isExempt().isClimable(50L)) {

            if (movementData.getAirTicks() > 4) {

                if (deltaY > 0.1177) fail("deltaY: §c" + deltaY + System.lineSeparator() + "§rdY: §c" + deltaY + System.lineSeparator() + "§raT: §c" + movementData.getAirTicks() + System.lineSeparator() + "§rlB: §c" + lowBlock + " §rcGT: §c" + movementData.getClientGroundTicks() + " §rbBT: §c" + movementData.getBlockBelowTicks());
            } else {

                if (deltaY > (MoveUtils.JUMP_MOTION + (lowBlock ? 0.5 : 0))) fail("Exceeded maximum ladder motion" + System.lineSeparator() + deltaY + System.lineSeparator() + "§rdY: §c" + deltaY + System.lineSeparator() + "§raT: §c" + movementData.getAirTicks() + System.lineSeparator() + "§rlB: §c" + lowBlock + " §rcGT: §c" + movementData.getClientGroundTicks() + " §rbBT: §c" + movementData.getBlockBelowTicks());
            }
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

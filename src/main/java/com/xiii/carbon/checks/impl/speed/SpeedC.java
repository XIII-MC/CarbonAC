package com.xiii.carbon.checks.impl.speed;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MoveUtils;

@Experimental
public class SpeedC extends Check {
    public SpeedC(final Profile profile) {
        super(profile, CheckType.SPEED, "C", "Impossible XZ movement");
    }

    /**
     *
     * ABS Stands for Absolute, aka it's an absolute limit meaning it's bufferless
     */

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        final boolean exempt = profile.isExempt().isFly() || profile.isExempt().getTeleportTicks() <= 2;

        if (!clientPlayPacket.isMovement() || exempt) return;

        final MovementData movementData = profile.getMovementData();

        final double speed = movementData.getDeltaXZ();

        //Ground
        if (movementData.getClientGroundTicks() > 12) {

            //ABS
            if (speed > MoveUtils.BASE_GROUND_SPEED) fail("ABS-GS: §c" + speed);

            //Non ABS
            if (speed > MoveUtils.NON_ABS_GROUND_SPEED) {
                if (increaseBufferBy(1) > 6) fail("GS: §c" + speed + "§r" + System.lineSeparator() + "buffer: §c" + getBuffer());
            } else decreaseBufferBy(1);
        } else {

            //ABS
            if (movementData.getAirTicks() > 2 && speed > (MoveUtils.BASE_AIR_SPEED + (movementData.getBlockAboveTicks() <= 7 ? 0.03 : 0))) fail("ABS-AS: §c" + speed + System.lineSeparator() + "§rbat: §c" + movementData.getBlockAboveTicks());

            //Non ABS
            /**
             * isStep: Handles stairs/slabs jumps that make you go faster
             * aboveTicks: Handles hit heads
             */
            if (speed > (MoveUtils.NON_ABS_AIR_SPEED + (profile.isExempt().isStep() ? 0.1 : 0) + (movementData.getBlockAboveTicks() <= 3 ? 0.15 : 0))) {
                if (increaseBufferBy(1) > 6) fail("AS: §c" + speed + "§r" + System.lineSeparator() + "buffer: §c" + getBuffer() + System.lineSeparator() + "§rbat: §c" + movementData.getBlockAboveTicks());
            } else decreaseBufferBy(1);
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

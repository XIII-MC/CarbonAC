package com.xiii.carbon.checks.impl.velocity;

import com.github.retrooper.packetevents.util.Vector3d;
import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.CombatData;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MoveUtils;

@Experimental
public class VelocityA extends Check {
    public VelocityA(final Profile profile) {
        super(profile, CheckType.VELOCITY, "A", "Check's difference between player's and server's velocity.");
    }

    private int ticks = 0;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isMovement()) {

            final MovementData movementData = profile.getMovementData();
            final CombatData combatData = profile.getCombatData();

            final boolean jumped = !movementData.isOnGround() && movementData.isLastOnGround() && movementData.getDeltaY() == MoveUtils.JUMP_MOTION;

            final Vector3d entityVelocity = combatData.getEntityVelocity();

            if (entityVelocity != null && entityVelocity.getY() != 0) {

                if(jumped || (ticks++ > (MoveUtils.getMaxVelocityTicks(0, entityVelocity.getY()))) ) {

                    final double percentage = movementData.getDeltaY() / entityVelocity.getY() * 100;

                    if ((percentage >= 0 && percentage < 100) && increaseBuffer() > 2) {
                        fail("percentage=" + percentage);
                        debug("%: " + percentage);
                    } else decreaseBufferBy(1);

                    ticks = 0;
                }
            }
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

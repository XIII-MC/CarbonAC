package com.xiii.carbon.checks.impl.test;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import com.xiii.carbon.checks.annotation.Testing;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.PredictionEngine;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;
import com.xiii.carbon.utils.MoveUtils;
import com.xiii.carbon.utils.fastmath.FastMath;

@Testing
public class Test extends Check {

    public Test(Profile profile) {
        super(profile, CheckType.TEST, "A", "Test Check for the Developers.");
    }
    public double predictionLimit = 1.9262653090336062E-14;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isMovement()) return;

        final MovementData movementData = profile.getMovementData();

        final boolean exempt = profile.isExempt().isFly() || profile.isExempt().isWater(150L) || profile.isExempt().isLava(150L) || profile.isExempt().isTrapdoor_door() || profile.isExempt().isCobweb(50L) || profile.isExempt().isCake() || profile.getVehicleData().isRiding(150L) || (profile.isExempt().isJoined(5000L) && movementData.isServerGround()) || profile.isExempt().isClimable(50L) || profile.isExempt().tookDamage(50L);

        final double deltaY = movementData.getDeltaY();

        final boolean nearGroundExempt = movementData.getClientGroundTicks() <= 4 && MathUtils.decimalRound(deltaY, 8) == -0.07840000;

        if (!movementData.isOnGround()) {

            final boolean isBlockAbove = movementData.isBlockAbove() && Math.abs(0.20000004768372381 - deltaY) < predictionLimit;

            final double predictionOutput = isBlockAbove ? deltaY : PredictionEngine.getVerticalPrediction(movementData.getLastDeltaY());

            final double prediction = deltaY - predictionOutput;

            final boolean jumped = (!movementData.isOnGround() && movementData.isLastOnGround() && deltaY == MoveUtils.JUMP_MOTION) || (!movementData.isOnGround() && movementData.getLastNearWallTicks() > 0 && (deltaY == 0.40444491418477835 || deltaY == 0.33319999363422337));
            if (profile.getPlayer().getName().equalsIgnoreCase("Vagdedes2")) {
               // debug(prediction + " " + deltaY);
            }
            if (!nearGroundExempt && !exempt && prediction > predictionLimit && !jumped) {

                fail("pred=" + predictionOutput + " my=" + deltaY);
                // debug("pred=" + predictionOutput + " my=" + deltaY + " ngt=" + movementData.getClientGroundTicks() + " offset=" + (prediction - 1.9262653090336062E-14));
            }
        } else decreaseBufferBy(1);
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {

    }
}

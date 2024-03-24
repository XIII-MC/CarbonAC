package com.xiii.carbon.checks.impl.fly;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.PredictionEngine;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
public class FlyD extends Check {
    public FlyD(final Profile profile) {
        super(profile, CheckType.FLY, "D", "Checks the players motion.");
    }


    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {
        if (!clientPlayPacket.isMovement()) return;
        final MovementData movementData = profile.getMovementData();
        final double testMotion = movementData.getLastDeltaY() - movementData.getAccelY();
        final double math = testMotion / PredictionEngine.getVerticalPrediction(movementData.getLastDeltaY());
        if (movementData.getDeltaY() < 0 && math < -1) {
            fail("t: §c" + math);
        }
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}

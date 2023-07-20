package com.xiii.carbon.checks.impl.fly;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.BetterStream;

public class FlyB extends Check {
    public FlyB(final Profile profile) {
        super(profile, CheckType.FLY, "B", "Checks the player's y acceleration.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isMovement()) return;

        final MovementData movementData = profile.getMovementData();

        final double getAccelY = movementData.getDeltaY() - movementData.getLastDeltaY();

        final boolean exempt = profile.isExempt().isFly() || profile.isExempt().isClimable(50L) || (profile.isExempt().isJoined(5000L) && movementData.isServerGround()) || profile.isExempt().getTeleportTicks() <= 2 || profile.isExempt().isWater(50L) || profile.isExempt().isLava(50L);

        final boolean fix = !(((movementData.getBelowBlocks().size() > 1 || !BetterStream.anyMatch(movementData.getBelowBlocks(), material -> material.toString().equalsIgnoreCase("AIR"))) && !movementData.getBelowBlocks().isEmpty()));

        if (movementData.getAirTicks() > 2 && getAccelY >= 0 && !movementData.isOnGround() && !exempt && fix) fail("accel: §c" + getAccelY + System.lineSeparator() + "§rfix: §c" + fix + "§r bBS: §c" + movementData.getBelowBlocks().size());
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

package com.xiii.carbon.checks.impl.aim;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;

public class AimAssistA extends Check {
    public AimAssistA(final Profile profile) {
        super(profile, CheckType.AIMASSIST, "A", "Basic Math.");
    }

    int exemptFixTicks = 0;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isRotation()) return;

        if (profile.isExempt().isJoined(5000L) || profile.isExempt().isVehicle()) return;

        final RotationData data = profile.getRotationData();

        final double divDeltaYaw = data.getDeltaYaw() / data.getLastDeltaYaw();
        final double divAccelYaw = data.getYawAccel() / data.getLastYawAccel();

        final double absGCD = MathUtils.getAbsoluteGcd((float) divDeltaYaw, (float) divAccelYaw);

        //Fixes a false that occurred when moving your pitch without changing your yaw (or very little change)
        final double fix = (data.getLastDeltaYaw() + data.getDeltaYaw()) % (data.getDeltaYaw() + data.getLastDeltaYaw() + 1000);
        if (fix < 0.31) exemptFixTicks++;
        else exemptFixTicks = 0;

        if ((absGCD < 1E-17 || (Double.isInfinite(divDeltaYaw) && Double.isInfinite(divAccelYaw))) && exemptFixTicks < 2) {
            if (increaseBufferBy(1) > 2) fail("ad: §c" + absGCD + "§r" + System.lineSeparator() + "ddy: §c" + divDeltaYaw + "§r" + System.lineSeparator() + "day: §c" + divAccelYaw + "§r" + System.lineSeparator() + "b: §c" + getBuffer() + "§r" + System.lineSeparator() + "f: §c" + fix);
        } else decreaseBufferBy(0.05);
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

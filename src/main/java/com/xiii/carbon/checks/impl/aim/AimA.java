package com.xiii.carbon.checks.impl.aim;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;

public class AimA extends Check {
    public AimA(Profile profile) {
        super(profile, CheckType.AIM, "A", "Basic Math.");
    }

    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {
        if (!clientPlayPacket.isRotation()) return;
        if (profile.isExempt().isJoined(5000L) || profile.isExempt().isVehicle()) return;
        final RotationData data = profile.getRotationData();
        double test = data.getDeltaYaw() / data.getLastDeltaYaw();
        double test2 = data.getYawAccel() / data.getLastYawAccel();
        double gcd = MathUtils.getAbsoluteGcd((float) test, (float) test2);
        if ((gcd < 1E-17 && data.getDeltaYaw() > 0.05) || (Double.isInfinite(test) && Double.isInfinite(test2))) {
            if (increaseBufferBy(1) > 2) {
                fail("gcd=" + gcd + " test=" + test + " test2=" + test2 + " buffer=");
            }
        } else decreaseBufferBy(0.05);
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}

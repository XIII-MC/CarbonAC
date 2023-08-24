package com.xiii.carbon.checks.impl;

import com.xiii.carbon.checks.annotation.Disabled;
import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.annotation.Testing;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;

@Testing
public class Test extends Check {

    public Test(final Profile profile) {
        super(profile, CheckType.TEST, "A", "Test Check for the Developers.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {
        if (!clientPlayPacket.isRotation()) return;
        if (profile.isExempt().isJoined(5000L) || profile.isExempt().isVehicle()) return;
        final RotationData data = profile.getRotationData();
        double test = data.getDeltaYaw() / data.getLastDeltaYaw();
        double test2 = data.getYawAccel() / data.getLastYawAccel();
        double gcd = MathUtils.getAbsoluteGcd((float) test, (float) test2);
        double funnimath = test % (data.getDeltaYaw() + data.getLastDeltaYaw());
        //debug(funnimath);
        if ((gcd < 1E-17 || (Double.isInfinite(test) && Double.isInfinite(test2))) && !Double.isNaN(funnimath)) {
            if (increaseBufferBy(1) > 3) {
                fail("gcd=" + gcd + " test=" + test + " test2=" + test2 + " buffer=" + getBuffer() + " funni=" + funnimath);
            }
        } else decreaseBufferBy(0.05);


            //debug("player=" + profile + " gcd=" + gcd + " test=" + test + " test2=" + test2);


    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}

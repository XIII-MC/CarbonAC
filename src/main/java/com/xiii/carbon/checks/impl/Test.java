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

    int exemptticks = 0;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isRotation()) return;

        if (profile.isExempt().isJoined(5000L) || profile.isExempt().isVehicle()) return;

        final RotationData data = profile.getRotationData();

        final double fix = (data.getLastDeltaYaw() + data.getDeltaYaw()) % (data.getDeltaYaw() + data.getLastDeltaYaw() + 1000);
        if (fix < 0.31) exemptticks++;
        else exemptticks = 0;

        double gcd = MathUtils.getAbsoluteGcd(data.getYawAccel() * 3242869, data.getLastYawAccel() * 3242869) % MathUtils.getAbsoluteGcd(data.getDeltaYaw() * 3242869, data.getLastDeltaYaw() * 3242869);

        if (gcd != 0 && !String.valueOf(gcd).contains("E") && exemptticks < 2) {
            if (increaseBufferBy(1) > 10)
                fail("LITTLE MONKEY BITCH GET FUCKED RETARDED BNASTERDATERED NEINNNN FRUNKFURT " + gcd + " et=" + exemptticks);
        } else if(gcd != 0) resetBuffer();

        debug(profile.getPlayer().getName() + " " + gcd + " et=" + exemptticks + " b=" + getBuffer());


    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}

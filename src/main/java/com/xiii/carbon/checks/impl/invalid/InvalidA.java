package com.xiii.carbon.checks.impl.invalid;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

@Experimental
public class InvalidA extends Check {
    public InvalidA(final Profile profile) {
        super(profile, CheckType.INVALID, "A", "Invalid mouse sensitivity");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isRotation()) {

            final RotationData rotationData = profile.getRotationData();

            final int sensitivity = rotationData.getSensitivityProcessor().getSensitivity();

            if (sensitivity < 0 || sensitivity > 200) fail("sens=" + sensitivity);
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}
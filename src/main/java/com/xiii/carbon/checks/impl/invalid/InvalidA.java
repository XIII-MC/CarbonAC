package com.xiii.carbon.checks.impl.invalid;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

public class InvalidA extends Check {
    public InvalidA(final Profile profile) {
        super(profile, CheckType.INVALID, "A", "Invalid mouse sensitivity");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isRotation()) return;

        final RotationData rotationData = profile.getRotationData();

        final boolean exempt = profile.isExempt().isCinematic();

        final int sensitivity = rotationData.getSensitivityProcessor().getSensitivity();

        if (!exempt && (sensitivity < 0 || sensitivity > 200)) fail("sens: §c" + sensitivity);
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}

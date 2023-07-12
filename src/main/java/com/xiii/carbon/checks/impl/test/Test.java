package com.xiii.carbon.checks.impl.test;

import com.xiii.carbon.checks.annotation.Testing;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.playerdata.processor.impl.SensitivityProcessor;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

@Testing
public class Test extends Check {

    public Test(final Profile profile) {
        super(profile, CheckType.TEST, "A", "Test Check for the Developers.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {
       if (!clientPlayPacket.isRotation()) return;

        RotationData rotationData = profile.getRotationData();

        SensitivityProcessor sensitivityProcessor = rotationData.getSensitivityProcessor();

        final double GCDYaw = sensitivityProcessor.getYawGcd();
        final double GCDPitch = sensitivityProcessor.getPitchGcd();

        final double constantYaw = sensitivityProcessor.getConstantYaw();
        final double constantPitch = sensitivityProcessor.getConstantPitch();

        final boolean invalid = (GCDYaw % constantYaw > 4.0E7 || GCDPitch % constantPitch > 4.0E7) && (constantYaw > 0.7 && constantPitch > 0.7);
        debug("" + sensitivityProcessor.getSensitivity());
        //if (invalid) debug("mx=" + sensitivityProcessor.getMouseX() + " my=" + sensitivityProcessor.getMouseY() + " cy=" + sensitivityProcessor.getConstantYaw() + " cp=" + sensitivityProcessor.getConstantPitch() + " gy=" + sensitivityProcessor.getYawGcd() + " gp=" + sensitivityProcessor.getPitchGcd() + " y=" + rotationData.getDeltaYaw() + " p=" + rotationData.getDeltaPitch());
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}
